package org.tweetyproject.arg.peaf.evaluation;


import org.tweetyproject.arg.dung.reasoner.SimplePreferredReasoner;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.peaf.evaluation.converters.EAFToPEAFConverter;
import org.tweetyproject.arg.peaf.evaluation.converters.EtaToAllConverter;
import org.tweetyproject.arg.peaf.evaluation.converters.EtaToTreeConverter;
import org.tweetyproject.arg.peaf.evaluation.daf.*;
import org.tweetyproject.arg.peaf.io.EdgeListWriter;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class GenerateEvaluationExamples {
    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();

        int minNumberOfNodes = 5;
        int maxNumberOfNodes = 11;
        int nodeStepSize = 1;
        int repetition = 10;
        double someProbability = 0.5;

        // Create evaluation folder
        Path folder = Paths.get("./evaluation");
        if (!Files.notExists(folder)) {
            GenerateEvaluationExamples.deleteFolderAndItsContent(folder);
        }
        Files.createDirectory(folder);

        // Create each graph type's folder
        GraphType[] graphs = {GraphType.WATTS, GraphType.RANDOM, GraphType.BARABASI};
        // GraphType[] graphs = {GraphType.RANDOM};

        for (int z = 0; z < graphs.length; z++) {
            GraphType graph = graphs[z];
            System.out.println("Graph type is: " + graph.toString());
            Path graphFolder = Paths.get(folder.toString(), graph.toString());
            Files.createDirectory(graphFolder);

            for (int i = minNumberOfNodes; i < maxNumberOfNodes; i = i + nodeStepSize) {
                Path nodeFolder = Paths.get(graphFolder.toString(), "" + i);
                Files.createDirectory(nodeFolder);

                for (int j = 1; j <= repetition; j++) {
                    SyntheticDAF daf;
                    switch (graph) {
                        case WATTS:
                            System.out.println(i);
                            int k = ((int) Math.ceil((double) i /  (double) 2));
                            if ((k % 2) != 0) {
                                k -= 1;
                            }
                            System.out.println("k = " + k);
                            daf = new WattsStrogatzDAF(i, k, 0.5, someProbability);
                            break;

                        case RANDOM:
                            daf = new RandomDAF(i, someProbability);
                            break;

                        case BARABASI:
                            daf = new BarabasiAlbertDAF(i, someProbability);
                            break;

                        default:
                            throw new IllegalStateException("Unexpected value: " + graph);
                    }

                    EtaToAllConverter eafConverter = new EtaToAllConverter();
                    EAFTheory eafTheory = eafConverter.convert(daf);

//                    EtaToTreeConverter eafConverter = new EtaToTreeConverter();
//                    EAFTheory eafTheory = eafConverter.convert(daf, true, 1.0);

                    PEAFTheory peaf = EAFToPEAFConverter.convert(eafTheory, 10, 2);


                    System.out.println("DAF:");
                    SimplePreferredReasoner reasoner = new SimplePreferredReasoner();
                    Extension queryExtension = null;
                    for (Extension model : reasoner.getModels(daf)) {
                        System.out.println(model);
                        queryExtension = model;
                        break;
                    }

                    if (queryExtension != null) {
                        if (queryExtension.isEmpty()) {
                            j = j - 1;
                            continue;
                        }
                        Set<EArgument> query = new HashSet<>();
                        for (Argument argument : queryExtension) {
                            System.out.println("Arg: "+argument.getName());
                            // Important: Plus one here because DAF arguments are in range of [0, n], EAF args [1, n + 1]
                            try {
                                query.add(peaf.getArguments().get(Integer.parseInt(argument.getName()) + 1));
                            } catch (IndexOutOfBoundsException exception) {
                                System.out.println();
                                System.out.println();
                                System.out.println(daf.prettyPrint());
                                System.err.println(exception.getMessage());
                                peaf.prettyPrint();
                                throw new RuntimeException();
                            }

                        }

                        Path peafFile = Paths.get(nodeFolder.toString(), "" + j + ".peaf");
                        System.out.println("Creating: " + peafFile.toString());
                        EdgeListWriter.write(peafFile.toString(), peaf, query);
                    }


                }

            }
        }


        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Elapsed seconds: " + estimatedTime / 1_000_000_000);
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
