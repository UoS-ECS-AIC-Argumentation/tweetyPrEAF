package org.tweetyproject.arg.peaf.syntax.aif;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public enum AIFNodeType {
    RA("RA"), // RA Node (is a S node) (considered as a support link)
    CA("CA"), // CA Node (is a S node) (considered as a attack link)
    I("I"),  // Information Node
    MA("MA"); // MA Node, Restatement Node (i.e. reframe)

    private static final Map<String, AIFNodeType> ENUM_MAP;
    private final String text;

    AIFNodeType(final String text) {
        this.text = text;
    }

    // Table 1, also details the types and sub-types https://aclanthology.org/W17-5114.pdf
    // MA("MA") // MA node is for reframe (again this is for dialogues), i.e. restatements
    private static final Set<String> ignoredNodeTypes = Sets.newHashSet("L", "YA", "TA");
    public static boolean isAnIgnoredNodeType(String type) {
        // Based on the sections and Figure 3 retrieved from here:
        // https://books.google.com/books?hl=en&lr=&id=U5rWx0Kh4vMC&oi=fnd&pg=PA311&dq=aif+dialogic+argumentation&ots=KvG2SgCksa&sig=sPX0nlXCTR0S4SdTIhlWzVWPXfs
        // http://www.simonwells.org/assets/papers/reed_2008_aif.plus.pdf
        // Upper ontology of AIF+ to AIF
        // TA Nodes: Transition Application (Describing a dialogue)
        // L("L"), // L: Locution node
        // YA("YA") // YA: Illocutionary Application (YA-) nodes
        // TA("TA"), // TA: Transition Application
        // Interesting slides:
        // https://www.academia.edu/download/48353941/Building_arguments_with_argumentation_th20160827-16862-1jyyooc.pdf
        return ignoredNodeTypes.contains(type);
    }

    @Override
    public String toString() {
        return text;
    }

    static {
        Map<String, AIFNodeType> map = new ConcurrentHashMap<String, AIFNodeType>();
        for (AIFNodeType instance : AIFNodeType.values()) {
            map.put(instance.getName().toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    private String getName() {
        return text;
    }

    public static AIFNodeType get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }
}
