package org.tweetyproject.arg.peaf.analysis;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.commons.util.Pair;

import java.util.Set;

public class ApproxJustificationTests {

    @Test
    public void computeSimple() {
        PEAFTheory peafTheory = new PEAFTheory(3);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(1, 2, 0.8);

        EArgument a1 = peafTheory.getArguments().get(1);
        Set<EArgument> query = Sets.newHashSet(a1);

        Pair<Double, Double> p = OldJustificationAnalysis.computeApproxOf(query, peafTheory, new PreferredReasoner(), 0.005);
        System.out.println("Justification: " + p.getFirst());
        Assert.assertEquals("Argument #1 is queried.", 0.9, p.getFirst(), 0.005);
    }

    @Test
    public void computeAttack() {
        PEAFTheory peafTheory = new PEAFTheory(3);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 2, 0.9);
        peafTheory.addAttack(2, 1);

        EArgument a = peafTheory.getArguments().get(2);
        Set<EArgument> query = Sets.newHashSet(a);

        Pair<Double, Double> p = OldJustificationAnalysis.computeApproxOf(query, peafTheory, new PreferredReasoner(), 0.005);
        System.out.println("Justification: " + p.getFirst());
        Assert.assertEquals("Argument #1 is queried.", 0.9, p.getFirst(), 0.005);
    }

    @Test
    public void computeSimpleAttack2() {
        PEAFTheory peafTheory = new PEAFTheory(3);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addAttack(1, 2);

        EArgument a = peafTheory.getArguments().get(2);
        Set<EArgument> query = Sets.newHashSet(a);

        Pair<Double, Double> p = OldJustificationAnalysis.computeApproxOf(query, peafTheory, new PreferredReasoner(), 0.005);
        System.out.println("Justification: " + p.getFirst());
        Assert.assertEquals("Attacked argument #2 is queried.", 0, p.getFirst(), 0.0005);
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

        Pair<Double, Double> pair = OldJustificationAnalysis.computeApproxOf(query, peafTheory, new PreferredReasoner(), 0.005);
        double p = pair.getFirst();

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

        Pair<Double, Double> pair = OldJustificationAnalysis.computeApproxOf(query, peafTheory, new PreferredReasoner(), 0.005);
        double p = pair.getFirst();

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

        Pair<Double, Double> pair = OldJustificationAnalysis.computeApproxOf(query, peafTheory, new PreferredReasoner(), 0.005);
        double p = pair.getFirst();

        Assert.assertEquals("Attacked argument #2 is queried.", 0.891, p, 0.01);
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

        Pair<Double, Double> pair = OldJustificationAnalysis.computeApproxOf(query, peafTheory, new PreferredReasoner(), 0.001);
        double p = pair.getFirst();

        Assert.assertEquals("Attacked argument #4 is queried.", 0.0081, p, 0.001);
    }

    @Test
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
        Pair<Double, Double> pair = OldJustificationAnalysis.computeApproxOf(query, peafTheory, new PreferredReasoner(), 0.005);
        double p = pair.getFirst();

        Assert.assertEquals("Attacked argument #2 is queried.", 0.81, p, 0.01);
    }
}
