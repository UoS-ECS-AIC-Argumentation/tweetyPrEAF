package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.peaf.inducers.ExactPEAFInducer;
import org.tweetyproject.arg.peaf.inducers.LiExactPEAFInducer;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

public class ExpandMCExample {
    public static void main(String[] args) {
        PEAFTheory peafTheory = new PEAFTheory(4);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.8);
        peafTheory.addSupport(1, 2, 0.9);
        peafTheory.addSupport(2, 3, 0.9);

        peafTheory.prettyPrint();

        System.out.println("The LiExactPEAFInducer:");
        System.out.println();
        final double[] p = {0.0};
        LiExactPEAFInducer inducer = new LiExactPEAFInducer(peafTheory);
        inducer.induce(inducibleEAF -> {
            System.out.println(inducibleEAF);
            p[0] += inducibleEAF.getInducePro();
        });

        System.out.println("The result is: " + p[0]);
        p[0] = 0.0;

        System.out.println();
        System.out.println("The ExactPEAFInducer:");
        System.out.println();
        ExactPEAFInducer inducer2 = new ExactPEAFInducer(peafTheory);
        inducer2.induce(inducibleEAF -> {
            System.out.println(inducibleEAF);
            p[0] += inducibleEAF.getInducePro();
        });

        System.out.println("The result is: " + p[0]);
    }
}
