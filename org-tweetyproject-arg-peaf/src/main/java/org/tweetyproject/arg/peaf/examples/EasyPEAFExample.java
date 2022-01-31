package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.peaf.inducers.LiExactPEAFInducer;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

public class EasyPEAFExample {
    public static void main(String[] args) {
        PEAFTheory peafTheory = new PEAFTheory(2);


        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(new int[]{0}, new int[]{1}, 0.3);


        peafTheory.prettyPrint();
        LiExactPEAFInducer inducer = new LiExactPEAFInducer(peafTheory);

        inducer.induce(ind -> {
            System.out.println(ind);


        });
    }
}
