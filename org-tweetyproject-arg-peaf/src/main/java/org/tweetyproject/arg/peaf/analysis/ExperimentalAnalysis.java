package org.tweetyproject.arg.peaf.analysis;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;

import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.inducers.ApproxPEAFInducer;
import org.tweetyproject.arg.peaf.inducers.ExactPEAFInducer;
import org.tweetyproject.arg.peaf.syntax.*;
import org.tweetyproject.commons.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ExperimentalAnalysis <T extends AbstractExtensionReasoner> {
    private final PEAFTheory peafTheory;
    private final Set<EArgument> args;
    private final T extensionReasoner;

    public ExperimentalAnalysis(PEAFTheory peafTheory, Set<EArgument> args, T extensionReasoner) {
        this.peafTheory = peafTheory;
        this.args = args;
        this.extensionReasoner = extensionReasoner;
    }

    public <T extends AbstractExtensionReasoner> Pair<Double, Double> compute() {
        // Create a DAF from the nodes that have attack relations
        Map<String, Argument> argumentsInAttack = Maps.newHashMap();
        DungTheory dungTheory = new DungTheory();
        for (EAttack attack : peafTheory.getAttacks()) {
            String from = this.convertEArgumentsToDAFArgumentName(attack.getFroms());

            if (!argumentsInAttack.containsKey(from)) {
                Argument fromArg = new Argument(from);
                argumentsInAttack.put(from, fromArg);
                dungTheory.add(fromArg);
            }

            String to = this.convertEArgumentsToDAFArgumentName(attack.getTos());
            if (!argumentsInAttack.containsKey(to)) {
                Argument toArg = new Argument(to);
                argumentsInAttack.put(to, toArg);
                dungTheory.add(toArg);
            }

            dungTheory.addAttack(argumentsInAttack.get(from), argumentsInAttack.get(to));
        }

        Collection<Extension> extensionCollection = extensionReasoner.getModels(dungTheory);

//        System.out.println("The query: " + args);
//        System.out.println(dungTheory.prettyPrint());
//        System.out.println("Extensions: ");
//        for (Extension ext : extensionCollection) {
//            System.out.println(ext);
//        }
//        System.out.println();

        ExactPEAFInducer peafInducer = new ExactPEAFInducer(peafTheory);

        final double[] p = {0.0};
        final double[] N = {0.0};
        peafInducer.induce(new Consumer<InducibleEAF>() {
            @Override
            public void accept(InducibleEAF inducibleEAF) {

                N[0] = N[0] + 1;
                EAFTheory eafTheory = inducibleEAF.toNewEAFTheory();


                if(!eafTheory.getArgumentsSet().containsAll(args)) {
                    return;
                }

//                System.out.println("---------------------------");
//                System.out.println("---------------------------");
//                eafTheory.prettyPrint();
//                System.out.println("Contribution: " + inducibleEAF.getInducePro());

                // Z: names of query args which are involved in an attack
                Set<String> Z = Sets.newHashSet();

                ArrayList<EAttack> attacks = eafTheory.getAttacks();
                for (EAttack attack : attacks) {
                    Set<EArgument> froms = attack.getFroms();
                    Set<EArgument> tos = attack.getTos();

                    for (EArgument arg : args) {
                        if (froms.contains(arg) || tos.contains(arg)) {
                            Z.add(arg.getName());
                        }
                    }

                }


                // If all arguments are involved in supports
                if (Z.isEmpty() && extensionCollection.size() == 1) {
                    p[0] += inducibleEAF.getInducePro();
                    return;
                }

                // for each extension
                for (Extension ext : extensionCollection) {
                    // [DAFArgument#1 -> (EArgument#1, EArgument#2 ...), DAFArgument#2 -> (EArgument#2 ...)]

                    for (Argument argument : ext) {
                        Set<String> parts = Sets.newHashSet(argument.getName().split("_"));

                        // If extension has all the queries then Z will become empty
                        Z.removeAll(parts);
                        if (Z.isEmpty()) {
                            p[0] += inducibleEAF.getInducePro();
                            return;
                        }
                    }
                }

                System.out.println("END");

            }
        });

        return new Pair<>(p[0], N[0]);
    }


    public <T extends AbstractExtensionReasoner> Pair<Double, Double> computeApprox(double errorLevel) {
        // Create a DAF from the nodes that have attack relations
        Map<String, Argument> argumentsInAttack = Maps.newHashMap();
        DungTheory dungTheory = new DungTheory();
        for (EAttack attack : peafTheory.getAttacks()) {
            String from = this.convertEArgumentsToDAFArgumentName(attack.getFroms());

            if (!argumentsInAttack.containsKey(from)) {
                Argument fromArg = new Argument(from);
                argumentsInAttack.put(from, fromArg);
                dungTheory.add(fromArg);
            }

            String to = this.convertEArgumentsToDAFArgumentName(attack.getTos());
            if (!argumentsInAttack.containsKey(to)) {
                Argument toArg = new Argument(to);
                argumentsInAttack.put(to, toArg);
                dungTheory.add(toArg);
            }

            dungTheory.addAttack(argumentsInAttack.get(from), argumentsInAttack.get(to));
        }

        Collection<Extension> extensionCollection = extensionReasoner.getModels(dungTheory);

//        System.out.println("The query: " + args);
//        System.out.println(dungTheory.prettyPrint());
//        System.out.println("Extensions: ");
//        for (Extension ext : extensionCollection) {
//            System.out.println(ext);
//        }
//        System.out.println();


        final double[] N = {0.0};
        final double[] M = {0};
        final double[] metric = {0};
        final double[] p_i = {0};

        Consumer<InducibleEAF> consumer = new Consumer<InducibleEAF>() {
            private double computeContribution(InducibleEAF inducibleEAF) {
                EAFTheory eafTheory = inducibleEAF.toNewEAFTheory();


                if(!eafTheory.getArgumentsSet().containsAll(args)) {
                    return 0.0;
                }


                // Z: names of query args which are involved in an attack
                Set<String> Z = Sets.newHashSet();

                ArrayList<EAttack> attacks = eafTheory.getAttacks();
                for (EAttack attack : attacks) {
                    Set<EArgument> froms = attack.getFroms();
                    Set<EArgument> tos = attack.getTos();

                    for (EArgument arg : args) {
                        if (froms.contains(arg) || tos.contains(arg)) {
                            Z.add(arg.getName());
                        }
                    }

                }


                // If all arguments are involved in supports
                if (Z.isEmpty() && extensionCollection.size() == 1) {
                    return 1.0;
                }

                // for each extension
                for (Extension ext : extensionCollection) {
                    // [DAFArgument#1 -> (EArgument#1, EArgument#2 ...), DAFArgument#2 -> (EArgument#2 ...)]

                    for (Argument argument : ext) {
                        Set<String> parts = Sets.newHashSet(argument.getName().split("_"));

                        // If extension has all the queries then Z will become empty
                        Z.removeAll(parts);
                        if (Z.isEmpty()) {
                            return 1.0;
                        }
                    }
                }
                return 0.0;
            }

            @Override
            public void accept(InducibleEAF inducibleEAF) {
                double contribution = computeContribution(inducibleEAF);

                M[0] = M[0] + contribution;
                N[0] = N[0] + 1.0;
                p_i[0] = (M[0] + 2) / (N[0] + 4);
                metric[0] = ((4.0 * p_i[0] * (1.0 - p_i[0])) / Math.pow(errorLevel, 2)) - 4.0;
            }
        };

        do {
            ApproxPEAFInducer inducer = new ApproxPEAFInducer(peafTheory);
            inducer.induce(consumer);

        } while (N[0] <= metric[0]);
        return new Pair<Double, Double>(M[0] / N[0], N[0]);
    }

    private String convertEArgumentsToDAFArgumentName(Collection<EArgument> eArguments) {
        String nameOfArgument = eArguments.stream()
                .map(EArgument::getName)
                .sorted()
                .collect(Collectors.joining("_"));
        return nameOfArgument;
    }
}
