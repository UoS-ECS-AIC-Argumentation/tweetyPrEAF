package org.tweetyproject.arg.peaf.syntax;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.jfree.util.ArrayUtilities;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

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
        return Math.exp(inducePro);
    }

    public EAFTheory toNewEAFTheory() {
        EAFTheory eafTheory = new EAFTheory();
        Set<EArgument> arguments = Sets.newHashSet();

        for (PSupport support : supports) {
            eafTheory.addSupport(support);
            arguments.addAll(support.getTos());
            arguments.addAll(support.getFroms());
        }

        for (EAttack attack : attacks) {
            eafTheory.addAttack(attack);
            arguments.addAll(attack.getTos());
            arguments.addAll(attack.getFroms());
        }

        List<EArgument> argsSorted = Lists.newArrayList();
        argsSorted.addAll(arguments);
        argsSorted.sort(Comparator.comparing(EArgument::getName));

        for (EArgument argument : argsSorted) {
            eafTheory.addArgument(argument);
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
        builder.append(Math.exp(inducePro));
        builder.append("}");


        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InducibleEAF that = (InducibleEAF) o;
        return arguments.containsAll(that.arguments)
                && supports.containsAll(that.supports)
                && attacks.containsAll(that.attacks)
                && that.arguments.containsAll(arguments)
                && that.supports.containsAll(supports)
                && that.attacks.containsAll(attacks);
    }

    @Override
    public int hashCode() {
        List<String> all = Lists.newArrayList();
        all.addAll(arguments.stream().map(EArgument::getName).toList());
        all.addAll(supports.stream().map(PSupport::getName).toList());
        all.addAll(attacks.stream().map(EAttack::getName).toList());

        return Objects.hash(all.toArray());
    }
}
