package org.tweetyproject.arg.peaf.analysis;

public record AnalysisResult(double probability, long noIterations, AnalysisType type, double totalProbability) {

    public double getProbability() {
        return probability;
    }

    public long getNoIterations() {
        return noIterations;
    }


    public void print() {
        System.out.println("Type: " + this.type + " prob: " + this.getProbability() + " iterations: " + this.getNoIterations()  + " totalProbability: " + this.totalProbability);
    }

}
