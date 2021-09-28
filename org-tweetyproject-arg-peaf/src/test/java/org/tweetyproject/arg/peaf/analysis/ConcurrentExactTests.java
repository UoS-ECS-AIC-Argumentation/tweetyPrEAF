package org.tweetyproject.arg.peaf.analysis;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.io.EdgeListReader;
import org.tweetyproject.arg.peaf.syntax.CyclicException;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.commons.util.Pair;

import java.io.IOException;
import java.util.Set;

public class ConcurrentExactTests {
    @Test
    public void computeSimple() {
        PEAFTheory peafTheory = new PEAFTheory(3);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(1, 2, 0.8);

        EArgument a1 = peafTheory.getArguments().get(1);
        Set<EArgument> query = Sets.newHashSet(a1);

        ConcurrentExactAnalysis exactAnalysis = new ConcurrentExactAnalysis(peafTheory, new PreferredReasoner());
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

        ConcurrentExactAnalysis exactAnalysis = new ConcurrentExactAnalysis(peafTheory, new PreferredReasoner());
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

        ConcurrentExactAnalysis exactAnalysis = new ConcurrentExactAnalysis(peafTheory, new PreferredReasoner());
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

        ConcurrentExactAnalysis exactAnalysis = new ConcurrentExactAnalysis(peafTheory, new PreferredReasoner());
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

        ConcurrentExactAnalysis exactAnalysis = new ConcurrentExactAnalysis(peafTheory, new PreferredReasoner());
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

        ConcurrentExactAnalysis exactAnalysis = new ConcurrentExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
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

        ConcurrentExactAnalysis exactAnalysis = new ConcurrentExactAnalysis(peafTheory, new PreferredReasoner());
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

        ConcurrentExactAnalysis exactAnalysis = new ConcurrentExactAnalysis(peafTheory, new PreferredReasoner());
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
        ConcurrentExactAnalysis exactAnalysis = new ConcurrentExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();


        Assert.assertEquals("Attacked argument #2 is queried.", 1.0, p, 0.01);
    }

    @Test
    public void computeWatts5_2() throws IOException {
        System.out.println("\nReading with EdgeListReader:");
        Pair<PEAFTheory, Set<EArgument>> pair = EdgeListReader.readPEAFWithQuery("./src/main/resources/watts_5_2.peaf", true);

        PEAFTheory peafTheory = pair.getFirst();
        Set<EArgument> query = pair.getSecond();

        ConcurrentExactAnalysis exactAnalysis = new ConcurrentExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();
        result.print();

        Assert.assertEquals("Attacked arguments #2 #3 #4 are queried.", 0.179, p, 0.01);
    }


}