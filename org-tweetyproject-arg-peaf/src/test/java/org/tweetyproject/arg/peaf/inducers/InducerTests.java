package org.tweetyproject.arg.peaf.inducers;

import org.junit.Test;


import org.tweetyproject.arg.peaf.syntax.InducibleEAF;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.function.Consumer;

public class InducerTests {

    @Test
    public void testApproxPEAFInducer() {
        PEAFTheory peafTheory = new PEAFTheory(8);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(1, 2, 0.8);
        peafTheory.addSupport(1, 3, 0.6);

        peafTheory.addAttack(2, 4);
        peafTheory.addAttack(4, 5);
        peafTheory.addAttack(3, 6);
        peafTheory.addAttack(3, 7);


        peafTheory.prettyPrint();


        System.out.println("ExactPEAFInducer:");
        ExactPEAFInducer inducer = new ExactPEAFInducer(peafTheory);
        inducer.induce(new Consumer<InducibleEAF>() {
            @Override
            public void accept(InducibleEAF inducibleEAF) {
                System.out.println(inducibleEAF);

//                EAFTheory eafTheory = inducibleEAF.toNewEAFTheory();
//                eafTheory.prettyPrint();
            }
        });

        System.out.println("ExactPEAFInducer:");
        ApproxPEAFInducer inducer2 = new ApproxPEAFInducer(peafTheory);
        inducer2.induceNTimes(new Consumer<InducibleEAF>() {
            @Override
            public void accept(InducibleEAF inducibleEAF) {
                System.out.println(inducibleEAF);

            }
        }, 20);
    }


    @Test
    public void testExactPEAFInducer() {
        PEAFTheory peafTheory = new PEAFTheory(8);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(1, 2, 0.8);
        peafTheory.addSupport(1, 3, 0.6);

        peafTheory.addAttack(2, 4);
        peafTheory.addAttack(4, 5);
        peafTheory.addAttack(3, 6);
        peafTheory.addAttack(3, 7);


        peafTheory.prettyPrint();

        System.out.println("ExactPEAFInducer:");
        ExactPEAFInducer inducer = new ExactPEAFInducer(peafTheory);
        inducer.induce(new Consumer<InducibleEAF>() {
            @Override
            public void accept(InducibleEAF inducibleEAF) {
                System.out.println(inducibleEAF);

//                EAFTheory eafTheory = inducibleEAF.toNewEAFTheory();
//                eafTheory.prettyPrint();
            }
        });
    }


    @Test
    public void testSimple() {
        PEAFTheory peafTheory = new PEAFTheory(4);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(1, 2, 0.8);
        peafTheory.addSupport(1, 3, 0.6);

        peafTheory.addAttack(2, 3);

        peafTheory.prettyPrint();

        System.out.println("ExactPEAFInducer:");
        ExactPEAFInducer inducer = new ExactPEAFInducer(peafTheory);
        inducer.induce(new Consumer<InducibleEAF>() {
            @Override
            public void accept(InducibleEAF inducibleEAF) {
                System.out.println(inducibleEAF);
            }
        });

//        System.out.println();
//        System.out.println("SomePEAFInducer:");
//        SomePEAFInducer inducer1 = new SomePEAFInducer(peafTheory);
//        inducer1.induceNTimes(new Consumer<InducibleEAF>() {
//            @Override
//            public void accept(InducibleEAF inducibleEAF) {
//                System.out.println(inducibleEAF);
//            }
//        }, 30);


    }
}
