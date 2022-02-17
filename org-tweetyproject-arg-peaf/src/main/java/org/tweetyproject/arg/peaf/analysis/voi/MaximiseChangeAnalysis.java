package org.tweetyproject.arg.peaf.analysis.voi;

import com.google.common.collect.Sets;
import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.peaf.analysis.AbstractAnalysis;
import org.tweetyproject.arg.peaf.analysis.AnalysisResult;
import org.tweetyproject.arg.peaf.analysis.AnalysisType;
import org.tweetyproject.arg.peaf.analysis.ProbabilisticJustificationAnalysis;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

public class MaximiseChangeAnalysis<T extends AbstractAnalysis & ProbabilisticJustificationAnalysis> extends VoIAbstractAnalysis<T> {
    /**
     * The default constructor
     *
     * @param peafTheory                         The PEAF Theory
     * @param extensionReasoner                  The extension reasoner
     * @param probabilisticJustificationAnalysis underlying analysis used for VoL
     */
    public MaximiseChangeAnalysis(PEAFTheory peafTheory, AbstractExtensionReasoner extensionReasoner, T probabilisticJustificationAnalysis) {
        super(peafTheory, extensionReasoner, AnalysisType.VOI_MAXIMISE_CHANGE, probabilisticJustificationAnalysis);
    }

    @Override
    public double computeUtility(EArgument e, PEAFTheory peafTheory, long[] iterations) {
        AnalysisResult result = probabilisticJustificationAnalysis.query(Sets.newHashSet(e));
        iterations[0] += result.getNoIterations();
        return result.getResult();
    }

    @Override
    public double computeDifference(double x, double y) {
        return Math.abs(x - y);
    }
}
