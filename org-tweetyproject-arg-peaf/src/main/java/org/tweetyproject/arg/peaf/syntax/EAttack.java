package org.tweetyproject.arg.peaf.syntax;

import java.util.Objects;
import java.util.Set;

public class EAttack {

    protected final String name;
    protected final Set<EArgument> froms;
    protected final Set<EArgument> tos;


    public EAttack(String name, Set<EArgument> froms, Set<EArgument> tos) {
        this.name = name;
        this.froms = froms;
        this.tos = tos;
    }

    public String getName() {
        return name;
    }

    public Set<EArgument> getFroms() {
        return froms;
    }

    public Set<EArgument> getTos() {
        return tos;
    }

    @Override
    public String toString() {
        return "EAtt{" + name +
                ", froms=" + froms +
                ", tos=" + tos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EAttack attack = (EAttack) o;
        return Objects.equals(name, attack.name) && Objects.equals(froms, attack.froms) && Objects.equals(tos, attack.tos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, froms, tos);
    }
}

