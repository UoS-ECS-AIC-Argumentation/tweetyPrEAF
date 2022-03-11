package org.tweetyproject.arg.peaf.syntax;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

/**
 * This class inherits PEAFTheory to store additional information regarding the arguments inserted
 *
 * @author Taha Dogan Gunes
 */
public class NamedPEAFTheory extends PEAFTheory {

    /**
     * Internal map for getting names of the arguments given their reference
     */
    private final Map<EArgument, String> namesMap = Maps.newHashMap();
    /**
     * Internal map for getting arguments given the AIF identifier
     */
    private final Map<String, EArgument> reverseAIFMap = Maps.newHashMap();
    private final Map<EArgument, String> aifMap = Maps.newHashMap();

    /**
     * Helper function to give the names of a set of arguments
     * Static such that it can work with PEAFTheory nodes (EArgument, EAttack and PSupport)
     *
     * @param names the map that has arguments as keys and the names of arguments as string
     * @param args  the arguments that are queried
     * @return the set of names
     */
    public static Set<String> giveNames(Map<EArgument, String> names, Set<EArgument> args) {
        Set<String> argumentNames = Sets.newHashSet();
        for (EArgument arg : args) {
            argumentNames.add(names.get(arg));
        }
        return argumentNames;
    }

    /**
     * Returns the argument's name give its identifier
     *
     * @param identifier the identifier of the argument
     * @return the argument's name
     */
    public String getArgumentNameFromIdentifier(String identifier) {
        return namesMap.get(getArgumentByIdentifier(identifier));
    }

    /**
     * Returns the EArgument object given its identifier
     *
     * @param identifier the identifier of the argument
     * @return corresponding EArgument object
     */
    public EArgument getArgumentByIdentifier(String identifier) {
        return reverseAIFMap.get(identifier);
    }

    /**
     * Returns the name of the argument given EArgument object's reference
     *
     * @param argument EArgument reference
     * @return the name in string
     */
    public String getNameOfArgument(EArgument argument) {
        return namesMap.get(argument);
    }

    /**
     * Add argument with names
     *
     * @param identifier        PEAF identifier as an integer value (index of the argument for efficiency reasons)
     * @param name              The given name of the argument
     * @param aifNodeIdentifier The aif node identifier
     * @return EArgument object given
     */
    public EArgument addArgument(int identifier, String name, String aifNodeIdentifier) {
        EArgument argument = super.addArgument(identifier);
        namesMap.put(argument, name);
        reverseAIFMap.put(aifNodeIdentifier, argument);
        aifMap.put(argument, aifNodeIdentifier);
        return argument;
    }

    /**
     * Print the NamedPEAFTheory for debugging purposes
     */
    public void prettyPrint() {
        System.out.println("NamedPEAF:");
        System.out.println("-- Arguments --");
        int i = 0;
        for (EArgument argument : this.getArguments()) {
            System.out.println(i + ". " + namesMap.get(argument));
            i++;
        }

        System.out.println();
        System.out.println("-- Supports --");
        i = 0;
        for (PSupport support : this.getSupports()) {
            System.out.println(i + ". " + support.namedToString(namesMap));
            i++;
        }

        System.out.println();
        System.out.println("-- Attacks --");
        i = 0;
        for (EAttack attack : this.getAttacks()) {
            System.out.println(i + ". " + attack.namedToString(namesMap));
            i++;
        }

        System.out.println("\n");
    }

    public void prettyPrintWithoutNames() {
        super.prettyPrint();
    }

    public NamedPEAFTheory createCopyWithoutArguments(Set<EArgument> args) {
        NamedPEAFTheory newPEAFTheory = new NamedPEAFTheory();

        int noArgs = 0;

//        peafTheory.addArgument(noArgs, argument.getName(), argument.getIdentifier());
        for (EArgument argument : this.arguments) {
            if (!args.contains(argument)) {
                newPEAFTheory.addArgument(noArgs++, namesMap.get(argument), this.aifMap.get(argument));
            }
        }
        newPEAFTheory.addSupport(new int[]{}, new int[]{0}, 1.0);

        for (EAttack attack : this.attacks) {
            Set<EArgument> froms = this.filter(attack.getFroms(), args);
            Set<EArgument> tos = this.filter(attack.getTos(), args);

            if (froms.isEmpty() || tos.isEmpty()) {
                continue;
            }

            Set<EArgument> newFroms = this.convert(froms, newPEAFTheory);
            Set<EArgument> newTos = this.convert(tos, newPEAFTheory);

            newPEAFTheory.addAttack(newFroms, newTos);
        }

        for (PSupport support : this.supports) {
            Set<EArgument> froms = this.filter(support.getFroms(), args);
            Set<EArgument> tos = this.filter(support.getTos(), args);

            if (froms.isEmpty() || tos.isEmpty()) {
                continue;
            }

            Set<EArgument> newFroms = this.convert(froms, newPEAFTheory);
            Set<EArgument> newTos = this.convert(tos, newPEAFTheory);

            newPEAFTheory.addSupport(newFroms, newTos, support.getConditionalProbability());
        }


        return newPEAFTheory;
    }

    private Set<EArgument> convert(Set<EArgument> originalArgs, NamedPEAFTheory newPEAFTheory) {
        Set<EArgument> conversions = Sets.newHashSet();

        for (EArgument originalArg : originalArgs) {
            String aifIdentifier = this.aifMap.get(originalArg);
            EArgument newArg = newPEAFTheory.getArgumentByIdentifier(aifIdentifier);
            conversions.add(newArg);
        }

        return conversions;
    }

    private Set<EArgument> filter(Set<EArgument> originalArgs, Set<EArgument> filter) {
        Set<EArgument> filteredSet = Sets.newHashSet();

        for (EArgument arg : originalArgs) {
            if (!filter.contains(arg)) {
                filteredSet.add(arg);
            }
        }

        return filteredSet;
    }

    public String getIdentifier(EArgument e) {
        return this.aifMap.get(e);
    }
}
