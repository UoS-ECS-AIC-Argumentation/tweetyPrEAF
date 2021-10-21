package org.tweetyproject.arg.peaf.io.preeaf;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.tweetyproject.arg.peaf.syntax.NamedPEAFTheory;
import org.tweetyproject.arg.peaf.syntax.PEEAFTheory;

import java.util.Map;
import java.util.Set;

public class PEEAFToPEAFConverter {

    public NamedPEAFTheory convert(PEEAFTheory peeafTheory) {
        // Li's Thesis Page 105 - Algorithm 11

        NamedPEAFTheory peafTheory = new NamedPEAFTheory();

        // V4R map
        Map<PEEAFTheory.Element, Integer> virtualMap = Maps.newHashMap();

        // Line 1
        int noArgs = 0;
        Map<PEEAFTheory.Argument, Integer> argToInt = Maps.newHashMap();
        for (PEEAFTheory.Argument argument : peeafTheory.getArguments()) {
            peafTheory.addArgument(noArgs, argument.getName());
            argToInt.put(argument, noArgs);
            noArgs++;
        }

        // Line 2-7
        for (PEEAFTheory.Support support : peeafTheory.getSupports()) {
            // virtual argument for support line 3
            peafTheory.addArgument(noArgs, "virtual" + noArgs);
            int virtualArgumentIndex = noArgs;
            noArgs++;

            int[] fromIndices = checkAndGetArguments(argToInt, support.getFroms());
            Integer toIndex = checkAndGetArgument(argToInt, support.getTo());

            peafTheory.addSupport(fromIndices, new int[]{virtualArgumentIndex}, support.getProbability());
            peafTheory.addSupport(new int[]{virtualArgumentIndex}, new int[]{toIndex}, 1.0);

            // Line 6
            virtualMap.put(support, virtualArgumentIndex);
        }

        // Line 8-14
        Set<PEEAFTheory.Attack> attacks = Sets.newHashSet(peeafTheory.getAttacks());
        for (PEEAFTheory.Attack attack : peeafTheory.getAttacks()) {
            PEEAFTheory.Element element = attack.getTo();
            if (element instanceof PEEAFTheory.Argument) {
                // virtual argument for support line 9 and 16
                peafTheory.addArgument(noArgs, "virtual" + noArgs);
                int virtualArgumentIndex = noArgs;
                noArgs++;

                int fromIndex = checkAndGetArgument(argToInt, attack.getFrom());
                peafTheory.addSupport(fromIndex, virtualArgumentIndex, attack.getProbability());


                int toIndex = checkAndGetArgument(argToInt, (PEEAFTheory.Argument) attack.getTo());
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
                peafTheory.addArgument(noArgs, "virtual" + noArgs);
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

    private Integer checkAndGetArgument(Map<PEEAFTheory.Argument, Integer> argToInt, PEEAFTheory.Argument argument) {
        Integer index = argToInt.get(argument);
        if (index == null) {
            throw new RuntimeException("The index of the argument: `" + argument + "` was not found.");
        }
        return index;
    }

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
