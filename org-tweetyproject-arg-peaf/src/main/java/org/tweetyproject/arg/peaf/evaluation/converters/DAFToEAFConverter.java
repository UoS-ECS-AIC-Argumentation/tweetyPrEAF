package org.tweetyproject.arg.peaf.evaluation.converters;

import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;

import java.util.HashMap;
import java.util.Map;

/**
 * The abstract class DAFToEAFConverter provides skeleton EAF structure for concrete implementations
 *
 * @author Taha Dogan Gunes
 */
public abstract class DAFToEAFConverter {

    /**
     * Generates EAFTheory objects with only attacks from the given DungTheory object
     * It is up to the implementations to decide what will be the supports (including eta supporting others)
     *
     * @param dungTheory a DungTheory object
     * @return an EAFTheory object
     */
    protected EAFTheory convert(DungTheory dungTheory) {
        EAFTheory eafTheory = new EAFTheory();
        eafTheory.addArgument(0);
        // This ignores the nodes that are not connected in a DungTheory
        Map<String, Integer> indices = new HashMap<>();
        int lastIndex = 1; // because eta is already added

        for (Argument node : dungTheory.getNodes()) {
            indices.put(node.getName(), lastIndex);
            eafTheory.addArgument(lastIndex);
            lastIndex++;
        }

        for (Attack attack : dungTheory.getAttacks()) {
            String from = attack.getAttacker().getName();
            String to = attack.getAttacked().getName();

            int fromIndex = indices.get(from);
            int toIndex = indices.get(to);

            eafTheory.addAttack(new int[]{fromIndex}, new int[]{toIndex});
        }

        return eafTheory;
    }

}
