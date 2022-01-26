package org.tweetyproject.arg.peaf.syntax;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Evidential argument used for NamedPEAFTheory, PEAFTheory and EAFTheory
 *
 * @author Taha Dogan Gunes
 */
public class EArgument {

    /**
     * The name of the argument
     */
    private String name;
    /**
     * The set of attacks that this argument originates
     */
    private final Set<EAttack> attacks;

    /**
     * The set of supports that this argument originates
     */
    private final Set<ESupport> supports;

    /**
     * Default constructor; initializes an argument with a name
     *
     * @param name the name of the argument
     */
    public EArgument(String name) {
        this.name = name;
        attacks = new HashSet<>();
        supports = new HashSet<>();
    }

    /**
     * Get the name of the argument
     *
     * @return the name of the argument
     */
    public String getName() {
        return name;
    }

    /**
     * Add an attack that this argument which originates the attack (to store internally).
     *
     * @param attack EAttack object
     */
    public void addAttack(EAttack attack) {
        if (attack.getFroms().contains(this)) {
            attacks.add(attack);
        } else {
            throw new RuntimeException("Attack is not from this argument.");
        }
    }

    /**
     * Get all the supports that this argument originates
     *
     * @return a set of supports
     */
    public Set<ESupport> getSupports() {
        return supports;
    }

    /**
     * Add a support to this argument which originates the support (to store internally).
     *
     * @param support ESupport object
     */
    public void addSupport(ESupport support) {
        this.supports.add(support);
    }

    /**
     * Checks if the given argument is supported by this argument
     *
     * @param argument queried argument
     * @return true if the given argument is supported
     */
    public boolean isSupportedBy(EArgument argument) {
        Set<ESupport> supports = argument.getSupports();
        for (ESupport support : supports) {
            Set<EArgument> argSet = support.getTos();
            if (argSet.contains(this)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the attacks that this argument originates
     *
     * @return the set of attacks
     */
    public Set<EAttack> getAttacks() {
        return attacks;
    }

    /**
     * Set the name of this argument
     *
     * @param name the name of the argument
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Arg{" + name + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EArgument argument = (EArgument) o;
        return name.equals(argument.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
