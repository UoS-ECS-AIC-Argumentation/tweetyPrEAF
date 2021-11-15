package org.tweetyproject.arg.peaf.syntax.aif;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum AIFNodeType {
    RA("RA"), // RA Node (is a S node)
    CA("CA"), // CA Node (is a S node)
    I("I");  // Information Node

    private static final Map<String, AIFNodeType> ENUM_MAP;
    private final String text;

    AIFNodeType(final String text) {
        this.text = text;
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
