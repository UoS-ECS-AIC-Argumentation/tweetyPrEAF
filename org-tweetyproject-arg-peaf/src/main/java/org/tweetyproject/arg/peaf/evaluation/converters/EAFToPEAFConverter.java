package org.tweetyproject.arg.peaf.evaluation.converters;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.BetaDistributionImpl;
import org.tweetyproject.arg.peaf.syntax.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class EAFToPEAFConverter {

    public static PEAFTheory convert(EAFTheory eafTheory, Supplier<Double> supportSupplier, Supplier<Double> attackSupplier) {
        PEAFTheory peafTheory = new PEAFTheory();

        Map<EArgument, Integer> argumentIndexMap = new HashMap<>();
        for (int i = 0; i < eafTheory.getNumberOfNodes(); i++) {
            EArgument argument = peafTheory.addArgument(i);
            argumentIndexMap.put(argument, i);
        }

        // This is important for making inducers work properly.
        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);

        for (ESupport support : eafTheory.getSupports()) {
            int[] fromIndices = getIndices(argumentIndexMap, support.getFroms());
            int[] toIndices = getIndices(argumentIndexMap, support.getTos());

            peafTheory.addSupport(fromIndices, toIndices, supportSupplier.get());
        }

        for (EAttack attack : eafTheory.getAttacks()) {
            int[] fromIndices = getIndices(argumentIndexMap, attack.getFroms());
            int[] toIndices = getIndices(argumentIndexMap, attack.getTos());
            peafTheory.addAttack(fromIndices, toIndices, attackSupplier.get());
        }

        return peafTheory;
    }

    private static int[] getIndices(Map<EArgument, Integer> argumentIndexMap, Set<EArgument> froms) {
        int[] fromIndices = new int[froms.size()];
        int i = 0;

        for (EArgument from : froms) {
            fromIndices[i] = argumentIndexMap.get(from);
            i++;
        }
        return fromIndices;
    }

    /**
     * Generates a PEAF from an EAF where all relations have the same probability
     * @param eafTheory
     * @param probability
     * @return
     */
    public static PEAFTheory convert(EAFTheory eafTheory, double probability) {
        Supplier<Double> supplier = () -> probability;
        return EAFToPEAFConverter.convert(eafTheory, supplier, supplier);
    }

    /**
     * Generates a PEAF from an EAF where `supp ~ Beta(alphaSupp, betaSupp)` and `att ~ Beta(alphaAtt, betaAtt)`
     * `supp` is the probability of each support relation.
     * `att` is the probability of each attack relation.
     * `Beta` is the Beta distribution.
     * @param eafTheory
     * @param alphaSupp
     * @param betaSupp
     * @param alphaAtt
     * @param betaAtt
     * @return
     */
    public static PEAFTheory convert(EAFTheory eafTheory, int alphaSupp, int betaSupp, int alphaAtt, int betaAtt) {
        class BetaSupplier implements Supplier<Double> {
            private final BetaDistributionImpl betaDistribution;

            BetaSupplier(int alpha, int beta) {
                betaDistribution = new BetaDistributionImpl(alpha, beta);
            }

            @Override
            public Double get() {
                try {
                    return betaDistribution.inverseCumulativeProbability(ThreadLocalRandom.current().nextDouble());
                } catch (MathException e) {
                    e.printStackTrace();
                    return 0.0;
                }
            }
        }

        Supplier<Double> supplierSupp = new BetaSupplier(alphaSupp, betaSupp);
        Supplier<Double> supplierAtt = new BetaSupplier(alphaAtt, betaAtt);

        return EAFToPEAFConverter.convert(eafTheory, supplierSupp, supplierAtt);
    }
}