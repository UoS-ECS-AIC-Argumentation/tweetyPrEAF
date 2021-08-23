package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.peaf.inducers.SomePEAFInducer;
import org.tweetyproject.arg.peaf.syntax.InducibleEAF;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.arg.peaf.writer.EdgeListWriter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SomePEAFInducerExample {
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

        peafTheory.addAttack(new int[]{5}, new int[]{4}, 0.5);
        peafTheory.addAttack(new int[]{2}, new int[]{6}, 0.4);

        peafTheory.prettyPrint();

        EdgeListWriter.write("peaf.networkx", peafTheory);

        AtomicInteger i = new AtomicInteger();


         Consumer<InducibleEAF> consumer = new Consumer<InducibleEAF>() {
             @Override
             public void accept(InducibleEAF inducibleEAF) {
                 int n = i.getAndIncrement();
                 System.out.println(inducibleEAF);
             }
         };

        System.out.println("Simulate:");
        for (int j = 0; j < 100; j++) {
            SomePEAFInducer inducer = new SomePEAFInducer(peafTheory);
            System.out.println("Iteration number (" + j + ")");
            inducer.induce(consumer);
        }


    }
}
