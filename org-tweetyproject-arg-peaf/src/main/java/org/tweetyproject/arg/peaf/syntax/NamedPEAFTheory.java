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
}
