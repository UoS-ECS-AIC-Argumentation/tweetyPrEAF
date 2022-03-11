package org.tweetyproject.arg.peaf.io.preeaf;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.tweetyproject.arg.peaf.syntax.NamedPEAFTheory;
import org.tweetyproject.arg.peaf.syntax.PEEAFTheory;

import java.util.Map;
import java.util.Set;

/**
 * PEAAFToPEAFConverter converts PEEAFTheory to PEAFTheory
 * The conversion is based on
 * </br>
 * </br>See
 * </br>
 * </br> Li, Hengfei. Probabilistic argumentation. 2015. PhD Thesis. Aberdeen University.
 *
 * @author Taha Dogan Gunes
 */
public class PEEAFToPEAFConverter {
    /**
     * During the conversion we assume a default probability for the virtual nodes that gets added.
     */
    private static final double DEFAULT_PROBABILITY = 1.0;

    /**
     * The conversion uses Algorithm 11 from Li's Thesis in page 105.
     * The general idea is to add virtual arguments on relations that are not supported by PEAFTheory.
     *
     * @param peeafTheory the PEEAFTheory object
     * @return NamedPEAFTheory object
     */
    public NamedPEAFTheory convert(PEEAFTheory peeafTheory) {
        // Li's Thesis Page 105 - Algorithm 11
        NamedPEAFTheory peafTheory = new NamedPEAFTheory();

        if (peeafTheory.getArguments().size() == 0) {
            return peafTheory;
        }


        // V4R map
        Map<PEEAFTheory.Element, Integer> virtualMap = Maps.newHashMap();

        // Line 1
        int noArgs = 0;
        Map<PEEAFTheory.Argument, Integer> argToInt = Maps.newHashMap();
        for (PEEAFTheory.Argument argument : peeafTheory.getArguments()) {
            peafTheory.addArgument(noArgs, argument.getName(), argument.getIdentifier());

            if (argument.getName().equalsIgnoreCase("eta")) {
                // This is important for making inducers work properly.
                peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
            }

            argToInt.put(argument, noArgs);
            noArgs++;
        }

        // Line 2-7
        for (PEEAFTheory.Support support : peeafTheory.getSupports()) {
            // virtual argument for support line 3
            int[] fromIndices = checkAndGetArguments(argToInt, support.getFroms());
            Integer toIndex = checkAndGetArgument(argToInt, support.getTo());

            // Don't add virtual arguments
            if (Math.abs(support.getProbability() - 1.0) < 0.0001) {
                peafTheory.addSupport(fromIndices, new int[]{toIndex}, DEFAULT_PROBABILITY);
                continue;
            }
            if (fromIndices.length == 1) {
                peafTheory.addSupport(fromIndices, new int[]{toIndex}, support.getProbability());
                continue;
            }

            peafTheory.addArgument(noArgs, "virtual" + noArgs, "virtual" + noArgs);
            int virtualArgumentIndex = noArgs;
            noArgs++;

            peafTheory.addSupport(fromIndices, new int[]{virtualArgumentIndex}, support.getProbability());
            peafTheory.addSupport(new int[]{virtualArgumentIndex}, new int[]{toIndex}, DEFAULT_PROBABILITY);

            // Line 6
            virtualMap.put(support, virtualArgumentIndex);
        }

        // Line 8-14
        Set<PEEAFTheory.Attack> attacks = Sets.newHashSet(peeafTheory.getAttacks());
        for (PEEAFTheory.Attack attack : peeafTheory.getAttacks()) {
            PEEAFTheory.Element element = attack.getTo();
            if (element instanceof PEEAFTheory.Argument) {
                int fromIndex = checkAndGetArgument(argToInt, attack.getFrom());
                int toIndex = checkAndGetArgument(argToInt, (PEEAFTheory.Argument) attack.getTo());

                // Don't add virtual arguments
                if (Math.abs(attack.getProbability() - 1.0) < 0.0001) {
                    peafTheory.addAttack(fromIndex, toIndex);
                    continue;
                }

                // virtual argument for support line 9 and 16
                peafTheory.addArgument(noArgs, "virtual" + noArgs, "virtual" + noArgs);
                int virtualArgumentIndex = noArgs;
                noArgs++;


                peafTheory.addSupport(fromIndex, virtualArgumentIndex, attack.getProbability());
                peafTheory.addAttack(virtualArgumentIndex, toIndex);

                virtualMap.put(attack, virtualArgumentIndex);
                attacks.remove(attack);
            }
        }

        // Line 15-20:
        for (PEEAFTheory.Attack attack : attacks) {
            PEEAFTheory.Element element = attack.getTo();
            if (!(element instanceof PEEAFTheory.Argument)) {
                // virtual argument for support line 9 and 16
                peafTheory.addArgument(noArgs, "virtual" + noArgs, "virtual" + noArgs);
                int virtualArgumentIndex = noArgs;
                noArgs++;

                int fromIndex = checkAndGetArgument(argToInt, attack.getFrom());
                peafTheory.addSupport(fromIndex, virtualArgumentIndex, attack.getProbability());


                Integer toIndex = virtualMap.get(attack.getTo());
                if (toIndex == null) {
                    throw new RuntimeException("The attack/support (`" + attack.getTo() + " ` ) is not in virtualMap.");
                }
                peafTheory.addAttack(virtualArgumentIndex, toIndex);

                virtualMap.put(attack, virtualArgumentIndex);
            }
        }

        return peafTheory;
    }

    /**
     * Utility function to fetch and argument with an error check.
     *
     * @param argToInt PEEAFTheory.Argument to identifier map
     * @param argument queried PEEAFTheory.Argument
     * @return the corresponding identifier
     */
    private Integer checkAndGetArgument(Map<PEEAFTheory.Argument, Integer> argToInt, PEEAFTheory.Argument argument) {
        Integer index = argToInt.get(argument);
        if (index == null) {
            throw new RuntimeException("The index of the argument: `" + argument + "` was not found.");
        }
        return index;
    }

    /**
     * Utility function to fetch a set of arguments with an error check.
     *
     * @param argToInt  PEEAFTheory.Argument to identifier map
     * @param arguments a set of queried PEEAFTheory.Argument
     * @return the corresponding identifiers
     */
    private int[] checkAndGetArguments(Map<PEEAFTheory.Argument, Integer> argToInt, Set<PEEAFTheory.Argument> arguments) {
        if (arguments.size() == 0) {
            throw new RuntimeException("The number of arguments are zero.");
        }

        int[] indices = new int[arguments.size()];
        int i = 0;
        for (PEEAFTheory.Argument argument : arguments) {
            indices[i] = checkAndGetArgument(argToInt, argument);
            i++;
        }

        return indices;
    }

}
