package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.peaf.inducers.LiExactPEAFInducer;
import org.tweetyproject.arg.peaf.io.preaf.EdgeListWriter;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.concurrent.atomic.AtomicInteger;

public class AllPEAFInducerExample {
    public static void main(String[] args) {
        int numOfArgs = 8;


        PEAFTheory peafTheory = new PEAFTheory();

        for (int i = 0; i < numOfArgs; i++) {
            peafTheory.addArgument(i);
        }

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(new int[]{0}, new int[]{1}, 0.3);
        peafTheory.addSupport(new int[]{1}, new int[]{2}, 0.8);
        peafTheory.addSupport(new int[]{1}, new int[]{3}, 0.9);
        peafTheory.addSupport(new int[]{2}, new int[]{4}, 0.85);
        peafTheory.addSupport(new int[]{3}, new int[]{5}, 0.5);
        peafTheory.addSupport(new int[]{3}, new int[]{6}, 0.6);
        peafTheory.addSupport(new int[]{5, 4}, new int[]{7}, 0.4);

        peafTheory.addAttack(new int[]{5}, new int[]{4});
        peafTheory.addAttack(new int[]{2}, new int[]{6});

        peafTheory.prettyPrint();

        EdgeListWriter.write("0.peaf", peafTheory);

        AtomicInteger i = new AtomicInteger();
        LiExactPEAFInducer inducer = new LiExactPEAFInducer(peafTheory);

        inducer.induce(ind -> {
            int n = i.getAndIncrement();
            System.out.println(ind);
            EdgeListWriter.write(n + ".eaf", ind.toNewEAFTheory());

        });

    }
}
