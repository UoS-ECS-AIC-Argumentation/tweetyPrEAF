package org.tweetyproject.arg.peaf.syntax.aif;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This represents a class of the probabilities that can be assigned to arguments from AIF in text format.
 * This is based on PHIA Probability Yardstick.
 *
 * @author Taha Dogan Gunes
 */
public enum YardstickProbability {
    ALMOST_CERTAIN("almost_certain", (0.95 + 1.00) / 2.0),
    HIGHLY_LIKELY("highly_likely", (0.80 + 0.90) / 2.0),
    HIGHLY_UNLIKELY("highly_unlikely", (0.2 + 0.1) / 2.0),
    LIKELY("likely", (0.75 + 0.55) / 2.0),
    REALISTIC_PROBABILITY("realistic_probability", (0.50 + 0.40) / 2.0),
    REMOTE_CHANCE("remote_chance", (0.05 + 0.00) / 2.0),
    UNLIKELY("unlikely", (0.25 + 0.35) / 2.0);

    /**
     * This is used internally to make mapping from string to enumeration
     */
    private static final Map<String, YardstickProbability> ENUM_MAP;

    static {
        Map<String, YardstickProbability> map = new ConcurrentHashMap<String, YardstickProbability>();
        for (YardstickProbability instance : YardstickProbability.values()) {
            map.put(instance.getName().toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * The probability assigned to each class of probability.
     */
    private final double probability;

    /**
     * The name of the class of the probability
     */
    private final String name;

    /**
     * The default constructor
     *
     * @param name        the class name of the probability
     * @param probability the probability value
     */
    YardstickProbability(String name, final double probability) {
        this.name = name;
        this.probability = probability;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.probability + ")";
    }

    /**
     * Get the name of the class regarding the probability
     *
     * @return the name of the class
     */
    private String getName() {
        return this.name;
    }

    /**
     * Returns the YardstickProbability given the class name
     *
     * @param name the class name of the YardstickProbability
     * @return corresponding YardstickProbability
     */
    public static YardstickProbability get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }

    /**
     * Checks if the class name exists as a YardstickProbability
     *
     * @param name the name of the class
     * @return true if it exists
     */
    public static boolean contains(String name) {
        return ENUM_MAP.containsKey(name);
    }

    /**
     * Converts the YardstickProbability to double
     *
     * @return the probability value
     */
    public Double toDouble() {
        return this.probability;
    }

}
