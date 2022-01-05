package org.tweetyproject.arg.peaf.syntax.aif;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum YardstickProbability {
    REMOTE_CHANCE("remote_chance", (0.05 + 0.00)/2.0),
    HIGHLY_UNLIKELY("highly_unlikely",(0.2 + 0.1)/2.0),
    UNLIKELY("unlikely",(0.25 + 0.35)/2.0),
    REALISTIC_PROBABILITY("realistic_probability",(0.50 + 0.40)/2.0),
    LIKELY("likely",(0.75 + 0.55)/2.0),
    HIGHLY_LIKELY("highly_likely",(0.80 + 0.90)/2.0),
    ALMOST_CERTAIN("almost_certain",(0.95 + 1.00)/2.0);

    private static final Map<String, YardstickProbability> ENUM_MAP;
    private final double probability;
    private final String name;
    YardstickProbability(String name, final double probability) {
        this.name = name;
        this.probability = probability;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.probability + ")";
    }

    static {
        Map<String, YardstickProbability> map = new ConcurrentHashMap<String, YardstickProbability>();
        for (YardstickProbability instance : YardstickProbability.values()) {
            map.put(instance.getName().toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    private String getName() {
        return this.name;
    }

    public static YardstickProbability get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }

    public static boolean contains(String name) {
        return ENUM_MAP.containsKey(name);
    }

    public Double toDouble() {
        return this.probability;
    }

    public static void main(String[] args) {
        for (Map.Entry<String, YardstickProbability> entry : ENUM_MAP.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().probability);
        }
    }
}
