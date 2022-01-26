package org.tweetyproject.arg.peaf.syntax;

import java.util.Set;

/**
 * Evidential support used for EAFTheory
 *
 * @author Taha Dogan Gunes
 */
public class ESupport {

    /**
     * The name of the support
     */
    protected final String name;
    /**
     * The arguments that originate the support
     */
    protected final Set<EArgument> froms;
    /**
     * The arguments that receive the support
     */
    protected final Set<EArgument> tos;

    /**
     * The default constructor that creates ESupport object
     *
     * @param name  the name of the support
     * @param froms the arguments that originate this support
     * @param tos   the arguments that receive this support
     */
    public ESupport(String name, Set<EArgument> froms, Set<EArgument> tos) {
        this.name = name;
        this.froms = froms;
        this.tos = tos;
    }


    /**
     * Returns the support object in string format for debug purposes
     *
     * @return verbose format of the support in string
     */
    @Override
    public String toString() {
        return "ESupp{" + name +
                ", froms=" + froms +
                ", tos=" + tos +
                '}';
    }

    /**
     * Get the arguments that originate this support
     *
     * @return a set of arguments
     */
    public Set<EArgument> getFroms() {
        return froms;
    }

    /**
     * Get the arguments that receive this support
     *
     * @return a set of arguments
     */
    public Set<EArgument> getTos() {
        return tos;
    }

    /**
     * Get the name of the support
     *
     * @return the name of the support
     */
    public String getName() {
        return name;
    }
}
