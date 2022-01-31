package org.tweetyproject.arg.peaf.evaluation.converters;

import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The EtaToTreeConverter class converts a DungTheory object to an EAFTheory by
 * retaining the attacks in DungTheory object and creates a tree structure of supports.
 * <p>
 * An example can be, if the input is:
 * ```
 * DAF:
 * argument(a_1).
 * argument(a_2).
 * <p>
 * attack(a_1,a_2).
 * ```
 * <p>
 * The conversion would result:
 * ```
 * EAF:
 * -- Arguments --
 * 0. Arg{0}
 * 1. Arg{1}
 * 2. Arg{2}
 * 3. Arg{3}
 * <p>
 * -- Supports --
 * 0. ESupp{0, froms=[Arg{3}], tos=[Arg{1}]}
 * 1. ESupp{1, froms=[Arg{3}], tos=[Arg{2}]}
 * 2. ESupp{2, froms=[Arg{0}], tos=[Arg{3}]}
 * <p>
 * -- Attacks --
 * 0. EAtt{0, froms=[Arg{1}], tos=[Arg{2}]}
 * ```
 *
 * @author Taha Dogan Gunes
 */
public class EtaToTreeConverter extends DAFToEAFConverter {

    /**
     * Converts a DungTheory object into EAFTheory object while creating a tree support structure.
     *
     * @param dungTheory        a DungTheory object
     * @param etaFullyConnected true if eta supports all arguments
     * @param dependencyDegree  the probability of having intermediate arguments
     * @return an EAFTheory object
     */
    public EAFTheory convert(DungTheory dungTheory, boolean etaFullyConnected, double dependencyDegree) {
        EAFTheory eafTheory = super.convert(dungTheory);
        // At this point, eta is not connected to any other argument

        List<Integer> interNodes = new ArrayList<>();
        Set<Integer> S = new HashSet<>();
        // Skip eta in S
        for (int i = 1; i < eafTheory.getNumberOfArguments(); i++) {
            S.add(i);
        }
        Integer s = 1; // skipping eta here

        while (!S.isEmpty()) {
            double r = ThreadLocalRandom.current().nextDouble();
            // If r is larger than dependencyDegree ignore the s, and get a new s from S
            if (r > dependencyDegree) {
                S.remove(s);

                s = this.getAnElementFromSet(S);
                // Even if s is null the while loop will terminate
                continue;
            }

            // If r is smaller or equal to dependencyDegree
            S.remove(s);
            Integer sPrime = getAnElementFromSet(S);
            if (sPrime == null) {
                break;
            }
            S.remove(sPrime);

            int alphaIndex = eafTheory.getNumberOfArguments();
            eafTheory.addArgument(alphaIndex);
            interNodes.add(alphaIndex);

            //noinspection ConstantConditions
            eafTheory.addSupport(new int[]{alphaIndex}, new int[]{s});
            eafTheory.addSupport(new int[]{alphaIndex}, new int[]{sPrime});

            s = alphaIndex;
        }

        if (etaFullyConnected) {
            // All other arguments are connected
            for (int i = 1; i < eafTheory.getNumberOfArguments(); i++) {
                eafTheory.addSupport(new int[]{0}, new int[]{i});
            }
        } else {
            if (!interNodes.isEmpty()) {
                eafTheory.addSupport(new int[]{0}, new int[]{interNodes.get(interNodes.size() - 1)});
            }
            // If there is no interNodes and eta not fully connected
            else {
                if (eafTheory.getNumberOfArguments() > 1) {
                    // connect to one argument to get a valid eaf
                    eafTheory.addSupport(new int[]{0}, new int[]{1});
                }
            }
        }

        return eafTheory;
    }

    /**
     * Internal utility function that gets the first element from the set otherwise null
     *
     * @param set a Set object
     * @param <T> some class
     * @return the first element from the set
     */
    private <T> T getAnElementFromSet(Set<T> set) {
        for (T t : set) {
            return t;
        }
        return null;
    }
}
