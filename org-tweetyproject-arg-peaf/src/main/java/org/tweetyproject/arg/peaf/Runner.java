package org.tweetyproject.arg.peaf;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.apache.commons.cli.*;
import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.peaf.analysis.*;
import org.tweetyproject.arg.peaf.analysis.extensions.ExtensionAnalysis;
import org.tweetyproject.arg.peaf.analysis.extensions.GroundedAnalysis;
import org.tweetyproject.arg.peaf.analysis.extensions.PreferredAnalysis;
import org.tweetyproject.arg.peaf.analysis.voi.KLDivergenceAnalysis;
import org.tweetyproject.arg.peaf.analysis.voi.MaximiseChangeAnalysis;
import org.tweetyproject.arg.peaf.analysis.voi.MinimiseEntropyAnalysis;
import org.tweetyproject.arg.peaf.analysis.voi.TargetOutputAnalysis;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.GroundReasoner;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.io.aif.AIFCISReader;
import org.tweetyproject.arg.peaf.io.aif.AIFtoPEEAFConverter;
import org.tweetyproject.arg.peaf.io.preeaf.PEEAFToPEAFConverter;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.NamedPEAFTheory;
import org.tweetyproject.arg.peaf.syntax.PEEAFTheory;
import org.tweetyproject.arg.peaf.syntax.aif.AIFJSONTheory;
import org.tweetyproject.arg.peaf.syntax.aif.AIFTheory;
import org.tweetyproject.arg.peaf.syntax.aif.analysis.AIFJSONAnalysis;
import org.tweetyproject.arg.peaf.syntax.aif.analysis.AIFJSONAnalysisResult;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Set;

/**
 * The main runner of the arg.peaf package
 * - Takes an aif file as an input
 * - Generates a new aif file as an output with `analysis` field
 *
 * @author Taha Dogan Gunes
 */
public class Runner {
    /**
     * Run justification analysis with an aif file
     *
     * @param args the given arguments
     * @throws IOException can be thrown while reading and/or writing aif files
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        Options options = new Options();
        Option input = new Option("i", "input", true, "AIF file path (.json) (required)");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "AIF file path (.json) (required)");
        output.setRequired(true);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Runner", options);

            System.exit(1);
        }

        String inputFilePath = cmd.getOptionValue("input");
        String outputFilePath = cmd.getOptionValue("output");
        System.out.println("Given AIF file path: " + inputFilePath);

        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new FileReader(inputFilePath));
        AIFJSONTheory aifJSON = gson.fromJson(jsonReader, AIFJSONTheory.class);

        AIFCISReader reader = new AIFCISReader(inputFilePath);
        AIFTheory aifTheory = reader.read(aifJSON);
        AIFtoPEEAFConverter aifConverter = new AIFtoPEEAFConverter();
        PEEAFTheory peeafTheory = aifConverter.convert(aifTheory);
        PEEAFToPEAFConverter peeafConverter = new PEEAFToPEAFConverter();
        peeafTheory.prettyPrint();

        NamedPEAFTheory peaf = peeafConverter.convert(peeafTheory);
        peaf.prettyPrint();
        peaf.prettyPrintWithoutNames();

        if (aifJSON.analyses != null) {
            runAnalyses(outputFilePath, aifJSON, peeafTheory, peeafConverter);
        } else {
            System.err.println("Analyses field in the given aif json was not found.");
        }


        try (Writer writer = new FileWriter(outputFilePath)) {
            Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
            gsonPretty.toJson(aifJSON, writer);
        }
    }

    private static void runAnalyses(String outputFilePath, AIFJSONTheory aifJSON, PEEAFTheory peeafTheory, PEEAFToPEAFConverter peeafConverter) throws IOException {
        NamedPEAFTheory peaf;
        int count = 0;
        for (AIFJSONAnalysis analysis : aifJSON.analyses) {
            StringBuilder builder = new StringBuilder();
            System.out.println("\n > Starting #" + ++count + " analysis with: " + analysis.reasoner.type + "\n");

            AnalysisType type = AnalysisType.get(analysis.reasoner.type);
            if (analysis.result == null) {
                analysis.result = new AIFJSONAnalysisResult();
            }
            analysis.result.datetime = "" + java.util.Calendar.getInstance().getTime();

            double errorLevel = 0.1;
            int noThreads = 1;
            Set<EArgument> target = Sets.newHashSet();
            Set<EArgument> objective = Sets.newHashSet();

            AbstractExtensionReasoner reasoner = new PreferredReasoner();
            peaf = peeafConverter.convert(peeafTheory);

            if (analysis.reasoner.parameters != null) {
                errorLevel = analysis.reasoner.parameters.errorLevel;
                if ((errorLevel <= 0 || errorLevel >= 1.0) && (type == AnalysisType.APPROX || type == AnalysisType.CONCURRENT_APPROX)) {
                    printLogWarning(builder, "Warning: Error level must be in the range of (0.0, 1.0). Using default error level, which is 0.1.\n");
                    errorLevel = 0.1;
                }

                noThreads = analysis.reasoner.parameters.noThreads;
                if (noThreads <= 0 && (type == AnalysisType.APPROX || type == AnalysisType.CONCURRENT_APPROX)) {
                    printLogWarning(builder, "Warning: The number of threads must be higher than 0. Using default noThreads, which is 1.\n");
                    noThreads = 1;
                }

                if (analysis.reasoner.parameters.target != null) {
                    fillTheSetWithArgs(target, analysis.reasoner.parameters.target, peaf, builder);
                }

                if (analysis.reasoner.parameters.objective != null) {
                    System.out.println("Objective is given as: ");
                    fillTheSetWithArgs(objective, analysis.reasoner.parameters.objective, peaf, builder);
                }

                if (analysis.reasoner.parameters.semantics != null) {
                    switch (analysis.reasoner.parameters.semantics) {
                        case "preferred" -> reasoner = new PreferredReasoner();
                        case "grounded" -> reasoner = new GroundReasoner();
                    }
                } else {
                    printLogWarning(builder, "Warning: The semantics were not specified. Using default semantics, which is preferred.\n");
                }
            }


            boolean isQueryExpected = true;
            AbstractAnalysis abstractAnalysis;
            if (type == null) {
                throw new RuntimeException("The analysis type does not exist. The analysis types are: " + Arrays.toString(AnalysisType.values()));
            }
            switch (type) {
                case EXACT -> abstractAnalysis = new ExactAnalysis(peaf, reasoner);
                case APPROX -> abstractAnalysis = new ApproxAnalysis(peaf, reasoner, errorLevel);
                case CONCURRENT_EXACT -> abstractAnalysis = new ConcurrentExactAnalysis(peaf, reasoner, noThreads);
                case CONCURRENT_APPROX -> abstractAnalysis = new ConcurrentApproxAnalysis(peaf, reasoner, errorLevel, noThreads);
                case PREFERRED -> {
                    // Convert peaf -> eaf -> daf, then run jargsemsat
                    abstractAnalysis = new PreferredAnalysis(peaf);
                    isQueryExpected = false;
                }
                case GROUNDED -> {
                    // Convert peaf -> eaf -> daf, then run jargsemsat
                    abstractAnalysis = new GroundedAnalysis(peaf);
                    isQueryExpected = false;
                }
                case VOI_TARGET_OUTPUT -> abstractAnalysis = new TargetOutputAnalysis<>(peaf,
                        reasoner,
                        target,
                        new ExactAnalysis(peaf, reasoner), objective);
                case VOI_MAXIMISE_CHANGE -> abstractAnalysis = new MaximiseChangeAnalysis<>(peaf,
                        reasoner,
                        new ExactAnalysis(peaf, reasoner), objective);
                case VOI_MINIMISE_ENTROPY -> abstractAnalysis = new MinimiseEntropyAnalysis<>(peaf,
                        reasoner,
                        new ExactAnalysis(peaf, reasoner), objective);
                case VOI_KL_DIVERGENCE -> abstractAnalysis = new KLDivergenceAnalysis<>(peaf,
                        reasoner,
                        new ExactAnalysis(peaf, reasoner), objective);
                default -> throw new RuntimeException("The analysis type that is named as '" + type + "' does not exist.");
            }

            String[] queries = analysis.query;

            if (isQueryExpected && (queries == null || analysis.query.length == 0)) {
                builder.append("Error: The query was given as empty or null.\n");
                System.err.println("Error: The query was given as empty or null.");
                analysis.result.status = builder.toString();


                try (Writer writer = new FileWriter(outputFilePath)) {
                    Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
                    gsonPretty.toJson(aifJSON, writer);
                }

                System.exit(1);
            }

            long startTime = System.currentTimeMillis();
            analysis.result.status = builder.toString();
            if (type == AnalysisType.PREFERRED || type == AnalysisType.GROUNDED) {
                analysis.result.status = builder.toString();
                ExtensionAnalysis preferredAnalysis = (ExtensionAnalysis) abstractAnalysis;
                analysis.result.outcome = Arrays.toString(preferredAnalysis.getExtensions().toArray());
            } else {
                if ((type == AnalysisType.APPROX || type == AnalysisType.CONCURRENT_APPROX)) {
                    System.out.println("The error level is: " + errorLevel);
                    System.out.println("No threads: " + noThreads);
                }


                Set<EArgument> query = Sets.newHashSet();
                System.out.println("The query (in text):");

                fillTheSetWithArgs(query, queries, peaf, builder);
                System.out.println("The query (in internal format): " + query);

                AnalysisResult result = abstractAnalysis.query(query);
                double justification = result.getResult();
                analysis.result.outcome = "" + justification;
                analysis.result.noIterations = "" + result.getNoIterations();
            }
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Completed in " + elapsedTime + " ms, the result is: " + analysis.result.outcome + ".");

            builder.append("Success");

            analysis.result.elapsedTimeMS = "" + elapsedTime;

            analysis.result.status = builder.toString();
        }
    }

    private static void printLogWarning(StringBuilder builder, String message) {
        builder.append(message);
        System.err.println(message);
    }

    private static void fillTheSetWithArgs(Set<EArgument> target, String[] target1, NamedPEAFTheory peaf, StringBuilder builder) {
        for (String iNodeID : target1) {
            EArgument eArgument = peaf.getArgumentByIdentifier(iNodeID);
            if (eArgument == null) {
                builder.append("The given nodeID as `").append(iNodeID).append("` does not exist in the given AIF.");
                System.err.println("The given nodeID as `" + iNodeID + "` does not exist in the given AIF.");
                break;
            }
            target.add(eArgument);
            System.out.println("`" + iNodeID + "`: " + peaf.getArgumentNameFromIdentifier(iNodeID));
        }
    }

}
