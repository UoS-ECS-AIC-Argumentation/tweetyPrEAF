package org.tweetyproject.arg.peaf.analysis;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.io.preaf.EdgeListReader;
import org.tweetyproject.arg.peaf.syntax.CyclicException;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.commons.util.Pair;

import java.io.IOException;
import java.util.Set;

public class ExactAnalysisTests {

    @Test
    public void computeSimple() {
        PEAFTheory peafTheory = new PEAFTheory(3);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(1, 2, 0.8);

        EArgument a1 = peafTheory.getArguments().get(1);
        Set<EArgument> query = Sets.newHashSet(a1);

        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();


        Assert.assertEquals("Argument #1 is queried.", 0.9, p, 0.001);
    }


    @Test
    public void computeSimpleAttack() {
        PEAFTheory peafTheory = new PEAFTheory(3);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 2, 0.9);
        peafTheory.addAttack(2, 1);

        EArgument a = peafTheory.getArguments().get(2);
        Set<EArgument> query = Sets.newHashSet(a);

        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();


        peafTheory.prettyPrint();

        Assert.assertEquals("Argument #2 is queried.", 0.9, p, 0.001);
    }

    @Test
    public void computeSimpleAttack2() {
        PEAFTheory peafTheory = new PEAFTheory(3);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addAttack(1, 2);

        EArgument a = peafTheory.getArguments().get(2);
        Set<EArgument> query = Sets.newHashSet(a);

        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();


        Assert.assertEquals("Attacked argument #2 is queried.", 0, p, 0.0001);
    }

    @Test
    public void computeSimpleTree() {
        PEAFTheory peafTheory = new PEAFTheory(4);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(1, 2, 0.9);
        peafTheory.addSupport(1, 3, 0.9);
        peafTheory.addAttack(2, 3);

        EArgument a = peafTheory.getArguments().get(3);
        Set<EArgument> query = Sets.newHashSet(a);

        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();


        Assert.assertEquals("Attacked argument #3 is queried.", 0.080, p, 0.01);
    }

    @Test
    public void computeSimpleTree2() {
        PEAFTheory peafTheory = new PEAFTheory(4);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(1, 2, 0.9);
        peafTheory.addSupport(1, 3, 0.9);
        peafTheory.addAttack(2, 3);

        EArgument a = peafTheory.getArguments().get(2);
        Set<EArgument> query = Sets.newHashSet(a);

        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();


        Assert.assertEquals("Attacked argument #2 is queried.", 0.81, p, 0.01);
    }

    @Test
    public void computeSimpleTreeCycle() {
        PEAFTheory peafTheory = new PEAFTheory(4);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(1, 2, 0.9);
        peafTheory.addSupport(1, 3, 0.9);
        peafTheory.addAttack(2, 3);
        peafTheory.addAttack(3, 2);

        EArgument a = peafTheory.getArguments().get(2);
        Set<EArgument> query = Sets.newHashSet(a);

        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        result.print();
        double p = result.getProbability();



        Assert.assertEquals("Attacked argument #2 is queried.", 0.81, p, 0.01);
    }

    @Test
    public void computeSimpleTreeCycleMoreAttacks() {
        PEAFTheory peafTheory = new PEAFTheory(5);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(1, 2, 0.9);
        peafTheory.addSupport(1, 3, 0.9);
        peafTheory.addSupport(3, 4, 0.01);
        peafTheory.addAttack(3, 2);
        peafTheory.addAttack(2, 3);
        peafTheory.addAttack(4, 2);


        EArgument a = peafTheory.getArguments().get(4);
        Set<EArgument> query = Sets.newHashSet(a);

        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();


        Assert.assertEquals("Attacked argument #4 is queried.", 0.0081, p, 0.0001);
    }

    @Test(expected = CyclicException.class)
    public void computeSupportCycle() {
        PEAFTheory peafTheory = new PEAFTheory(3);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(1, 2, 0.9);
        peafTheory.addSupport(2, 1, 0.9);


        EArgument a = peafTheory.getArguments().get(2);
        Set<EArgument> query = Sets.newHashSet(a);

        System.out.println("PEAF:");
        peafTheory.prettyPrint();

        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();


        Assert.assertEquals("Attacked argument #2 is queried.", 0.81, p, 0.01);
    }


    @Test
    public void computeAttackSquare() {
        PEAFTheory peafTheory = new PEAFTheory(5);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 1.0);
        peafTheory.addSupport(0, 2, 1.0);
        peafTheory.addSupport(0, 3, 1.0);
        peafTheory.addSupport(0, 4, 1.0);

        peafTheory.addAttack(1, 2);
        peafTheory.addAttack(2, 3);
        peafTheory.addAttack(3, 4);
        peafTheory.addAttack(4, 1);

        EArgument a = peafTheory.getArguments().get(4);
        Set<EArgument> query = Sets.newHashSet(a);

        System.out.println("PEAF:");
        peafTheory.prettyPrint();
        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();


        Assert.assertEquals("Attacked argument #2 is queried.", 1.0, p, 0.01);
    }


    @Test
    public void computeWatts5_1() throws IOException {
        System.out.println("\nReading with EdgeListReader:");
        Pair<PEAFTheory, Set<EArgument>> pair = EdgeListReader.readPEAFWithQuery("./src/main/resources/watts_5_1.peaf", true);

        PEAFTheory peafTheory = pair.getFirst();
        Set<EArgument> query = pair.getSecond();

        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();
        result.print();

        Assert.assertEquals("Attacked arguments #2 #4 are queried.", 0.047, p, 0.01);
    }

    @Test
    public void computeWatts5_2() throws IOException {
        System.out.println("\nReading with EdgeListReader:");
        Pair<PEAFTheory, Set<EArgument>> pair = EdgeListReader.readPEAFWithQuery("./src/main/resources/watts_5_2.peaf", true);

        PEAFTheory peafTheory = pair.getFirst();
        Set<EArgument> query = pair.getSecond();

        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();
        result.print();


        Assert.assertEquals("Attacked arguments #2 #3 #4 are queried.", 0.074, p, 0.01);
    }

    @Test
    public void computeRandom_3_1() throws IOException {
        System.out.println("\nReading with EdgeListReader:");
        Pair<PEAFTheory, Set<EArgument>> pair = EdgeListReader.readPEAFWithQuery("./src/main/resources/random_3_1.peaf", false);

        PEAFTheory peafTheory = pair.getFirst();
        Set<EArgument> query = pair.getSecond();
        peafTheory.prettyPrint();
        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();
        result.print();

        exactAnalysis.saveEAFs();
        Assert.assertEquals("Attacked arguments #1 #3 are queried.", 0.34, p, 0.01);
    }

    @Test
    public void computeTree() {
        PEAFTheory peafTheory = new PEAFTheory(4);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 1.0);
        peafTheory.addSupport(1, 2, 1.0);
        peafTheory.addSupport(1, 3, 1.0);


        peafTheory.addAttack(2, 3);


        EArgument a = peafTheory.getArguments().get(2);
        Set<EArgument> query = Sets.newHashSet(a);

        System.out.println("PEAF:");
        peafTheory.prettyPrint();
        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();

        exactAnalysis.saveEAFs();
        result.print();

        Assert.assertEquals("Attacked argument #2 is queried.", 1.0, p, 0.01);
    }


    @Test
    public void computeTree2() {
        PEAFTheory peafTheory = new PEAFTheory(5);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 1.0);
        peafTheory.addSupport(0, 4, 1.0);
        peafTheory.addSupport(4, 1, 1.0);
        peafTheory.addSupport(4, 2, 1.0);

        peafTheory.addAttack(2, 3);
        peafTheory.addAttack(1, 2);

        EArgument a = peafTheory.getArguments().get(1);
        Set<EArgument> query = Sets.newHashSet(a);
        System.out.println("Query: " + query);

        System.out.println("PEAF:");
        peafTheory.prettyPrint();
        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();

        exactAnalysis.saveEAFs();
        result.print();

        Assert.assertEquals("Attacked argument #3 is queried.", 1.0, p, 0.01);
    }


    @Test
    public void testDuplication() {
        PEAFTheory peafTheory = new PEAFTheory(4);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 1.0);
        peafTheory.addSupport(0, 2, 1.0);
        peafTheory.addSupport(0, 3, 1.0);
        peafTheory.addSupport(1, 2, 1.0);
        peafTheory.addSupport(3, 1, 1.0);
        peafTheory.addSupport(3, 2, 1.0);

        //    0  --> 3
        //  /   \  /
        // 1 -> 2

        EArgument a = peafTheory.getArguments().get(1);
        Set<EArgument> query = Sets.newHashSet(a);
        System.out.println("Query: " + query);

        System.out.println("PEAF:");
        peafTheory.prettyPrint();
        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();

        exactAnalysis.saveEAFs();
        result.print();

        Assert.assertEquals("Attacked argument #3 is queried.", 1.0, p, 0.01);
    }

    @Test
    public void testTwoArgumentsSupportOneArgument() {
        PEAFTheory peafTheory = new PEAFTheory(4);

        //  0  --->  1, 2 ---> 3
        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(new int[]{0}, new int[]{1, 2}, 1.0);
        peafTheory.addSupport(new int[]{1, 2}, new int[]{3}, 1.0);

        EArgument a = peafTheory.getArguments().get(3);
        Set<EArgument> query = Sets.newHashSet(a);
        System.out.println("Query: " + query);

        System.out.println("PEAF:");
        peafTheory.prettyPrint();
        ExactAnalysis exactAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();

        exactAnalysis.saveEAFs();
        result.print();

//        Assert.assertEquals("Attacked argument #3 is queried.", 1.0, p, 0.01);
    }
}
