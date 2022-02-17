package org.tweetyproject.arg.peaf.analysis.voi;

import com.google.common.collect.Sets;
import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.peaf.analysis.AbstractAnalysis;
import org.tweetyproject.arg.peaf.analysis.AnalysisResult;
import org.tweetyproject.arg.peaf.analysis.AnalysisType;
import org.tweetyproject.arg.peaf.analysis.ProbabilisticJustificationAnalysis;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

public class MinimiseEntropyAnalysis<T extends AbstractAnalysis & ProbabilisticJustificationAnalysis> extends VoIAbstractAnalysis<T> {
    /**
     * The default constructor
     *
     * @param peafTheory                         The PEAF Theory
     * @param extensionReasoner                  The extension reasoner
     * @param probabilisticJustificationAnalysis underlying analysis used for VoL
     */
    public MinimiseEntropyAnalysis(PEAFTheory peafTheory, AbstractExtensionReasoner extensionReasoner, T probabilisticJustificationAnalysis) {
        super(peafTheory, extensionReasoner, AnalysisType.VOI_MINIMISE_ENTROPY, probabilisticJustificationAnalysis);
    }

    @Override
    public double computeUtility(EArgument e, PEAFTheory peafTheory, long[] iterations) {
        AnalysisResult result = probabilisticJustificationAnalysis.query(Sets.newHashSet(e));
        iterations[0] += result.getNoIterations();
        double p = result.getResult();

        return p * Math.log(p) + (1.0 - p) * Math.log(1.0 - p);
    }

    @Override
    public double computeDifference(double x, double y) {
        return x - y;
    }
}
