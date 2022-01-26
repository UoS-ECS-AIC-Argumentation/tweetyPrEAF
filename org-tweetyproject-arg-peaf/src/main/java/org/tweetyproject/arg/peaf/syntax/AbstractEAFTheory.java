package org.tweetyproject.arg.peaf.syntax;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractEAFTheory<S> {

    protected EArgument eta;
    protected Set<EArgument> argumentsSet = Sets.newHashSet();


    protected ArrayList<EArgument> arguments = new ArrayList<>();
    protected ArrayList<S> supports = new ArrayList<>();
    protected ArrayList<EAttack> attacks = new ArrayList<>();

    protected void addArgument(EArgument argument) {
        if (arguments.size() == 0) {
            eta = argument;
        }
        arguments.add(argument);
        argumentsSet.add(argument);
    }


    protected boolean addAttack(EAttack attack) {
        return attacks.add(attack);
    }

    protected boolean addSupport(S support) {
        return supports.add(support);
    }

    protected EArgument createArgument(String name) {
        return new EArgument(name);
    }

    public void addAttack(int[] fromIndices, int[] toIndices) {
        Set<EArgument> froms = createEmptyArgSet(fromIndices);
        Set<EArgument> tos = createEmptyArgSet(toIndices);

        int identifier = attacks.size();
        EAttack attack = this.createAttack(Integer.toString(identifier), froms, tos);
        this.addAttack(attack);
    }

    /**
     * Creates an attack object (does not add to the internal abstract object)
     *
     * @param name  the name of the attack
     * @param froms the set of arguments that the attack originates from
     * @param tos   the set of arguments that the attack targets
     * @return EAttack object
     */
    protected EAttack createAttack(String name, Set<EArgument> froms, Set<EArgument> tos) {
        if (tos.contains(eta)) {
            throw new RuntimeException("Argument eta can't be attacked.");
        }
        EAttack attack = new EAttack(name, froms, tos);
        for (EArgument from : froms) {
            from.addAttack(attack);
        }
        return attack;
    }

    protected Set<EArgument> createEmptyArgSet(int[] fromIndices) {
        Set<EArgument> froms = new HashSet<>(fromIndices.length);
        for (int fromIndex : fromIndices) {
            froms.add(arguments.get(fromIndex));
        }
        return froms;
    }

    public EArgument addArgument(int identifier) {
        EArgument argument = this.createArgument(Integer.toString(identifier));
        this.addArgument(argument);
        return argument;
    }

    public Set<EArgument> getArgumentsAsSet() {
        return this.argumentsSet;
    }

    public ArrayList<EArgument> getArguments() {
        return arguments;
    }

    public Set<EArgument> getArgumentsSet() { return argumentsSet; }

    public ArrayList<S> getSupports() {
        return supports;
    }

    public ArrayList<EAttack> getAttacks() {
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
        for (EAttack attack : this.getAttacks()) {
            System.out.println(i + ". " + attack);
            i++;
        }

        System.out.println("\n");
    }

    public int getNumberOfNodes() {
        return arguments.size();
    }

    public EArgument getEta() {
        return eta;
    }
}
