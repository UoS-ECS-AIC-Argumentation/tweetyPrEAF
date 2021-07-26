package org.tweetyproject.arg.peaf.syntax;

import java.util.*;

public abstract class AbstractEAFTheory<S, A>{

    protected EArgument eta;

    protected ArrayList<EArgument> arguments = new ArrayList<>();
    protected ArrayList<S> supports = new ArrayList<>();
    protected ArrayList<A> attacks = new ArrayList<>();

    protected void addArgument(EArgument argument) {
        if (arguments.size() == 0) {
            eta = argument;
        }
        arguments.add(argument);
    }

    protected boolean addAttack(A attack) {
        return attacks.add(attack);
    }

    protected boolean addSupport(S support) {
        return supports.add(support);
    }

    protected EArgument createArgument(String name) {
        return new EArgument(name);
    }

    protected Set<EArgument> createEmptyArgSet(int[] fromIndices) {
        Set<EArgument> froms = new HashSet<>(fromIndices.length);
        for (int fromIndex : fromIndices) {
            froms.add(arguments.get(fromIndex));
        }
        return froms;
    }

    public void addArgument(int identifier) {
        EArgument argument = this.createArgument(Integer.toString(identifier));
        this.addArgument(argument);
    }

    public ArrayList<EArgument> getArguments() {
        return arguments;
    }

    public ArrayList<S> getSupports() {
        return supports;
    }

    public ArrayList<A> getAttacks() {
        return attacks;
    }

    public void prettyPrint() {
        System.out.println("-- Arguments --");
        int i = 0;
        for (EArgument argument : this.getArguments()) {
            System.out.println(i + ". " + argument);
            i++;
        }

        System.out.println("");
        System.out.println("-- Supports --");
        i = 0;
        for (S support : this.getSupports()) {
            System.out.println(i + ". " + support);
            i++;
        }

        System.out.println("");
        System.out.println("-- Attacks --");
        i = 0;
        for (A attack : this.getAttacks()) {
            System.out.println(i + ". " + attack);
            i++;
        }

        System.out.println("\n");
    }
}
