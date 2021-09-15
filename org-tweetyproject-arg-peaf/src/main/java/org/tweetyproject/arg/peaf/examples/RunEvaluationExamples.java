package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.dung.reasoner.SimplePreferredReasoner;
import org.tweetyproject.arg.peaf.analysis.JustificationAnalysis;
import org.tweetyproject.arg.peaf.inducers.AbstractPEAFInducer;
import org.tweetyproject.arg.peaf.inducers.AllPEAFInducer;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.InducibleEAF;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.arg.peaf.writer.EdgeListReader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

public class RunEvaluationExamples {
    public static void evaluate(String evaluationFolderPath) throws IOException {
        // This is important to make sure generated queries are same across approximate and exact justification runs
        Random random = new Random(9999);

        Path evaluateFolder = Paths.get(evaluationFolderPath);

        if (Files.notExists(evaluateFolder)) {
            throw new RuntimeException("The given path '" + evaluateFolder + "' does not exist.");
        }

        PrintWriter writer = new PrintWriter(Paths.get(evaluationFolderPath.toString(), "results.txt").toString());
        writer.println("type,noNodes,repetition,time,justification");

        String[] graphTypes = evaluateFolder.toFile().list((dir, name) -> new File(dir, name).isDirectory());

        if (graphTypes.length == 0) {
            throw new RuntimeException("There are not any graph type folder(s).");
        }
        Arrays.sort(graphTypes);

        System.out.println("Types found: " + Arrays.toString(graphTypes));

        for (String graphType : graphTypes) {
            Path graphTypePath = Paths.get(evaluationFolderPath, graphType);
            System.out.println("GraphType path: " + graphTypePath.toString());
            String[] nodeSizesString = graphTypePath.toFile().list((dir, name) -> new File(dir, name).isDirectory());
            if (nodeSizesString.length == 0) {
                throw new RuntimeException("There are not any sub folders in " + graphTypePath.toString());
            }
            Integer[] nodeSizes = Arrays.stream(nodeSizesString).map(Integer::parseInt).toArray(Integer[]::new);
            Arrays.sort(nodeSizes);
            System.out.println("Node sizes: " + Arrays.toString(nodeSizes));

            for (Integer nodeSize : nodeSizes) {
                Path nodeSizePath = Paths.get(graphTypePath.toString(), "" + nodeSize);
                System.out.println("Node size path: " + nodeSizePath);
                File[] repetitionFiles = nodeSizePath.toFile().listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String filename) {
                        return filename.endsWith(".peaf");
                    }
                });

                if (repetitionFiles.length == 0) {
                    throw new RuntimeException("There are not any peaf files in this path: " + nodeSizePath);
                }
                Integer[] repetitionFileNames = Arrays.stream(repetitionFiles)
                        .map(File::getName)
                        .map(p -> Integer.parseInt(p.substring(0, p.length() - 5)))
                        .toArray(Integer[]::new);

                Arrays.sort(repetitionFileNames);
                System.out.println(Arrays.toString(repetitionFileNames));

                for (Integer repetitionFileName : repetitionFileNames) {
                    String repetitionFilePath = Paths.get(nodeSizePath.toString(), ("" + repetitionFileName + ".peaf")).toString();
//                    System.out.println("Running: " + repetitionFilePath );
                    PEAFTheory peafTheory = EdgeListReader.readPEAF(repetitionFilePath);
//                    peafTheory.prettyPrint();
                    long startTime = System.nanoTime();


                    Set<EArgument> query = peafTheory.getRandomArguments(random, 1);
                    // AllInducer
                    System.out.println(query.toString());

                    double justification = JustificationAnalysis.computeJustificationOf(query, new AllPEAFInducer(peafTheory), new SimplePreferredReasoner());

                    // SomeInducer
//                    double justification = JustificationAnalysis.approximateJustificationOf(query, peafTheory, new SimplePreferredReasoner(), 0.5);

                    long estimatedTime = System.nanoTime() - startTime;
                    String output = "[RESULT] " + repetitionFilePath + ": " + estimatedTime + " justification: " + justification;
                    System.out.println(output);
                    writer.println(graphType + "," + nodeSize + "," + repetitionFileName + "," + estimatedTime + "," + justification);

//                    break;
                }
//                break;
            }
//            break;
        }

        writer.close();
    }

    public static void main(String[] args) throws IOException {
        RunEvaluationExamples.evaluate("./evaluation");
    }
}
