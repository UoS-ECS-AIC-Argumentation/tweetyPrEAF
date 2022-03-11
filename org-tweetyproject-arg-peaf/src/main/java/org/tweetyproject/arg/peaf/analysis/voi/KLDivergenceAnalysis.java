package org.tweetyproject.arg.peaf.analysis.voi;

import com.google.common.collect.Sets;
import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.peaf.analysis.AbstractAnalysis;
import org.tweetyproject.arg.peaf.analysis.AnalysisResult;
import org.tweetyproject.arg.peaf.analysis.AnalysisType;
import org.tweetyproject.arg.peaf.analysis.ProbabilisticJustificationAnalysis;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.NamedPEAFTheory;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.Set;

public class KLDivergenceAnalysis<T extends AbstractAnalysis & ProbabilisticJustificationAnalysis> extends VOIAbstractAnalysis<T> {
    /**
     * The default constructor
     *
     * @param peafTheory                         The PEAF Theory
     * @param extensionReasoner                  The extension reasoner
     * @param probabilisticJustificationAnalysis underlying analysis used for VoL
     */
    public KLDivergenceAnalysis(NamedPEAFTheory peafTheory, AbstractExtensionReasoner extensionReasoner, T probabilisticJustificationAnalysis, Set<EArgument> objective) {
        super(peafTheory, extensionReasoner, AnalysisType.VOI_MAXIMISE_CHANGE, probabilisticJustificationAnalysis, objective);
    }

    @Override
    public double computeUtility(EArgument e, PEAFTheory peafTheory, long[] iterations) {
        probabilisticJustificationAnalysis.setPEAFTheory(peafTheory);
        AnalysisResult result = probabilisticJustificationAnalysis.query(Sets.newHashSet(e));
        iterations[0] += result.getNoIterations();
        return result.getResult();
    }

    @Override
    public double computeDifference(double x, double y) {
        return x * Math.log(x / y) + (1.0 - x) * Math.log((1.0 - x) / (1.0 - y));
    }
}
