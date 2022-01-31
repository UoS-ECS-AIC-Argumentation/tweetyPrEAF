package org.tweetyproject.arg.peaf.syntax.aif;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AIFNodeType corresponds the category of the node.
 * These can be RA, CA, I, MA, however this package is not supporting L, YA, and TA nodes.
 * More details are given here:
 * <p>
 * - https://aclanthology.org/W17-5114.pdf (Table 1, also details the types and sub-types)
 * - https://books.google.com/books?hl=en&lr=&id=U5rWx0Kh4vMC&oi=fnd&pg=PA311&dq=aif+dialogic+argumentation&ots=KvG2SgCksa&sig=sPX0nlXCTR0S4SdTIhlWzVWPXfs
 * - http://www.simonwells.org/assets/papers/reed_2008_aif.plus.pdf
 * - https://www.academia.edu/download/48353941/Building_arguments_with_argumentation_th20160827-16862-1jyyooc.pdf
 *
 * @author Taha Dogan Gunes
 */
public enum AIFNodeType {
    /**
     * RA Node (is a S node) (considered as a support link)
     */
    RA("RA"),

    /**
     * CA Node (is a S node) (considered as a attack link)
     */
    CA("CA"),
    /**
     * Information Node
     */
    I("I"),
    /**
     * MA Node, Restatement Node (i.e. reframe)
     */
    MA("MA");

    /**
     * This is used internally to make mapping from string to enumeration
     */
    private static final Map<String, AIFNodeType> ENUM_MAP;

    /**
     * The node type in string
     */
    private final String text;

    static {
        Map<String, AIFNodeType> map = new ConcurrentHashMap<String, AIFNodeType>();
        for (AIFNodeType instance : AIFNodeType.values()) {
            map.put(instance.getName().toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * The default constructor that creates the enumerations
     *
     * @param text the node type in string
     */
    AIFNodeType(final String text) {
        this.text = text;
    }

    /**
     * The ignored node types for the AIFCISReader
     * <p>
     * Table 1, also details the types and sub-types https://aclanthology.org/W17-5114.pdf
     * MA("MA") // MA node is for reframe (again this is for dialogues), i.e. restatements
     */
    private static final Set<String> ignoredNodeTypes = Sets.newHashSet("L", "YA", "TA");

    /**
     * Check if given the node type is ignored or not
     *
     * @param type the node type in string
     * @return true if the node type is ignored
     */
    public static boolean isAnIgnoredNodeType(String type) {
        return ignoredNodeTypes.contains(type);
    }

    /**
     * Converts the object to string
     *
     * @return the node type in string
     */
    @Override
    public String toString() {
        return text;
    }

    /**
     * Return the node type in string of the enum
     *
     * @return the node type in string
     */
    private String getName() {
        return text;
    }

    /**
     * Convert the node type in string to enum
     *
     * @param name the node type in string
     * @return AIFNodeType enum
     */
    public static AIFNodeType get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }
}
