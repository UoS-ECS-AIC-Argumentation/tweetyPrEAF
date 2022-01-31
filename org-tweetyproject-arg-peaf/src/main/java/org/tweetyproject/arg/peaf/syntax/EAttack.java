package org.tweetyproject.arg.peaf.syntax;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Evidential attack used for PEAF and EAF
 *
 * @author Taha Dogan Gunes
 */
public class EAttack {

    /**
     * The name of the attack
     */
    protected final String name;

    /**
     * The arguments that originate the attack
     */
    protected final Set<EArgument> froms;

    /**
     * The arguments that receive the attack
     */
    protected final Set<EArgument> tos;


    /**
     * The default constructor that creates EAttack object
     *
     * @param name  the name of the attack
     * @param froms the arguments that originate this attack
     * @param tos   the arguments that receive this attack
     */
    public EAttack(String name, Set<EArgument> froms, Set<EArgument> tos) {
        this.name = name;
        this.froms = froms;
        this.tos = tos;
    }

    /**
     * Get the name of the attack
     *
     * @return the name of the attack
     */
    public String getName() {
        return name;
    }

    /**
     * Get the arguments that originate this attack
     *
     * @return a set of arguments
     */
    public Set<EArgument> getFroms() {
        return froms;
    }

    /**
     * Get the arguments that receive this attack
     *
     * @return a set of arguments
     */
    public Set<EArgument> getTos() {
        return tos;
    }

    /**
     * Returns the attack object in string format for debug purposes
     *
     * @return verbose format of the attack in string
     */
    @Override
    public String toString() {
        return "EAtt{" + name +
                ", froms=" + froms +
                ", tos=" + tos +
                '}';
    }

    /**
     * Returns the support object in string format for debug purposes by giving names to arguments
     * This is called by NamedPEAFTheory.
     *
     * @param names a map that has the names of the arguments
     * @return verbose format of the support in string
     */
    public String namedToString(Map<EArgument, String> names) {
        return "EAtt{" + name +
                ", froms=" + NamedPEAFTheory.giveNames(names, froms) +
                ", tos=" + NamedPEAFTheory.giveNames(names, tos) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EAttack attack = (EAttack) o;
        return Objects.equals(name, attack.name) && Objects.equals(froms, attack.froms) && Objects.equals(tos, attack.tos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, froms, tos);
    }
}

