package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.peaf.inducers.ExactPEAFInducer;
import org.tweetyproject.arg.peaf.inducers.LiExactPEAFInducer;
import org.tweetyproject.arg.peaf.syntax.InducibleEAF;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.function.Consumer;

public class ExpandMCExample {
    public static void main(String[] args) {
        PEAFTheory peafTheory = new PEAFTheory(3);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.5);
        peafTheory.addSupport(0, 2, 0.5);

        System.out.println("The LiExactPEAFInducer:");
        System.out.println();
        LiExactPEAFInducer inducer = new LiExactPEAFInducer(peafTheory);
        inducer.induce(new Consumer<InducibleEAF>() {
            @Override
            public void accept(InducibleEAF inducibleEAF) {
                System.out.println(inducibleEAF);
            }
        });

        System.out.println();
        System.out.println("The ExactPEAFInducer:");
        System.out.println();
        ExactPEAFInducer inducer2 = new ExactPEAFInducer(peafTheory);
        inducer2.induce(new Consumer<InducibleEAF>() {
            @Override
            public void accept(InducibleEAF inducibleEAF) {
                System.out.println(inducibleEAF);
            }
        });
    }
}
