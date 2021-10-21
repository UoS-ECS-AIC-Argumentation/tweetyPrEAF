package org.tweetyproject.arg.peaf.analysis;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.io.preaf.EdgeListReader;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.commons.util.Pair;

import java.io.IOException;
import java.util.Set;

public class LiExactAnalysisTests {
    @Test
    public void computeWatts5_1() throws IOException {
        System.out.println("\nReading with EdgeListReader:");
        Pair<PEAFTheory, Set<EArgument>> pair = EdgeListReader.readPEAFWithQuery("./src/main/resources/watts_5_1.peaf", true);

        PEAFTheory peafTheory = pair.getFirst();
        Set<EArgument> query = pair.getSecond();

        LiExactAnalysis liExactAnalysis = new LiExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = liExactAnalysis.query(query);
        double p = result.getProbability();
        result.print();

        // Type: li_exact prob: 0.04749305682280337 iterations: 48
        Assert.assertEquals("Attacked arguments #2 #4 are queried.", 0.047, p, 0.01);
    }

    @Test
    public void computeWatts5_2() throws IOException {
        System.out.println("\nReading with EdgeListReader:");
        Pair<PEAFTheory, Set<EArgument>> pair = EdgeListReader.readPEAFWithQuery("./src/main/resources/watts_5_2.peaf", true);

        PEAFTheory peafTheory = pair.getFirst();
        Set<EArgument> query = pair.getSecond();

        LiExactAnalysis liExactAnalysis = new LiExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = liExactAnalysis.query(query);
        double p = result.getProbability();
        result.print();

        Assert.assertEquals("Attacked arguments #2 #3 #4 are queried.", 0.121, p, 0.01);
    }

    @Test
    public void computeRandom_3_1() throws IOException {
        System.out.println("\nReading with EdgeListReader:");
        Pair<PEAFTheory, Set<EArgument>> pair = EdgeListReader.readPEAFWithQuery("./src/main/resources/random_3_1.peaf", true);

        PEAFTheory peafTheory = pair.getFirst();
        Set<EArgument> query = pair.getSecond();

        LiExactAnalysis liExactAnalysis = new LiExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = liExactAnalysis.query(query);
        double p = result.getProbability();
        result.print();

        Assert.assertEquals("Attacked arguments #1 #3 are queried.", 0.447, p, 0.01);
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
        LiExactAnalysis exactAnalysis = new LiExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = exactAnalysis.query(query);
        double p = result.getProbability();

        exactAnalysis.saveEAFs();
        result.print();

        Assert.assertEquals("Attacked argument #2 is queried.", 1.0, p, 0.01);
    }

}
