package org.tweetyproject.arg.peaf.syntax;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

public class NamedPEAFTheory extends PEAFTheory {

    private Map<EArgument, String> names = Maps.newHashMap();

    public static Set<String> giveNames(Map<EArgument, String> names, Set<EArgument> args) {
        Set<String> argumentNames = Sets.newHashSet();
        for (EArgument arg : args) {
            argumentNames.add(names.get(arg));
        }
        return argumentNames;
    }

    public EArgument addArgument(int identifier, String name) {
        EArgument argument = super.addArgument(identifier);
        names.put(argument, name);
        return argument;
    }

    public void prettyPrint() {
        System.out.println("-- Arguments --");
        int i = 0;
        for (EArgument argument : this.getArguments()) {
            System.out.println(i + ". " + names.get(argument));
            i++;
        }

        System.out.println("");
        System.out.println("-- Supports --");
        i = 0;
        for (PSupport support : this.getSupports()) {
            System.out.println(i + ". " + support.namedToString(names));
            i++;
        }

        System.out.println("");
        System.out.println("-- Attacks --");
        i = 0;
        for (EAttack attack : this.getAttacks()) {
            System.out.println(i + ". " + attack.namedToString(names));
            i++;
        }

        System.out.println("\n");
    }
}
