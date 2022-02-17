package org.tweetyproject.arg.peaf.analysis;

/**
 * A record for storing analysis of the results
 *
 * @author Taha Dogan Gunes
 */
public record AnalysisResult(double result, long noIterations, AnalysisType type, double totalProbability) {

    /**
     * Returns the result of the analysis
     *
     * @return the result
     */
    public double getResult() {
        return result;
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
        System.out.println("Type: " + this.type + " result: " + this.getResult() + " iterations: " + this.getNoIterations() + " totalProbability: " + this.totalProbability);
    }

}
