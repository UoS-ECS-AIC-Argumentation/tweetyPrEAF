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

public class TargetOutputAnalysis<T extends AbstractAnalysis & ProbabilisticJustificationAnalysis> extends VOIAbstractAnalysis<T> {

    private final Set<EArgument> targetOutput;

    /**
     * The default constructor
     *
     * @param peafTheory                         The PEAF Theory
     * @param extensionReasoner                  The extension reasoner
     * @param targetOutput                       the objective O'
     * @param probabilisticJustificationAnalysis underlying analysis used for VoL
     */
    public TargetOutputAnalysis(NamedPEAFTheory peafTheory,
                                AbstractExtensionReasoner extensionReasoner,
                                Set<EArgument> targetOutput,
                                T probabilisticJustificationAnalysis,
                                Set<EArgument> objective) {
        super(peafTheory, extensionReasoner, AnalysisType.VOI_TARGET_OUTPUT, probabilisticJustificationAnalysis, objective);

        this.targetOutput = targetOutput;
    }

    @Override
    public double computeUtility(EArgument e, PEAFTheory peafTheory, long[] iterations) {
        probabilisticJustificationAnalysis.setPEAFTheory(peafTheory);

        // args is O'
        AnalysisResult result = probabilisticJustificationAnalysis.query(Sets.newHashSet(e));
        iterations[0] += result.getNoIterations();
        double p = result.getResult();

        if (targetOutput.contains(e)) {
            return p;
        } else {
            return 1.0 - p;
        }
    }

    @Override
    public double computeDifference(double x, double y) {
        return x - y;
    }
}
