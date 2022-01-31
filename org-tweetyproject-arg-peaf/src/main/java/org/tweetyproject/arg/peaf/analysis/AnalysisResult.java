package org.tweetyproject.arg.peaf.analysis;

/**
 * A record for storing analysis of the results
 *
 * @author Taha Dogan Gunes
 */
public record AnalysisResult(double probability, long noIterations, AnalysisType type, double totalProbability) {

    /**
     * Returns the probability of the analysis
     *
     * @return the probability
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Returns the number of iterations
     *
     * @return the number of iterations
     */
    public long getNoIterations() {
        return noIterations;
    }

    /**
     * For debugging purposes, prints the analysis result.
     */
    public void print() {
        System.out.println("Type: " + this.type + " prob: " + this.getProbability() + " iterations: " + this.getNoIterations() + " totalProbability: " + this.totalProbability);
    }

}
