package org.tweetyproject.arg.peaf.analysis;


import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.inducers.AllPEAFInducer;
import org.tweetyproject.arg.peaf.inducers.SomePEAFInducer;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.InducibleEAF;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class JustificationAnalysis {

    public static <T extends AbstractExtensionReasoner> double approximateJustificationOf(Set<EArgument> args, PEAFTheory peafTheory, T extensionReasoner, double errorLevel) {

        final double[] M = {0};
        final double[] N = {0};
        final double[] metric = {0};
        final double[] p_i = {0};
        final int[] i = {0};

        Consumer<InducibleEAF> consumer = new Consumer<InducibleEAF>() {
            @Override
            public void accept(InducibleEAF inducibleEAF) {
                double contribution = JustificationAnalysis.computeJustificationOfASingleEAF(args, inducibleEAF, extensionReasoner);
                M[0] = M[0] + contribution;
                N[0] = N[0] + 1.0;
                p_i[0] = (M[0] + 2) / (N[0] + 4);
                metric[0] = ((4.0 * p_i[0] * (1.0 - p_i[0])) / Math.pow(errorLevel, 2)) - 4.0;
//                System.out.println(inducibleEAF);
//                System.out.println("Simulation step: " + i[0] + " M: " + M[0] + " - N: " + N[0] + " metric: " + metric[0]);
//                System.out.println("Contribution was: " + contribution);
                i[0] += 1;
            }
        };


        do {
            SomePEAFInducer inducer = new SomePEAFInducer(peafTheory);
            inducer.induce(consumer);


        } while (N[0] <= metric[0]);
        return M[0] / N[0];
    }

    public static <T extends AbstractExtensionReasoner> double computeJustificationOf(Set<EArgument> args, AllPEAFInducer inducer, T extensionReasoner) {
        AtomicReference<Double> prob = new AtomicReference<>((double) 0);

        Set<String> queryStringArgs = new HashSet<>();

        for (EArgument arg : args) {
            queryStringArgs.add(arg.getName());
        }

        AtomicInteger i = new AtomicInteger();
        inducer.induce((Consumer<InducibleEAF>) ind -> {
            // If args is in EAFTheory and if X is in an extension of EAFTheory
            // then induce probability can be considered.
            EAFTheory eafTheory = ind.toNewEAFTheory();
            //  eafTheory.prettyPrint();
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
                        prob.updateAndGet(v -> (v + ind.getInducePro()));
                    }
                }


            }

        });

        return prob.get();
    }

    public static <T extends AbstractExtensionReasoner> double computeJustificationOfASingleEAF(Set<EArgument> args, InducibleEAF inducibleEAF, T extensionReasoner) {

        EAFTheory eafTheory = inducibleEAF.toNewEAFTheory();

        Set<String> queryStringArgs = new HashSet<>();

        for (EArgument arg : args) {
            queryStringArgs.add(arg.getName());
        }

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
