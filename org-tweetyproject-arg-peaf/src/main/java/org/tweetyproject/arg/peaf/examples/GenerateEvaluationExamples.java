package org.tweetyproject.arg.peaf.examples;


import org.tweetyproject.arg.peaf.evaluation.*;
import org.tweetyproject.arg.peaf.writer.EdgeListWriter;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class GenerateEvaluationExamples {
    public static void main(String[] args) throws IOException {
//        long startTime = System.nanoTime();
//
//        int minNumberOfNodes = 5;
//        int maxNumberOfNodes = 20;
//        int nodeStepSize = 10;
//        int repetition = 10;
//        double someProbability = 0.5;
//
//        // Create evaluation folder
//        Path folder = Paths.get("./evaluation");
//        if (!Files.notExists(folder)) {
//            GenerateEvaluationExamples.deleteFolderAndItsContent(folder);
//        }
//        Files.createDirectory(folder);
//
//        // Create each graph type's folder
//        GraphType[] graphs = {GraphType.WATTS, GraphType.RANDOM, GraphType.BARABASI};
//        // GraphType[] graphs = {GraphType.RANDOM};
//
//        for (int z = 0; z < graphs.length; z++) {
//            GraphType graph = graphs[z];
//            System.out.println("Graph type is: " + graph.toString());
//            Path graphFolder = Paths.get(folder.toString(), graph.toString());
//            Files.createDirectory(graphFolder);
//
//            for (int i = minNumberOfNodes; i < maxNumberOfNodes; i = i + nodeStepSize) {
//                Path nodeFolder = Paths.get(graphFolder.toString(), "" + i);
//                Files.createDirectory(nodeFolder);
//
//                for (int j = 1; j <= repetition; j++) {
//                    SyntheticDAF peaf;
//                    switch (graph) {
//                        case WATTS:
//                            System.out.println(i);
//                            int k = ((int) Math.ceil((double) i /  (double) 2));
//                            if ((k % 2) != 0) {
//                                k -= 1;
//                            }
//                            System.out.println("k = " + k);
//                            peaf = new WattsStrogatzDAF(i, k, 0.5, someProbability);
//                            break;
//
//                        case RANDOM:
//                            peaf = new RandomDAF(i, someProbability);
//                            break;
//
//                        case BARABASI:
//                            peaf = new BarabasiAlbertDAF(i, someProbability);
//                            break;
//
//                        default:
//                            throw new IllegalStateException("Unexpected value: " + graph);
//                    }
//
//                    Path peafFile = Paths.get(nodeFolder.toString(), "" + j + ".peaf");
//                    System.out.println("Creating: " + peafFile.toString());
//                    EdgeListWriter.write(peafFile.toString(), peaf);
//                }
//
//            }
//        }
//
//
//        long estimatedTime = System.nanoTime() - startTime;
//        System.out.println("Elapsed seconds: " + estimatedTime / 1_000_000_000);
    }

    /*
     * Retrieved from: https://stackoverflow.com/questions/20281835/how-to-delete-a-folder-with-files-using-java
     */
    public static void deleteFolderAndItsContent(final Path folder) throws IOException {
        Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc != null) {
                    throw exc;
                }
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
