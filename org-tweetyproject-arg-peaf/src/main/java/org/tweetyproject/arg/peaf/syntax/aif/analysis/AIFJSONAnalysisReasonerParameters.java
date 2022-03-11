package org.tweetyproject.arg.peaf.syntax.aif.analysis;

/**
 * AIFJSONAnalysisReasonerParameters is utility class used with Google's GSON
 * <p>
 * The corresponding JSON in the aif is:
 * ```
 * {
 * "noThreads": "4",
 * "errorLevel": "0.1"
 * }
 * ```
 *
 * @author Taha Dogan Gunes
 */
public class AIFJSONAnalysisReasonerParameters {
    /**
     * This must be larger than 1 and typically lower or equal to the maximum available logical cores.
     */
    public int noThreads;

    /**
     * This is used for the accuracy of the approximate reasoners (i.e. approx and con_approx)
     */
    public double errorLevel;

    /**
     * The list for target arguments
     */
    public String[] target;

    /**
     * Semantics can be "preferred" or "grounded"
     */
    public String semantics;

    /**
     * Objectives for value of information
     */
    public String[] objective;
}
