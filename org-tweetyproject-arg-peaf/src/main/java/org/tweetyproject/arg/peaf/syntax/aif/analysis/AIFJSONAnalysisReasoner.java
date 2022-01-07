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
public class AIFJSONAnalysisReasoner {
    public final String type;
    public final AIFJSONAnalysisReasonerParameters parameters;

    public AIFJSONAnalysisReasoner(String type, AIFJSONAnalysisReasonerParameters parameters) {
        this.type = type;
        this.parameters = parameters;
    }
}
