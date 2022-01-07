package org.tweetyproject.arg.peaf.syntax.aif.analysis;

/*
{
          "query" : ["6f491666-6029-45ae-94b6-848094c81ea3"],
          "result" : {
          "datetime" : "",
          "outcome": ""
          },
          "reasoner" : {
          "type": "approx",
          "parameters": {
          "noThreads": "4",
          "errorLevel": "0.1"
          }
 */
public class AIFJSONAnalysis {
    public final String[] query;
    public final AIFJSONAnalysisResult result;
    public final AIFJSONAnalysisReasoner reasoner;

    public AIFJSONAnalysis(String[] query, AIFJSONAnalysisResult result, AIFJSONAnalysisReasoner reasoner) {
        this.query = query;
        this.result = result;
        this.reasoner = reasoner;
    }
}


