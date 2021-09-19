package org.tweetyproject.arg.peaf.analysis;


import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.inducers.ApproxPEAFInducer;
import org.tweetyproject.arg.peaf.inducers.ExactPEAFInducer;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.InducibleEAF;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.commons.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class JustificationAnalysis {

    public static <T extends AbstractExtensionReasoner> Pair<Double, Double> computeApproxOf(Set<EArgument> args, PEAFTheory peafTheory, T extensionReasoner, double errorLevel) {

        final double[] M = {0};
        final double[] N = {0};
        final double[] metric = {0};
        final double[] p_i = {0};
        final int[] i = {0};

        final double[] foundPro = {0};
        final double[] totalPro = {0};

        Consumer<InducibleEAF> consumer = new Consumer<InducibleEAF>() {
            @Override
            public void accept(InducibleEAF inducibleEAF) {
                double contribution = JustificationAnalysis.computeJustificationOfASingleEAF(args, inducibleEAF, extensionReasoner);
                foundPro[0] = foundPro[0] + (contribution * inducibleEAF.getInducePro());
                totalPro[0] = totalPro[0] + inducibleEAF.getInducePro();

                M[0] = M[0] + contribution;
                N[0] = N[0] + 1.0;
                p_i[0] = (M[0] + 2) / (N[0] + 4);
                metric[0] = ((4.0 * p_i[0] * (1.0 - p_i[0])) / Math.pow(errorLevel, 2)) - 4.0;
                if (contribution > 0) {
//                    System.out.println("Contributed: " + inducibleEAF);
                }
                else {
//                    System.out.println(inducibleEAF);
                }

//                System.out.println("Simulation step: " + i[0] + " M: " + M[0] + " - N: " + N[0] + " metric: " + metric[0]);
//                System.out.println("Contribution was: " + contribution);
                i[0] += 1;
            }
        };


        do {
            ApproxPEAFInducer inducer = new ApproxPEAFInducer(peafTheory);
            inducer.induce(consumer);

        } while (N[0] <= metric[0]);
        return new Pair<Double, Double>(M[0] / N[0], N[0]);
//        return new Pair<Double, Double>(foundPro[0] / totalPro[0], N[0]);
    }

    public static <T extends AbstractExtensionReasoner> double compute(Set<EArgument> args, ExactPEAFInducer inducer, T extensionReasoner) {
        return compute(args, inducer, extensionReasoner, false, false);
    }

    public static <T extends AbstractExtensionReasoner> double compute(Set<EArgument> args, ExactPEAFInducer inducer, T extensionReasoner, boolean printDAF, boolean printEAF) {
        AtomicReference<Double> prob = new AtomicReference<>((double) 0);
//        AtomicReference<Double> count = new AtomicReference<>((double) 0);
//        AtomicReference<Double> found = new AtomicReference<>((double) 0);

        Set<String> queryStringArgs = new HashSet<>();

        for (EArgument arg : args) {
            queryStringArgs.add(arg.getName());
        }

        AtomicInteger i = new AtomicInteger();
        inducer.induce((Consumer<InducibleEAF>) ind -> {
            // If args is in EAFTheory and if X is in an extension of EAFTheory
            // then induce probability can be considered.

            EAFTheory eafTheory = ind.toNewEAFTheory();

            if (printEAF) {
                System.out.println("\nEAF:");
                System.out.println(ind);
                eafTheory.prettyPrint();
            }
            //  eafTheory.prettyPrint();
//            count.updateAndGet(c -> c + 1.0);
            if (eafTheory.getArguments().containsAll(args)) {
                // If the X is in an extension of IEAF

                DungTheory dungTheory = eafTheory.convertToDAFNaively();

                if (printDAF) {
                    System.out.println("DAF: ");
                    System.out.println(dungTheory.prettyPrint());
                }

                Collection<Extension> extensionCollection = extensionReasoner.getModels(dungTheory);

                for (Extension extension : extensionCollection) {
                    Set<String> subArgsNamesInExtension = new HashSet<>();

                    for (Argument argument : extension) {
                        String mainArgName = argument.getName();
                        if (mainArgName.contains("_")) {
                            // Must contain all subArgsNames from an extension
                            String[] subArgsNames = mainArgName.split("_");
                            Collections.addAll(subArgsNamesInExtension, subArgsNames);
                        } else {
                            subArgsNamesInExtension.add(mainArgName);
                        }
                    }


                    if (subArgsNamesInExtension.containsAll(queryStringArgs) && !extension.isEmpty()) {
//                        System.out.println(" Contributed: " + ind);
//                        System.out.println(prob.get());
                        if (printDAF) {
                            System.out.println("Extension: " + extension);
                            System.out.println("Contribution: " + prob.get());
                        }
                        prob.updateAndGet(v -> (v + ind.getInducePro()));
                        // The number of times

                        break;
                    }
                }


            }

        });

//        return prob.get() / count.get();
        return prob.get();
//        return found.get();
    }

    public static <T extends AbstractExtensionReasoner> double computeJustificationOfASingleEAF(Set<EArgument> args, InducibleEAF inducibleEAF, T extensionReasoner) {

        EAFTheory eafTheory = inducibleEAF.toNewEAFTheory();

        Set<String> queryStringArgs = new HashSet<>();

        for (EArgument arg : args) {
            queryStringArgs.add(arg.getName());
        }
//        eafTheory.prettyPrint();
        if (eafTheory.getArguments().containsAll(args)) {
            // If the X is in an extension of IEAF

            DungTheory dungTheory = eafTheory.convertToDAFNaively();
            Collection<Extension> extensionCollection = extensionReasoner.getModels(dungTheory);

            for (Extension extension : extensionCollection) {
                Set<String> subArgsNamesInExtension = new HashSet<>();

                for (Argument argument : extension) {
                    String mainArgName = argument.getName();
                    if (mainArgName.contains("_")) {
                        // Must contain all subArgsNames from an extension
                        String[] subArgsNames = mainArgName.split("_");
                        Collections.addAll(subArgsNamesInExtension, subArgsNames);
                    } else {
                        subArgsNamesInExtension.add(mainArgName);
                    }
                }


                if (subArgsNamesInExtension.containsAll(queryStringArgs) && !extension.isEmpty()) {
                    return 1.0;
                }
            }
        }

        return 0.0;
    }
}
