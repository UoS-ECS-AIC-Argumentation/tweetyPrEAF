package org.tweetyproject.arg.peaf.analysis.voi;

import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.peaf.analysis.AbstractAnalysis;
import org.tweetyproject.arg.peaf.analysis.AnalysisResult;
import org.tweetyproject.arg.peaf.analysis.AnalysisType;
import org.tweetyproject.arg.peaf.analysis.ProbabilisticJustificationAnalysis;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.Set;

/**
 * Abstract class that computes value of observed metric from Robinson (2021).
 * <p>
 * For more:
 * Robinson, T. (2021). Value of information for argumentation based intelligence analysis.
 */
public abstract class VoIAbstractAnalysis<T extends AbstractAnalysis & ProbabilisticJustificationAnalysis> extends AbstractAnalysis implements VOIAnalysis {

    protected final T probabilisticJustificationAnalysis;

    /**
     * The default constructor
     *
     * @param peafTheory                         The PEAF Theory
     * @param extensionReasoner                  The extension reasoner
     * @param analysisType                       The type of the analysis
     * @param probabilisticJustificationAnalysis underlying analysis used for VoL
     */
    public VoIAbstractAnalysis(PEAFTheory peafTheory,
                               AbstractExtensionReasoner extensionReasoner,
                               AnalysisType analysisType,
                               T probabilisticJustificationAnalysis) {
        super(peafTheory, extensionReasoner, analysisType);

        this.probabilisticJustificationAnalysis = probabilisticJustificationAnalysis;
    }

    /**
     * Generates "value of observed"
     * <p>
     * This metric is computed by the sum of difference between the utility of having the arguments in question
     * versus the utility of not having such arguments (their associated supports and attacks including froms and tos).
     *
     * @param args the set of arguments necessary for the query
     * @return an AnalysisResult object
     */
    @Override
    public AnalysisResult query(Set<EArgument> args) {
        // args is O in Definition 10 in the paper

        double valueOfInformation = 0;

        long[] noIterations = {0};

        // sum all differences on the objectives
        for (EArgument e : args) {
            PEAFTheory copy = this.peafTheory.createCopyWithoutArgument(e);

            double utility1 = this.computeUtility(e, peafTheory, noIterations);
            double utility2 = this.computeUtility(e, copy, noIterations);

            valueOfInformation += this.computeDifference(utility1, utility2);
        }

        return new AnalysisResult(valueOfInformation, noIterations[0], this.analysisType, 0);
    }
}
