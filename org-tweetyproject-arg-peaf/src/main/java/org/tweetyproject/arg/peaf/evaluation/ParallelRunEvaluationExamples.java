package org.tweetyproject.arg.peaf.evaluation;

import org.tweetyproject.arg.peaf.analysis.OldJustificationAnalysis;
import org.tweetyproject.arg.peaf.inducers.LiExactPEAFInducer;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.io.EdgeListReader;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.commons.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


public class ParallelRunEvaluationExamples {
    public static void evaluate(String evaluationFolderPath) throws IOException, InterruptedException {
        /* - Inputs */
        // The inducer name

        String inducer = "approx"; // Approximate Solution with JustificationAnalysis (Single PEAF -> to many EAF to many DAF)
//        String inducer = "exact"; // Exact Solution with JustificationAnalysis (Single PEAF -> to many EAF to many DAF)

        int sizeLimiter = Integer.MAX_VALUE;

        double errorLevel = 0.1;
        int nThreads = 1;
        /* End Inputs */

        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(nThreads);
        ReentrantLock lock = new ReentrantLock();

        // This is important to make sure generated queries are same across approximate and exact justification runs
        Path evaluateFolder = Paths.get(evaluationFolderPath);

        if (Files.notExists(evaluateFolder)) {
            throw new RuntimeException("The given path '" + evaluateFolder + "' does not exist.");
        }

        String filePath = Paths.get(evaluationFolderPath.toString(), "results_" + inducer + ".txt").toString();
        System.out.println("Results will be saved in: " + filePath);
        BufferedWriter writer = new BufferedWriter( new FileWriter(filePath));
        writer.write("type,dafNodes,repetition,time,justification,peafNodes,iteration\n");

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

                if (nodeSize > sizeLimiter) {
                    break;
                }

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
                    if (nThreads != 1 ) {
                        submitTask(inducer, writer, graphType, nodeSize, nodeSizePath, repetitionFileName, threadPoolExecutor, lock, errorLevel);
                    }
                    else {
                        extracted(nodeSizePath, repetitionFileName, lock, inducer, errorLevel, writer, graphType, nodeSize);
                    }

                }
            }
        }

        try {
            threadPoolExecutor.shutdown();
        } finally {
            threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        }

        writer.close();

    }

    private static void submitTask(String inducer, BufferedWriter writer, String graphType, Integer nodeSize, Path nodeSizePath, Integer repetitionFileName, ExecutorService threadPoolExecutor, ReentrantLock lock, double errorLevel) throws IOException {
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                extracted(nodeSizePath, repetitionFileName, lock, inducer, errorLevel, writer, graphType, nodeSize);

            }
        });
    }

    private static void extracted(Path nodeSizePath, Integer repetitionFileName, ReentrantLock lock, String inducer, double errorLevel, BufferedWriter writer, String graphType, Integer nodeSize) {
        String repetitionFilePath = Paths.get(nodeSizePath.toString(), ("" + repetitionFileName + ".peaf")).toString();

        lock.lock();
        try{
            System.out.println("Started: " + repetitionFilePath);
        } finally {
            lock.unlock();
        }


        Pair<PEAFTheory, Set<EArgument>> pair = null;
        try {
            pair = EdgeListReader.readPEAFWithQuery(repetitionFilePath, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PEAFTheory peafTheory = pair.getFirst();
//        peafTheory.prettyPrint();
        Set<EArgument> query = pair.getSecond();
        long startTime = System.currentTimeMillis();


        // AllInducer
        lock.lock();
        try{
            System.out.println(query.toString());
        } finally {
            lock.unlock();
        }

        Pair<Double, Double> result;

        if (inducer.equalsIgnoreCase("exact")) {
            result = OldJustificationAnalysis.compute(query, new LiExactPEAFInducer(peafTheory), new PreferredReasoner());

        }
        else if (inducer.equalsIgnoreCase("approx")) {
            result = OldJustificationAnalysis.computeApproxOf(query, peafTheory, new PreferredReasoner(), errorLevel);
        }
        else {
            throw new RuntimeException("The given inducer named as '" + inducer + "' does not exist.");
        }
        double justification = result.getFirst();
        double iterations = result.getSecond();


        long estimatedTime = System.currentTimeMillis()- startTime;

        lock.lock();
        try{
            String output = "[RESULT] " + repetitionFilePath + ": " + estimatedTime + " justification: " + justification + " iter: " + iterations + " ind: " + inducer;
            System.out.println(output);
            writer.write(graphType + "," +
                    nodeSize + "," +
                    repetitionFileName + "," +
                    estimatedTime + "," +
                    justification + "," +
                    peafTheory.getNumberOfNodes() + "," +
                    iterations + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ParallelRunEvaluationExamples.evaluate("./evaluation");
    }
}
