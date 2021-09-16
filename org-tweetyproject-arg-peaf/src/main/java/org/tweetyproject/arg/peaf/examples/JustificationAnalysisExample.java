package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.dung.reasoner.SimplePreferredReasoner;
import org.tweetyproject.arg.peaf.analysis.JustificationAnalysis;
import org.tweetyproject.arg.peaf.inducers.AllPEAFInducer;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JustificationAnalysisExample {
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
        peafTheory.addAttack(new int[]{5}, new int[]{2}, 1.0);
        peafTheory.addAttack(new int[]{5}, new int[]{1}, 1.0);
        peafTheory.addAttack(new int[]{1}, new int[]{5}, 1.0);
        peafTheory.addAttack(new int[]{1}, new int[]{6}, 1.0);

        List<EArgument> args = peafTheory.getArguments();
//        args.get(0).setName("eta");
//        args.get(1).setName("b");
//        args.get(2).setName("d");
//        args.get(3).setName("e");
//        args.get(4).setName("f");
//        args.get(5).setName("a");
//
//
//        args.get(6).setName("c");

        Set<EArgument> query = new HashSet<>();
        query.add(args.get(0));
        query.add(args.get(1));
//        query.add(args.get(6));

        AllPEAFInducer inducer = new AllPEAFInducer(peafTheory);
        double probabilisticJustification = JustificationAnalysis.compute(query, inducer, new PreferredReasoner());
        System.out.println("Probabilistic justification: " + probabilisticJustification);


    }

}
