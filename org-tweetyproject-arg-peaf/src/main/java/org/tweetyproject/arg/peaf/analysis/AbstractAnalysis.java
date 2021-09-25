package org.tweetyproject.arg.peaf.analysis;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.syntax.*;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractAnalysis  implements JustificationAnalysis {

    protected final PEAFTheory peafTheory;
    private final AbstractExtensionReasoner extensionReasoner;
    protected final AnalysisType analysisType;

    public AbstractAnalysis(PEAFTheory peafTheory, AbstractExtensionReasoner extensionReasoner, AnalysisType analysisType) {
        this.peafTheory = peafTheory;
        this.extensionReasoner = extensionReasoner;
        this.analysisType = analysisType;
    }

    protected AnalysisResult createResult(double probability, long noIterations, double totalProbability) {
        return new AnalysisResult(probability, noIterations, this.analysisType, totalProbability);
    }

    protected double computeContributionOfAniEAF(Set<EArgument> args, InducibleEAF inducibleEAF) {
        EAFTheory eafTheory = inducibleEAF.toNewEAFTheory();

        // If Q is not a subset of A, the contribution is zero
        if (!eafTheory.getArgumentsSet().containsAll(args)) {
            return 0.0;
        }

        // If R^F_a is empty, then directly return 1.0
        if (eafTheory.getAttacks().isEmpty()) {
            return 1.0;
        }

        // Create Q' (this is the number of nodes that have attack relation)
        // If Q' equals to \emptyset (then return 1.0);
        Set<String> Q_prime = this.getAttackQueries(eafTheory, args);

        if (Q_prime.isEmpty()) {
            return 1.0;
        }

        // Create a DAF from arguments that are involved in an attack
        DungTheory dungTheory = this.createDAF(eafTheory);

        // Solve (<A^F, R^F_a>)
        Collection<Extension> extensions = extensionReasoner.getModels(dungTheory);

        for (Extension extension : extensions) {
            Set<String> extensionArgs = new HashSet<>();

            for (Argument argument : extension) {
                String mainArgName = argument.getName();
                if (mainArgName.contains("_")) {
                    // Must contain all subArgsNames from an extension
                    String[] subArgsNames = mainArgName.split("_");
                    Collections.addAll(extensionArgs, subArgsNames);
                } else {
                    extensionArgs.add(mainArgName);
                }
            }


            if (extensionArgs.containsAll(Q_prime) && !extension.isEmpty()) {
                return 1.0;
            }
        }

        return 0.0;
    }

    private Set<String> getAttackQueries(EAFTheory eafTheory, Set<EArgument> args) {
        Set<String> Q_prime = Sets.newHashSet();

        ArrayList<EAttack> attacks = eafTheory.getAttacks();
        for (EAttack attack : attacks) {
            Set<EArgument> froms = attack.getFroms();
            Set<EArgument> tos = attack.getTos();

            for (EArgument arg : args) {
                if (froms.contains(arg) || tos.contains(arg)) {
                    Q_prime.add(arg.getName());
                }
            }
        }
        return Q_prime;
    }

    private DungTheory createDAF(EAFTheory eafTheory) {
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
        return dungTheory;
    }

    private String convertEArgumentsToDAFArgumentName(Set<EArgument> eArguments) {
        String nameOfArgument = eArguments.stream()
                .map(EArgument::getName)
                .sorted()
                .collect(Collectors.joining("_"));
        return nameOfArgument;
    }
}
