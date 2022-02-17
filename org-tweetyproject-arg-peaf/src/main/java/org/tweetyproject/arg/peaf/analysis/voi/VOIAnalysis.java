package org.tweetyproject.arg.peaf.analysis.voi;

import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

public interface VOIAnalysis {
    /**
     * U(e, PF)
     *
     * @return
     */
    double computeUtility(EArgument e, PEAFTheory peafTheory, long[] iterations);

    /**
     * d(x, y)
     *
     * @return the difference between x and y
     */
    double computeDifference(double x, double y);
}
