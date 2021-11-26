package org.tweetyproject.arg.peaf;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.commons.cli.*;
import org.tweetyproject.arg.peaf.analysis.*;
import org.tweetyproject.arg.peaf.inducers.ExactPEAFInducer;
import org.tweetyproject.arg.peaf.inducers.LiExactPEAFInducer;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.io.aif.AIFCISReader;
import org.tweetyproject.arg.peaf.io.aif.AIFtoPEEAFConverter;
import org.tweetyproject.arg.peaf.io.aif.Query;
import org.tweetyproject.arg.peaf.io.preaf.EdgeListWriter;
import org.tweetyproject.arg.peaf.io.preeaf.PEEAFToPEAFConverter;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.InducibleEAF;
import org.tweetyproject.arg.peaf.syntax.NamedPEAFTheory;
import org.tweetyproject.arg.peaf.syntax.PEEAFTheory;
import org.tweetyproject.arg.peaf.syntax.aif.AIFTheory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class AIFInducerRunner {
    public static void main(String[] args) throws IOException {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        Options options = new Options();
        Option input = new Option("i", "input", true, "AIF file path (.json) (required)");
        input.setRequired(true);
        options.addOption(input);

        Option uncertaintyOption = new Option("u", "uncertainty", true, "Uncertainty table file path (.json) (optional, otherwise probabilities will be 0.5.)");
        options.addOption(uncertaintyOption);

        Option algorithmType = new Option("t", "type", true, "The algorithm type: `exact`, `con_exact`, `approx` and `con_approx`) (default=`approx`)");
        options.addOption(algorithmType);
        
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("AIFInducer", options);

            System.exit(1);
        }

        String inputFilePath = cmd.getOptionValue("input");
        String uncertaintyPath = cmd.getOptionValue("uncertainty");

        System.out.println("Given AIF file path: " + inputFilePath);
        System.out.println("Uncertainty table file path: " + uncertaintyPath);

        AIFCISReader reader = new AIFCISReader(inputFilePath);
        AIFTheory aifTheory = reader.read();
        aifTheory.annotateEdges(uncertaintyPath);
        AIFtoPEEAFConverter aifConverter = new AIFtoPEEAFConverter();
        PEEAFTheory peeafTheory = aifConverter.convert(aifTheory);
        PEEAFToPEAFConverter peeafConverter = new PEEAFToPEAFConverter();
        NamedPEAFTheory peaf = peeafConverter.convert(peeafTheory);

        peeafTheory.prettyPrint();
        peaf.prettyPrint();



        System.out.println("Given AIF file path: " + inputFilePath);

        AtomicInteger i = new AtomicInteger();
        ExactPEAFInducer exactPEAFInducer = new ExactPEAFInducer(peaf);
        exactPEAFInducer.induce(iEAF -> {
            int n = i.getAndIncrement();
            System.out.println(n);
            EdgeListWriter.write(  n + ".eaf", iEAF.toNewEAFTheory());
        });

    }
}
