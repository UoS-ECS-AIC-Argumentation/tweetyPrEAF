package org.tweetyproject.arg.peaf.syntax;

import java.util.Set;

public class PAttack extends EAttack {
        private final double conditionalProbability;

        public PAttack(String name, Set<EArgument> froms, Set<EArgument> tos, double conditionalProbability) {
            super(name, froms, tos);
            this.conditionalProbability = conditionalProbability;
        }

        @Override
        public String toString() {
            return "PAtt{" + name +
                    ", froms=" + froms +
                    ", tos=" + tos +
                    ", cp=" + conditionalProbability +
                    '}';
        }

}
