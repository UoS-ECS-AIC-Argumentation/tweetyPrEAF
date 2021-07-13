package org.tweetyproject.arg.eaf.syntax;

public class Support {

    private final String name;
    private final Argument from;
    private final Argument to;

    public Support(String name, Argument from, Argument to) {
        this.name = name;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "Support{" +
                "name='" + name + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
