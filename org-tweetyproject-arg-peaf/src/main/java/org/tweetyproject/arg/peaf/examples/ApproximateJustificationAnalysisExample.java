package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.peaf.analysis.AnalysisResult;
import org.tweetyproject.arg.peaf.analysis.ApproxAnalysis;
import org.tweetyproject.arg.peaf.analysis.ExactAnalysis;
import org.tweetyproject.arg.peaf.inducers.ApproxPEAFInducer;
import org.tweetyproject.arg.peaf.inducers.LiExactPEAFInducer;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.commons.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApproximateJustificationAnalysisExample {
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
        peafTheory.addSupport(new int[]{3, 4}, new int[]{6}, 0.9);
        peafTheory.addAttack(new int[]{5}, new int[]{2});
        peafTheory.addAttack(new int[]{5}, new int[]{1});
        peafTheory.addAttack(new int[]{1}, new int[]{5});
        peafTheory.addAttack(new int[]{1}, new int[]{6});

        List<EArgument> args = peafTheory.getArguments();
        Set<EArgument> query = new HashSet<>();
        query.add(args.get(0));



        ExactAnalysis analysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
        AnalysisResult result = analysis.query(query);
        result.print();

        ApproxAnalysis analysis2 = new ApproxAnalysis(peafTheory, new PreferredReasoner(), 0.1);
        AnalysisResult result2 = analysis2.query(query);
        result2.print();

    }


}
