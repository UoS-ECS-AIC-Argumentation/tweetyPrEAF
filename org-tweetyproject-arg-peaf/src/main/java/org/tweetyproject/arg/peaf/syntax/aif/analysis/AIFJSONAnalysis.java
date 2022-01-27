package org.tweetyproject.arg.peaf.syntax.aif.analysis;

/**
 * AIFJSONAnalysis is utility class used with Google's GSON
 *
 * The corresponding JSON in the aif is:
 * ```
 * {
 *     "query" : ["6f491666-6029-45ae-94b6-848094c81ea3"],
 *     "result" : {"datetime" : "",  "outcome": ""},
 *     "reasoner" : {"type": "approx",
 *              "parameters": {
 *              "noThreads": "4",
 *              "errorLevel": "0.1"
 *              }
 *     }
 *  }
 * ```
 *  The query is used for all reasoners except `preferred`.
 *
 * @author Taha Dogan Gunes
 */

public class AIFJSONAnalysis {
    /**
     * The list of queried arguments
     */
    public String[] query;

    /**
     * The result object (will contain the result after getting processed by the reasoner).
     */
    public AIFJSONAnalysisResult result;

    /**
     * The configuration of the selected reasoner that will process the aif file.
     */
    public AIFJSONAnalysisReasoner reasoner;
}


