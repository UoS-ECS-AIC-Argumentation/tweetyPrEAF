package org.tweetyproject.arg.peaf.syntax;

import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class InducibleEAF {
    public final Set<EArgument> arguments;
    public final Set<PSupport> supports;
    public final Set<EAttack> attacks;
    public final Set<EArgument> newArguments;
    public final double pInside;
    public final double inducePro;

    public InducibleEAF(Set<EArgument> arguments,
                        Set<PSupport> supports,
                        Set<EAttack> attacks,
                        Set<EArgument> newArguments,
                        double pInside, double inducePro) {

        this.arguments = arguments;
        this.supports = supports;
        this.attacks = attacks;
        this.newArguments = newArguments;
        this.pInside = pInside;
        this.inducePro = inducePro;
    }

    public Set<EArgument> getArguments() {
        return arguments;
    }

    public Set<PSupport> getSupports() {
        return supports;
    }

    public Set<EArgument> getNewArguments() {
        return newArguments;
    }


    public double getpInside() {
        return pInside;
    }

    public double getInducePro() {
        return inducePro;
    }

    public EAFTheory toNewEAFTheory() {
        EAFTheory eafTheory = new EAFTheory();
        for (PSupport support : supports) {
            eafTheory.addSupport(support);
        }
        List<EArgument> sorted = Lists.newArrayList(arguments);
        sorted.sort(Comparator.comparing(EArgument::getName));
        for (EArgument argument : sorted) {
            eafTheory.addArgument(argument);
        }

        for (EAttack attack : attacks) {
            eafTheory.addAttack(attack);
        }
        return eafTheory;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("InducibleEAF{");
        builder.append("arguments=[");
        int i = 0;
        for (EArgument argument : arguments) {
            builder.append(argument.getName());
            if (i != arguments.size() - 1) {
                builder.append(",");
            }
            i++;
        }
        builder.append("], supports=[");
        i = 0;
        for (PSupport support : supports) {
            builder.append(support.getName());
            if (i != supports.size() - 1) {
                builder.append(",");
            }
            i++;
        }

        builder.append("], attacks=[");
        i = 0;
        for (EAttack attack : attacks) {
            builder.append(attack.getName());
            if (i != attacks.size() - 1) {
                builder.append(",");
            }
            i++;
        }
        builder.append("] induce probability=");
        builder.append(inducePro);
        builder.append("}");


        return builder.toString();
    }
}
