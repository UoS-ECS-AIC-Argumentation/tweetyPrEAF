package org.tweetyproject.arg.peaf.analysis;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum AnalysisType {
    EXACT("exact"),
    LI_EXACT("li_exact"),
    APPROX("approx"),
    CONCURRENT_APPROX("con_approx"),
    CONCURRENT_EXACT("con_exact"),
    PREFERRED("preferred");

    private static final Map<String, AnalysisType> ENUM_MAP;
    private final String text;

    AnalysisType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    static {
        Map<String, AnalysisType> map = new ConcurrentHashMap<String, AnalysisType>();
        for (AnalysisType instance : AnalysisType.values()) {
            map.put(instance.getName().toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    private String getName() {
        return text;
    }

    public static AnalysisType get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }
}
