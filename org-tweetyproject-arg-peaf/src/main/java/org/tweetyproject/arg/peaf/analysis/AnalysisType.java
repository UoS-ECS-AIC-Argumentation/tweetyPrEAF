package org.tweetyproject.arg.peaf.analysis;

public enum AnalysisType {
    EXACT("exact"),
    LI_EXACT("li_exact"),
    APPROX("approx"),
    CONCURRENT_APPROX("con_approx"),
    CONCURRENT_EXACT("con_exact");

    private final String text;

    AnalysisType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
