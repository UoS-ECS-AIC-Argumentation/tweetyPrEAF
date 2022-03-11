package org.tweetyproject.arg.peaf.analysis.voi;

import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.peaf.analysis.AbstractAnalysis;
import org.tweetyproject.arg.peaf.analysis.AnalysisResult;
import org.tweetyproject.arg.peaf.analysis.AnalysisType;
import org.tweetyproject.arg.peaf.analysis.ProbabilisticJustificationAnalysis;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.NamedPEAFTheory;

import java.util.Set;

/**
 * Abstract class that computes value of observed metric from Robinson (2021).
 * <p>
 * For more:
 * Robinson, T. (2021). Value of information for argumentation based intelligence analysis.
 */
public abstract class VOIAbstractAnalysis<T extends AbstractAnalysis & ProbabilisticJustificationAnalysis> extends AbstractAnalysis implements VOIAnalysis {

    protected final T probabilisticJustificationAnalysis;
    private final Set<EArgument> objective;

    /**
     * The default constructor
     *
     * @param peafTheory                         The PEAF Theory
     * @param extensionReasoner                  The extension reasoner
     * @param analysisType                       The type of the analysis
     * @param probabilisticJustificationAnalysis underlying analysis used for VoL
     */
    public VOIAbstractAnalysis(NamedPEAFTheory peafTheory,
                               AbstractExtensionReasoner extensionReasoner,
                               AnalysisType analysisType,
                               T probabilisticJustificationAnalysis,
                               Set<EArgument> objective) {
        super(peafTheory, extensionReasoner, analysisType);

        this.probabilisticJustificationAnalysis = probabilisticJustificationAnalysis;
        this.objective = objective;
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
        for (EArgument e : objective) {
            NamedPEAFTheory original = (NamedPEAFTheory) this.peafTheory;


            NamedPEAFTheory copy = original.createCopyWithoutArguments(args);
            EArgument e_copy = copy.getArgumentByIdentifier(original.getIdentifier(e));
//            System.out.println();
//            System.out.println("JUSTIFICATION ANALYSIS: " + copy.getNameOfArgument(e_copy));
//            for (EArgument arg : args) {
//                System.out.println("VALUE OF INFORMATION: "+ original.getNameOfArgument(arg));
//            }
//            System.out.println();

//            copy.prettyPrint();


            double utility1 = this.computeUtility(e, peafTheory, noIterations);
//            System.out.println("utility1: " + utility1);
            double utility2 = this.computeUtility(e_copy, copy, noIterations);
//            System.out.println("utility2: " + utility2);

            double v = this.computeDifference(utility1, utility2);
//            System.out.println("v: " + v);
            valueOfInformation += v;
        }

        return new AnalysisResult(valueOfInformation, noIterations[0], this.analysisType, 0);
    }
}
