package org.tweetyproject.arg.peaf.syntax;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * This class is an intermediate representation of EAFs that are induced from a PEAF.
 * This is used for making analysis and storing intermediate values while making probabilistic justification
 * of some arguments.
 *
 * @author Taha Dogan Gunes
 */
public class InducibleEAF {
    /**
     * A subset of arguments from PEAF
     */
    public final Set<EArgument> arguments;
    /**
     * A subset of supports from PEAF
     */
    public final Set<PSupport> supports;
    /**
     * A subset of attacks from PEAF
     */
    public final Set<EAttack> attacks;
    /**
     * A subset of arguments from PEAF that can be used to create a new EAF (connected to InducibleEAF)
     */
    public final Set<EArgument> newArguments;
    /**
     *
     */
    public final double pInside;
    /**
     *
     */
    public final double inducePro;

    /**
     * @param arguments
     * @param supports
     * @param attacks
     * @param newArguments
     * @param pInside
     * @param inducePro
     */
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

        arguments.addAll(this.arguments);

        for (PSupport support : supports) {
            eafTheory.addSupport(support);
        }

        for (EAttack attack : attacks) {
            eafTheory.addAttack(attack);
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

        List<EArgument> sortedArgs = Lists.newArrayList();
        sortedArgs.addAll(this.arguments);
        sortedArgs.sort(Comparator.comparing(EArgument::getName));

        for (EArgument argument : sortedArgs) {
            builder.append(argument.getName());
            if (i != arguments.size() - 1) {
                builder.append(",");
            }
            i++;
        }
        builder.append("], supports=[");
        i = 0;

        List<PSupport> sortedSupports = Lists.newArrayList();
        sortedSupports.addAll(this.supports);
        sortedSupports.sort(Comparator.comparing(PSupport::getName));

        for (PSupport support : sortedSupports) {
            builder.append(support.getName());
            if (i != supports.size() - 1) {
                builder.append(",");
            }
            i++;
        }

        List<EAttack> sortedAttacks = Lists.newArrayList();
        sortedAttacks.addAll(this.attacks);
        sortedAttacks.sort(Comparator.comparing(EAttack::getName));

        builder.append("], attacks=[");
        i = 0;
        for (EAttack attack : sortedAttacks) {
            builder.append(attack.getName());
            if (i != attacks.size() - 1) {
                builder.append(",");
            }
            i++;
        }
        builder.append("] induce result=");
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

    public void addAttackLinks() {
        this.attacks.clear();
        Set<EArgument> args = Sets.newHashSet(this.getArguments());

        for (EArgument arg : args) {
            for (EAttack attack : arg.getAttacks()) {
                Set<EArgument> froms = attack.getFroms();
                Set<EArgument> tos = attack.getTos();

                if (args.containsAll(froms) && args.containsAll(tos)) {
                    this.attacks.add(attack);
                }
            }
        }
    }
}
