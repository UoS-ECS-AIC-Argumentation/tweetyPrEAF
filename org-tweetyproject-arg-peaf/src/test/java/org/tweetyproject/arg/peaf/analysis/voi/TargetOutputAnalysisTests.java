package org.tweetyproject.arg.peaf.analysis.voi;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.tweetyproject.arg.peaf.analysis.AnalysisResult;
import org.tweetyproject.arg.peaf.analysis.ApproxAnalysis;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.NamedPEAFTheory;

import java.util.List;
import java.util.Set;

public class TargetOutputAnalysisTests {

    /**
     *
     */
    @Test
    public void testExample2() {
        NamedPEAFTheory peafTheory = new NamedPEAFTheory();

        List<EArgument> list = Lists.newArrayList();
        EArgument eta = peafTheory.addArgument(0, "eta", "0");
        list.add(eta);

        EArgument eArgument;
        eArgument = peafTheory.addArgument(1, "I trust John.", "1");
        list.add(eArgument);
        peafTheory.addSupport(0, 1, 0.15);

        eArgument = peafTheory.addArgument(2, "I don't trust John.", "2");
        list.add(eArgument);
        peafTheory.addSupport(0, 2, 0.9);

        eArgument = peafTheory.addArgument(3, "John thinks there it will rain tomorrow.", "3");
        list.add(eArgument);
        peafTheory.addSupport(0, 3, 0.15);

        eArgument = peafTheory.addArgument(4, "John thinks rains are becoming rare.", "4");
        list.add(eArgument);
        peafTheory.addSupport(0, 4, 0.5);


        peafTheory.addAttack(1, 2);
        peafTheory.addAttack(2, 3);
        peafTheory.addAttack(2, 4);
        peafTheory.addAttack(4, 3);

        // 1 -> 2 -> 3 <- |
        //      | -> 4 -> |

        peafTheory.prettyPrint();

        for (EArgument argument : list) {
            ApproxAnalysis approxAnalysis = new ApproxAnalysis(peafTheory, new PreferredReasoner(), 0.01);
            AnalysisResult result = approxAnalysis.query(Sets.newHashSet(argument));
            System.out.println(argument + ": " + result.getResult());
        }

        Set<EArgument> objective = Sets.newHashSet(list.get(4), list.get(1), list.get(2), list.get(3));

        System.out.println("Objective is: " + objective);
        System.out.println("Target is: " + list.get(2));

        System.out.println("TargetOutputAnalysis: ");
        TargetOutputAnalysis<ApproxAnalysis> analysis = new TargetOutputAnalysis<>(peafTheory,
                new PreferredReasoner(),
                Sets.newHashSet(list.get(3)),
                new ApproxAnalysis(peafTheory,
                        new PreferredReasoner(),
                        0.01));

        for (EArgument argument : list) {
            AnalysisResult result = analysis.query(Sets.newHashSet(argument));
            System.out.println("V(" + argument + "): " + result.getResult());
        }

        System.out.println("MinimiseEntropyAnalysis: ");
        MinimiseEntropyAnalysis<ApproxAnalysis> analysis2 = new MinimiseEntropyAnalysis<>(peafTheory,
                new PreferredReasoner(),
                new ApproxAnalysis(peafTheory,
                        new PreferredReasoner(),
                        0.01));

        for (EArgument argument : list) {
            AnalysisResult result = analysis2.query(Sets.newHashSet(argument));
            System.out.println("V(" + argument + "): " + result.getResult());
        }

        System.out.println("MaximiseChangeAnalysis: ");
        MaximiseChangeAnalysis<ApproxAnalysis> analysis3 = new MaximiseChangeAnalysis<>(peafTheory,
                new PreferredReasoner(),
                new ApproxAnalysis(peafTheory,
                        new PreferredReasoner(),
                        0.01));

        for (EArgument argument : list) {
            AnalysisResult result = analysis3.query(Sets.newHashSet(argument));
            System.out.println("V(" + argument + "): " + result.getResult());
        }


        System.out.println("KLDivergenceAnalysis: ");
        KLDivergenceAnalysis<ApproxAnalysis> analysis4 = new KLDivergenceAnalysis<>(peafTheory,
                new PreferredReasoner(),
                new ApproxAnalysis(peafTheory,
                        new PreferredReasoner(),
                        0.01));

        for (EArgument argument : list) {
            AnalysisResult result = analysis4.query(Sets.newHashSet(argument));
            System.out.println("V(" + argument + "): " + result.getResult());
        }
    }
}
