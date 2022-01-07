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

public class AIFJSONAnalysisReasonerParameters {
    public final int noThreads;
    public final double errorLevel;

    public AIFJSONAnalysisReasonerParameters(int noThreads, double errorLevel) {
        this.noThreads = noThreads;
        this.errorLevel = errorLevel;
    }
}
