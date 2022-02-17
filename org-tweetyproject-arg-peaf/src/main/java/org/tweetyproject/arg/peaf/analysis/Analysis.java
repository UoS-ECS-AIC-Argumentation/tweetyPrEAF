package org.tweetyproject.arg.peaf.analysis;

import org.tweetyproject.arg.peaf.syntax.EArgument;

import java.util.Set;

/**
 * The interface of the analyses in arg.peaf
 *
 * @author Taha Dogan Gunes
 */
public interface Analysis {
    /**
     * Creates an AnalysisResult to store the results of the analysis
     *
     * @param args the set of arguments necessary for the query
     * @return an AnalysisResult object
     */
    AnalysisResult query(Set<EArgument> args);
}
