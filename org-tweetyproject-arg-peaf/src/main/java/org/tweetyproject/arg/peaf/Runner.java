package org.tweetyproject.arg.peaf;

import org.apache.commons.cli.*;
import org.tweetyproject.arg.peaf.analysis.*;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.io.EdgeListReader;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.commons.util.Pair;

import java.io.IOException;
import java.util.Set;

public class Runner {
    public static void main(String[] args) throws IOException {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));

        Options options = new Options();
        Option input = new Option("i", "input", true, "PrEAF file path");
        input.setRequired(true);
        options.addOption(input);

        Option algorithmType = new Option("t", "type", true, "The algorithm type: `exact`, `con_exact`, `approx` and `con_approx`) (default=`approx`)");
        options.addOption(algorithmType);

        Option noThreadsOption = new Option("j", "noThreads", true, "The number of threads (for `con_exact` and `con_approx`) (default=1)");
        options.addOption(noThreadsOption);

        Option errorLevelOption = new Option("e", "errorLevel", true, "The error level for `approx` and `con_approx` (default=0.01)");
        options.addOption(errorLevelOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;//not a good practice, it serves it purpose

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("PEAFSolver", options);

            System.exit(1);
        }

        String inputFilePath = cmd.getOptionValue("input");
        Pair<PEAFTheory, Set<EArgument>> pair = EdgeListReader.readPEAFWithQuery(inputFilePath, false);

        PEAFTheory peaf = pair.getFirst();
        Set<EArgument> query = pair.getSecond();

        System.out.println("Given PrEAF file path: " + inputFilePath);
        System.out.println("The query: " + query.toString());

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

        System.out.println("type,time(ms),justification,peafNodes,iteration,attacks,supports");
        System.out.println(
                type + "," +
                        estimatedTime + "," +
                        justification + "," +
                        peaf.getNumberOfNodes() + "," +
                        iterations + "," +
                        peaf.getAttacks().size() + "," +
                        peaf.getSupports().size());

    }

}
