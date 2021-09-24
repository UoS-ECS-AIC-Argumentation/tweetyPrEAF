package org.tweetyproject.arg.peaf.analysis;

import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.peaf.inducers.ApproxPEAFInducer;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.Set;


public class ApproxAnalysis<T extends AbstractExtensionReasoner> extends AbstractAnalysis <T>  {

    private final double errorLevel;

    public ApproxAnalysis(PEAFTheory peafTheory, T extensionReasoner, double errorLevel) {
        super(peafTheory, extensionReasoner);
        this.errorLevel = errorLevel;
    }

    @Override
    public AnalysisResult query(Set<EArgument> args) {
        final double[] M = {0.0};
        final double[] N = {0.0};
        final double[] metric = {0.0};
        final double[] p_i = {0.0};
        final long[] i = {0};

        do {
            ApproxPEAFInducer approxPEAFInducer = new ApproxPEAFInducer(this.peafTheory);
            approxPEAFInducer.induce(iEAF -> {
                double contribution = computeContributionOfAniEAF(args, iEAF);
                M[0] = M[0] + contribution;
                N[0] = N[0] + 1.0;
                i[0] += 1;
                p_i[0] = (M[0] + 2) / (N[0] + 4);
                metric[0] = ((4.0 * p_i[0] * (1.0 - p_i[0])) / Math.pow(errorLevel, 2)) - 4.0;
            });
        } while (N[0] <= metric[0]);


        return new AnalysisResult(M[0] / N[0], i[0]);
    }
}
