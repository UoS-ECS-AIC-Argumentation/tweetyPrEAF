package org.tweetyproject.arg.peaf.evaluation;

import org.tweetyproject.arg.peaf.analysis.JustificationAnalysis;
import org.tweetyproject.arg.peaf.inducers.ExactPEAFInducer;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.arg.peaf.io.EdgeListReader;
import org.tweetyproject.commons.util.Pair;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;

public class RunEvaluationExamples {
//    public static void evaluate(String evaluationFolderPath) throws IOException {
//        /* - Inputs */
//        // The inducer name
////        String inducer = "approx";
//        String inducer = "all";
//
//        /* End Inputs */
//
//        // This is important to make sure generated queries are same across approximate and exact justification runs
//        Path evaluateFolder = Paths.get(evaluationFolderPath);
//
//        if (Files.notExists(evaluateFolder)) {
//            throw new RuntimeException("The given path '" + evaluateFolder + "' does not exist.");
//        }
//
//        PrintWriter writer = new PrintWriter(Paths.get(evaluationFolderPath.toString(), "results_" + inducer + ".txt").toString());
//        writer.println("type,dafNodes,repetition,time,justification,peafNodes,iteration");
//
//        String[] graphTypes = evaluateFolder.toFile().list((dir, name) -> new File(dir, name).isDirectory());
//
//        if (graphTypes.length == 0) {
//            throw new RuntimeException("There are not any graph type folder(s).");
//        }
//        Arrays.sort(graphTypes);
//
//        System.out.println("Types found: " + Arrays.toString(graphTypes));
//
//        for (String graphType : graphTypes) {
//            Path graphTypePath = Paths.get(evaluationFolderPath, graphType);
//            System.out.println("GraphType path: " + graphTypePath.toString());
//            String[] nodeSizesString = graphTypePath.toFile().list((dir, name) -> new File(dir, name).isDirectory());
//            if (nodeSizesString.length == 0) {
//                throw new RuntimeException("There are not any sub folders in " + graphTypePath.toString());
//            }
//            Integer[] nodeSizes = Arrays.stream(nodeSizesString).map(Integer::parseInt).toArray(Integer[]::new);
//            Arrays.sort(nodeSizes);
//            System.out.println("Node sizes: " + Arrays.toString(nodeSizes));
//
//            for (Integer nodeSize : nodeSizes) {
//                Path nodeSizePath = Paths.get(graphTypePath.toString(), "" + nodeSize);
//                System.out.println("Node size path: " + nodeSizePath);
//                File[] repetitionFiles = nodeSizePath.toFile().listFiles(new FilenameFilter() {
//                    public boolean accept(File dir, String filename) {
//                        return filename.endsWith(".peaf");
//                    }
//                });
//
//                if (repetitionFiles.length == 0) {
//                    throw new RuntimeException("There are not any peaf files in this path: " + nodeSizePath);
//                }
//                Integer[] repetitionFileNames = Arrays.stream(repetitionFiles)
//                        .map(File::getName)
//                        .map(p -> Integer.parseInt(p.substring(0, p.length() - 5)))
//                        .toArray(Integer[]::new);
//
//                Arrays.sort(repetitionFileNames);
//                System.out.println(Arrays.toString(repetitionFileNames));
//
//                for (Integer repetitionFileName : repetitionFileNames) {
//                    String repetitionFilePath = Paths.get(nodeSizePath.toString(), ("" + repetitionFileName + ".peaf")).toString();
////                    System.out.println("Running: " + repetitionFilePath );
//                    Pair<PEAFTheory, Set<EArgument>> pair = EdgeListReader.readPEAFWithQuery(repetitionFilePath, false);
//                    PEAFTheory peafTheory = pair.getFirst();
//                    Set<EArgument> query = pair.getSecond();
////                    peafTheory.prettyPrint();
//                    long startTime = System.currentTimeMillis();
//
//
//
//                    // AllInducer
//                    System.out.println(query.toString());
//                    Pair<Double, Double> result;
//
//                    if (inducer.equalsIgnoreCase("all")) {
//                        result = JustificationAnalysis.compute(query, new ExactPEAFInducer(peafTheory), new PreferredReasoner());
//
//                    }
//                    else if (inducer.equalsIgnoreCase("approx")) {
//                        result = JustificationAnalysis.computeApproxOf(query, peafTheory, new PreferredReasoner(), 0.01);
//
//                    }
//                    else {
//                        throw new RuntimeException("The given inducer named as '" + inducer + "' does not exist.");
//                    }
//                    double justification = result.getFirst();
//                    double iterations = result.getSecond();
//
//
//                    long estimatedTime = System.currentTimeMillis() - startTime;
//                    String output = "[RESULT] " + repetitionFilePath + ": " + estimatedTime + " justification: " + justification;
//                    System.out.println(output);
//                    writer.println(graphType + "," +
//                                   nodeSize + "," +
//                                   repetitionFileName + "," +
//                                   estimatedTime + "," +
//                                   justification + "," +
//                                   peafTheory.getNumberOfNodes() + "," +
//                                   iterations);
//
////                    break;
//                }
////                break;
//            }
////            break;
//        }
//
//        writer.close();
//    }
//
//    public static void main(String[] args) throws IOException {
//        RunEvaluationExamples.evaluate("./evaluation");
//    }
}
