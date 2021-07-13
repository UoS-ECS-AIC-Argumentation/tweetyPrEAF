package org.tweetyproject.arg.eaf.syntax;

import java.util.Objects;

public class Attack {

    private final String name;
    private Argument from;
    private Argument to;

    public Attack(String name, Argument from, Argument to) {
        this.name = name;
        this.from = from;
        this.to = to;
    }

    public Argument getFrom() {
        return from;
    }

    public Argument getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attack attack = (Attack) o;
        return name.equals(attack.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Attack{" +
                "name='" + name + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
