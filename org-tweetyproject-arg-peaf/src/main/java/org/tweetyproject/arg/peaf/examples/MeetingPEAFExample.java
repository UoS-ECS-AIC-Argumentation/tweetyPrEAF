package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.peaf.inducers.AllPEAFInducer;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.InducibleEAF;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.arg.peaf.writer.EdgeListWriter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class MeetingPEAFExample {
    static double total = 0;

    public static void main(String[] s) {
        int numOfArgs = 7;


        PEAFTheory peafTheory = new PEAFTheory();

        for (int i = 0; i < numOfArgs; i++) {
            peafTheory.addArgument(i);
        }

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(new int[]{0}, new int[]{2}, 0.6);
        peafTheory.addSupport(new int[]{0}, new int[]{1}, 0.7);
        peafTheory.addSupport(new int[]{0}, new int[]{3}, 0.9);
        peafTheory.addSupport(new int[]{0}, new int[]{4}, 0.3);
        peafTheory.addSupport(new int[]{3}, new int[]{5}, 0.5);
        peafTheory.addSupport(new int[]{3,4}, new int[]{6}, 0.9);

        peafTheory.addAttack(new int[]{5}, new int[]{2}, 1.0);
        peafTheory.addAttack(new int[]{5}, new int[]{1}, 1.0);
        peafTheory.addAttack(new int[]{1}, new int[]{5}, 1.0);
        peafTheory.addAttack(new int[]{1}, new int[]{6}, 1.0);

        peafTheory.prettyPrint();

        List<EArgument> args = peafTheory.getArguments();
        args.get(0).setName("eta");
        args.get(1).setName("b");
        args.get(2).setName("d");
        args.get(3).setName("e");
        args.get(4).setName("f");
        args.get(5).setName("a");
        args.get(6).setName("c");

        EdgeListWriter.write("/Users/tdgunes/Projects/DrawPrEAF/input/0.peaf", peafTheory);

        AtomicInteger i = new AtomicInteger();
        AllPEAFInducer inducer = new AllPEAFInducer(peafTheory);
        total = 0;
        inducer.induce((Consumer<InducibleEAF>) ind -> {
            int n = i.getAndIncrement();
            System.out.println(ind);
            String probability = String.format("%.04f", ind.getInducePro());
            probability = probability.replace(".", "_");
            EdgeListWriter.write("/Users/tdgunes/Projects/DrawPrEAF/input/"+ n + "" + "_" + probability + ".eaf", ind.toNewEAFTheory());
            total += ind.getInducePro();
        });
        System.out.println("Total probability: " + total);

    }
}
