package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.peaf.inducers.ExactPEAFInducer;
import org.tweetyproject.arg.peaf.syntax.InducibleEAF;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.function.Consumer;

public class EasyPEAFExample {
    public static void main(String[] args) {
        PEAFTheory peafTheory = new PEAFTheory(2);


        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(new int[]{0}, new int[]{1}, 0.3);


        peafTheory.prettyPrint();
        ExactPEAFInducer inducer = new ExactPEAFInducer(peafTheory);

        inducer.induce((Consumer<InducibleEAF>) ind -> {
            System.out.println(ind);


        });
    }
}
