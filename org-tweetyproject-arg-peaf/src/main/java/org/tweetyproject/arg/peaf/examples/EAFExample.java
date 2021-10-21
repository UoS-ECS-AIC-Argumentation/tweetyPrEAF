package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.peaf.syntax.EAFTheory;
import org.tweetyproject.arg.peaf.io.preaf.EdgeListWriter;

public class EAFExample {

    public static void main(String[] _args) {
        int numOfArgs = 8;


        EAFTheory eafTheory = new EAFTheory();

        for (int i = 0; i < numOfArgs; i++) {
            eafTheory.addArgument(i);
        }

        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addSupport(new int[]{2}, new int[]{3});
        eafTheory.addSupport(new int[]{1, 3}, new int[]{3});
        eafTheory.addSupport(new int[]{0}, new int[]{5});
        eafTheory.addSupport(new int[]{0}, new int[]{6});
        eafTheory.addSupport(new int[]{6}, new int[]{7});
        eafTheory.addSupport(new int[]{5, 7}, new int[]{4});

        eafTheory.addAttack(new int[]{5}, new int[]{4});
        eafTheory.addAttack(new int[]{2}, new int[]{6});

        eafTheory.prettyPrint();

        EdgeListWriter.write("eaf.txt", eafTheory);
    }
}
