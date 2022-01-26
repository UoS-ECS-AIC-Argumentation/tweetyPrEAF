package org.tweetyproject.arg.peaf.syntax;

import java.util.Map;
import java.util.Set;

/**
 * PSupport extends ESupport to have a probability value assigned to the support links (for PEEAF and PEAF)
 *
 * @author Taha Dogan Gunes
 */
public class PSupport extends ESupport {
    /**
     * The probability value for the support link
     */
    private final double conditionalProbability;

    /**
     * The default constructor that creates ESupport object
     *
     * @param name                   the name of the support
     * @param froms                  the arguments that originate this support
     * @param tos                    the arguments that receive this support
     * @param conditionalProbability the probability value for the support link
     */
    public PSupport(String name, Set<EArgument> froms, Set<EArgument> tos, double conditionalProbability) {
        super(name, froms, tos);
        this.conditionalProbability = conditionalProbability;
    }

    /**
     * Returns the support object in string format for debug purposes
     *
     * @return verbose format of the support in string
     */
    @Override
    public String toString() {
        return "PSupp{" + name +
                ", froms=" + froms +
                ", tos=" + tos +
                ", cp=" + conditionalProbability +
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
        return "PSupp{" + name +
                ", froms=" + NamedPEAFTheory.giveNames(names, froms) +
                ", tos=" + NamedPEAFTheory.giveNames(names, tos) +
                ", cp=" + conditionalProbability +
                '}';
    }

    /**
     * Returns the probability assigned to the support
     *
     * @return the probability value
     */
    public double getConditionalProbability() {
        return conditionalProbability;
    }
}
