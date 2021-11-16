package org.tweetyproject.arg.peaf;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.commons.cli.*;
import org.tweetyproject.arg.peaf.analysis.*;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.io.aif.AIFReader;
import org.tweetyproject.arg.peaf.io.aif.AIFtoPEEAFConverter;
import org.tweetyproject.arg.peaf.io.aif.Query;
import org.tweetyproject.arg.peaf.io.preeaf.PEEAFToPEAFConverter;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.NamedPEAFTheory;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.arg.peaf.syntax.PEEAFTheory;
import org.tweetyproject.arg.peaf.syntax.aif.AIFTheory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class AIFRunner {
    public static void main(String[] args) throws IOException {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        Options options = new Options();
        Option input = new Option("i", "input", true, "AIF file path (required)");
        input.setRequired(true);
        options.addOption(input);

        Option queryOption = new Option("q", "query", true, "Query file path (required)");
        queryOption.setRequired(true);
        options.addOption(queryOption);

        Option algorithmType = new Option("t", "type", true, "The algorithm type: `exact`, `con_exact`, `approx` and `con_approx`) (default=`approx`)");
        options.addOption(algorithmType);

        Option noThreadsOption = new Option("j", "noThreads", true, "The number of threads (for `con_exact` and `con_approx`) (default=1)");
        options.addOption(noThreadsOption);

        Option errorLevelOption = new Option("e", "errorLevel", true, "The error level for `approx` and `con_approx` (default=0.01)");
        options.addOption(errorLevelOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("AIFSolver", options);

            System.exit(1);
        }

        String inputFilePath = cmd.getOptionValue("input");
        String queryFilePath = cmd.getOptionValue("query");

        AIFReader reader = new AIFReader(inputFilePath);
        AIFTheory aifTheory = reader.read();
        AIFtoPEEAFConverter aifConverter = new AIFtoPEEAFConverter();
        PEEAFTheory peeafTheory = aifConverter.convert(aifTheory);
        PEEAFToPEAFConverter peeafConverter = new PEEAFToPEAFConverter();
        NamedPEAFTheory peaf = peeafConverter.convert(peeafTheory);

        peeafTheory.prettyPrint();
        peaf.prettyPrint();


        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new FileReader(queryFilePath));

        Query queryObject = gson.fromJson(jsonReader, Query.class);

        Set<EArgument> query = Sets.newHashSet();
        for (String iNodeID : queryObject.iNodeIDs) {
            EArgument eArgument = peaf.getArgument(iNodeID);
            if (eArgument == null) {
                throw new RuntimeException("The given nodeID as `" + iNodeID + "` does not exist in the given AIF.");
            }
            query.add(eArgument);
        }

        System.out.println("Given AIF file path: " + inputFilePath);
        System.out.println("The query: " + Arrays.toString(queryObject.iNodeIDs));

        String errorLevelString = cmd.getOptionValue("errorLevel", "0.01");
        double errorLevel = Double.parseDouble(errorLevelString);

        if (errorLevel <= 0 || errorLevel >= 1.0) {
            throw new RuntimeException("Error level must be in the range of (0.0, 1.0).");
        }

        System.out.println("The error level is: " + errorLevel);

        String noThreadsString = cmd.getOptionValue("noThreads", "1");
        int noThreads = Integer.parseInt(noThreadsString);

        if (noThreads <= 0) {
            throw new RuntimeException("The number of threads must be higher than 0.");
        }

        System.out.println("No threads: " + noThreads);

        String type = cmd.getOptionValue("type", "approx");
        AnalysisType analysisType = AnalysisType.get(type);

        AbstractAnalysis analysis;
        switch (analysisType) {
            case EXACT:
                analysis = new ExactAnalysis(peaf, new PreferredReasoner());
                break;
            case APPROX:
                analysis = new ApproxAnalysis(peaf, new PreferredReasoner(), errorLevel);
                break;
            case CONCURRENT_EXACT:
                analysis = new ConcurrentExactAnalysis(peaf, new PreferredReasoner(), noThreads);
                break;
            case CONCURRENT_APPROX:
                analysis = new ConcurrentApproxAnalysis(peaf, new PreferredReasoner(), errorLevel, noThreads);
                break;
            default:
                throw new RuntimeException("The analysis type that is named as '" + type + "' does not exist.");
        }

        long startTime = System.currentTimeMillis();
        AnalysisResult result = analysis.query(query);

        double justification = result.getProbability();
        double iterations = result.getNoIterations();
        long estimatedTime = System.currentTimeMillis() - startTime;

        System.out.println("type,time(ms),justification,peafNodes,iteration,attacks,supports,aifNodes");
        System.out.println(
                type + "," +
                        estimatedTime + "," +
                        justification + "," +
                        peaf.getNumberOfNodes() + "," +
                        iterations + "," +
                        peaf.getAttacks().size() + "," +
                        peaf.getSupports().size() + "," +
                        aifTheory.nodeMap.size()
            );

    }

}
