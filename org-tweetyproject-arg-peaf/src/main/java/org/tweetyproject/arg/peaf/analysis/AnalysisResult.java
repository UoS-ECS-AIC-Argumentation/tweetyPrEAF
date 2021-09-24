package org.tweetyproject.arg.peaf.analysis;

public record AnalysisResult(double probability, long noIterations) {

    public double getProbability() {
        return probability;
    }

    public long getNoIterations() {
        return noIterations;
    }
}
