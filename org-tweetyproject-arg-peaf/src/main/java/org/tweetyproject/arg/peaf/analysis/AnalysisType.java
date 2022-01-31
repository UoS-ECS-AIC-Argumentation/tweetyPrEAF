package org.tweetyproject.arg.peaf.analysis;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The types of analysis supported by arg.peaf
 *
 * @author Taha Dogan Gunes
 */
public enum AnalysisType {
    /**
     * @see ExactAnalysis
     */
    EXACT("exact"),
    /**
     * @see ApproxAnalysis
     */
    APPROX("approx"),
    /**
     * @see ConcurrentApproxAnalysis
     */
    CONCURRENT_APPROX("con_approx"),
    /**
     * @see ConcurrentExactAnalysis
     */
    CONCURRENT_EXACT("con_exact"),
    /**
     * @see PreferredAnalysis
     */
    PREFERRED("preferred");

    /**
     * Internal map for string enumeration
     */
    private static final Map<String, AnalysisType> ENUM_MAP;

    static {
        Map<String, AnalysisType> map = new ConcurrentHashMap<>();
        for (AnalysisType instance : AnalysisType.values()) {
            map.put(instance.getName().toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * The keyword of the analysis
     */
    private final String text;

    /**
     * Creates an AnalysisType object
     *
     * @param text the keyword
     */
    AnalysisType(final String text) {
        this.text = text;
    }

    /**
     * Get the AnalysisType by giving the keyword
     *
     * @param name the keyword in string
     * @return the AnalysisType
     */
    public static AnalysisType get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }

    @Override
    public String toString() {
        return text;
    }

    /**
     * Return the keyword of the AnalysisType
     *
     * @return the keyword in string
     */
    private String getName() {
        return text;
    }
}
