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
public class AIFJSONAnalysisResult {
    public String datetime;
    public String outcome;
    public String status;
    public String elapsedTimeMS;

    public AIFJSONAnalysisResult(String datetime, String outcome, String status, String elapsedTimeMS) {
        this.datetime = datetime;
        this.outcome = outcome;
        this.status = status;
        this.elapsedTimeMS = elapsedTimeMS;
    }
}
