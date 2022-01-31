package org.tweetyproject.arg.peaf.syntax;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * A base class for implementing PEAFTheory and EAFTheory.
 *
 * @param <S> the support link (ESupport or PSupport)
 * @author Taha Dogan Gunes
 */
public abstract class AbstractEAFTheory<S extends ESupport> {

    /**
     * The root node of the EAFTheory
     */
    protected EArgument eta;
    /**
     * Arguments stored in a set
     */
    protected Set<EArgument> argumentsSet = Sets.newHashSet();

    /**
     * Arguments stored in a list
     */
    protected ArrayList<EArgument> arguments = new ArrayList<>();
    /**
     * Supports stored in a list
     */
    protected ArrayList<S> supports = new ArrayList<>();
    /**
     * Attacks stored in a list
     */
    protected ArrayList<EAttack> attacks = new ArrayList<>();

    /**
     * Add an argument
     *
     * @param argument an EArgument objects
     */
    protected void addArgument(EArgument argument) {
        if (arguments.size() == 0) {
            eta = argument;
        }
        arguments.add(argument);
        argumentsSet.add(argument);
    }


    /**
     * Add an attack
     *
     * @param attack an EAttack object
     * @return true if successful
     */
    protected boolean addAttack(EAttack attack) {
        return attacks.add(attack);
    }

    /**
     * Add a support
     *
     * @param support a T-Support object
     * @return true if successful
     */
    protected boolean addSupport(S support) {
        return supports.add(support);
    }

    /**
     * Creates an argument with a name
     *
     * @param name the name of the argument in string
     * @return the created EArgument object
     */
    protected EArgument createArgument(String name) {
        return new EArgument(name);
    }

    /**
     * Add an attack with indices
     *
     * @param fromIndices indices of arguments that originates the attack
     * @param toIndices   indices of arguments that receive the attack
     */
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

    /**
     * Create a set of arguments from the given indices
     *
     * @param fromIndices the indices of arguments requested
     * @return the set of arguments
     */
    protected Set<EArgument> createEmptyArgSet(int[] fromIndices) {
        Set<EArgument> froms = new HashSet<>(fromIndices.length);
        for (int fromIndex : fromIndices) {
            froms.add(arguments.get(fromIndex));
        }
        return froms;
    }

    /**
     * Create and add a new argument with identifier
     *
     * @param identifier integer identifier for the argument
     * @return the created argument
     */
    public EArgument addArgument(int identifier) {
        EArgument argument = this.createArgument(Integer.toString(identifier));
        this.addArgument(argument);
        return argument;
    }

    /**
     * Get all arguments
     *
     * @return a set of arguments
     */
    public Set<EArgument> getArgumentsAsSet() {
        return this.argumentsSet;
    }

    /**
     * @return a list of arguments
     */
    public ArrayList<EArgument> getArguments() {
        return arguments;
    }

    /**
     * Get all supports
     *
     * @return a list of supports
     */
    public ArrayList<S> getSupports() {
        return supports;
    }

    /**
     * Get all attacks
     *
     * @return a list of attacks
     */
    public ArrayList<EAttack> getAttacks() {
        return attacks;
    }

    /**
     * Pretty print of the EAFTheory
     */
    public void prettyPrint() {
        System.out.println("-- Arguments --");
        int i = 0;
        for (EArgument argument : this.getArguments()) {
            System.out.println(i + ". " + argument);
            i++;
        }

        System.out.println();
        System.out.println("-- Supports --");
        i = 0;
        for (S support : this.getSupports()) {
            System.out.println(i + ". " + support);
            i++;
        }

        System.out.println();
        System.out.println("-- Attacks --");
        i = 0;
        for (EAttack attack : this.getAttacks()) {
            System.out.println(i + ". " + attack);
            i++;
        }

        System.out.println("\n");
    }

    /**
     * Get all the number of arguments
     *
     * @return the number of arguments
     */
    public int getNumberOfArguments() {
        return arguments.size();
    }

    /**
     * Get the eta (the root) argument
     *
     * @return eta argument
     */
    public EArgument getEta() {
        return eta;
    }
}
