package org.tweetyproject.arg.peaf.syntax;

import java.util.Set;

public class PSupport extends ESupport {
    private final double conditionalProbability;

    public PSupport(String name, Set<EArgument> froms, Set<EArgument> tos, double conditionalProbability) {
        super(name, froms, tos);
        this.conditionalProbability = conditionalProbability;
    }

    @Override
    public String toString() {
        return "PSupp{" + name +
                ", froms=" + froms +
                ", tos=" + tos +
                ", cp=" + conditionalProbability +
                '}';
    }

    public double getConditionalProbability() {
        return conditionalProbability;
    }
}
