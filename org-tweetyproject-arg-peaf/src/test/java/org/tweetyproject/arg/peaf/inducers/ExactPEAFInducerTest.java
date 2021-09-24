package org.tweetyproject.arg.peaf.inducers;

import org.junit.Assert;
import org.junit.Test;
import org.tweetyproject.arg.peaf.syntax.CyclicException;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

public class ExactPEAFInducerTest {
    @Test
    public void testSmallTree() {
        //        ┌─┐
        //        │0│
        //        └┬┘
        //        ┌▽───┐
        //        │1   │
        //        └┬──┬┘
        //        ┌▽┐┌▽┐
        //        │2││3│
        //        └─┘└─┘

        PEAFTheory peafTheory = new PEAFTheory(8);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(1, 2, 0.8);
        peafTheory.addSupport(1, 3, 0.6);

        peafTheory.addAttack(2, 4);
        peafTheory.addAttack(4, 5);
        peafTheory.addAttack(3, 6);
        peafTheory.addAttack(3, 7);


        peafTheory.printASCIITree();


        final double[] liTotalP = new double[]{0.0};
        System.out.println("LiExactPEAFInducer:");
        LiExactPEAFInducer inducer = new LiExactPEAFInducer(peafTheory);
        inducer.induce(inducibleEAF -> {
            System.out.println(inducibleEAF);
            liTotalP[0] += inducibleEAF.inducePro;
        });

        final double[] exactTotalP = new double[]{0.0};
        System.out.println();
        System.out.println("ExaactPEAFInducer:");
        ExactPEAFInducer inducer3 = new ExactPEAFInducer(peafTheory);
        inducer3.induce(inducibleEAF -> {
            System.out.println(inducibleEAF);
            exactTotalP[0] += inducibleEAF.inducePro;
        });

        Assert.assertEquals("Checks if Li's Method vs Exact Method.", liTotalP[0], exactTotalP[0], 0.01);
    }

    @Test
    public void testTwoAndTreeCombined() {
        PEAFTheory peafTheory = new PEAFTheory(4);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(new int[]{1}, new int[]{2,3}, 0.6);


        peafTheory.printASCIITree();


        final double[] liTotalP = new double[]{0.0};
        System.out.println("LiExactPEAFInducer:");
        LiExactPEAFInducer inducer = new LiExactPEAFInducer(peafTheory);
        inducer.induce(inducibleEAF -> {
            System.out.println(inducibleEAF);
            liTotalP[0] += inducibleEAF.inducePro;
        });

        final double[] exactTotalP = new double[]{0.0};
        System.out.println();
        System.out.println("ExaactPEAFInducer:");
        ExactPEAFInducer inducer3 = new ExactPEAFInducer(peafTheory);
        inducer3.induce(inducibleEAF -> {
            System.out.println(inducibleEAF);
            exactTotalP[0] += inducibleEAF.inducePro;
        });

        Assert.assertEquals("Checks if Li's Method vs Exact Method.", liTotalP[0], exactTotalP[0], 0.01);

    }
}
