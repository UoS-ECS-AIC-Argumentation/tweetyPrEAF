package org.tweetyproject.arg.peaf.evaluation.converters;

import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;

import java.util.HashMap;
import java.util.Map;

public abstract class DAFToEAFConverter {

    protected EAFTheory convert(DungTheory dungTheory) {
        EAFTheory eafTheory = new EAFTheory();
        eafTheory.addArgument(0);
        // This ignores the nodes that are not connected in a DungTheory
        Map<String, Integer> indices = new HashMap<>();
        int lastIndex = 1; // because eta is already added
        for (Attack attack : dungTheory.getAttacks()) {
            String from = attack.getAttacker().getName();
            int fromIndex;
            if (indices.containsKey(from)) {
                fromIndex = indices.get(from);
            } else {
                eafTheory.addArgument(lastIndex);
                indices.put(from, lastIndex);
                fromIndex = lastIndex;
                lastIndex++;
            }

            String to = attack.getAttacked().getName();
            int toIndex;
            if (indices.containsKey(to)) {
                toIndex = indices.get(to);
            } else {
                eafTheory.addArgument(lastIndex);
                indices.put(to, lastIndex);
                toIndex = lastIndex;
                lastIndex++;
            }

            eafTheory.addAttack(new int[]{fromIndex}, new int[]{toIndex});
        }

        return eafTheory;
    }

}
