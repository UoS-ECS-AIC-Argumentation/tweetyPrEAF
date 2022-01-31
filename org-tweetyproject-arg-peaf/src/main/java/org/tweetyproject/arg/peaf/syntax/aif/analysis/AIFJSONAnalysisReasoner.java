package org.tweetyproject.arg.peaf.syntax.aif.analysis;

/**
 * AIFJSONAnalysisReasoner is utility class used with Google's GSON
 * <p>
 * The corresponding JSON in the aif is:
 * ```
 * {
 * "type": "approx",
 * "parameters": {"noThreads": "4", "errorLevel": "0.1"}
 * }
 * ```
 * The query is used for all reasoners except `preferred`.
 *
 * @author Taha Dogan Gunes
 */
public class AIFJSONAnalysisReasoner {
    /**
     * The type of the reasoner
     */
    public String type;
    /**
     * The parameters of the reasoner
     */
    public AIFJSONAnalysisReasonerParameters parameters;
}
