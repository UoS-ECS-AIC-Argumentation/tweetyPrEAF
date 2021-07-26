package org.tweetyproject.arg.peaf.syntax;

import java.util.Set;

public class ESupport {

    protected final String name;
    protected final Set<EArgument> froms;
    protected final Set<EArgument> tos;

    public ESupport(String name, Set<EArgument> froms, Set<EArgument> tos) {
        this.name = name;
        this.froms = froms;
        this.tos = tos;
    }


    @Override
    public String toString() {
        return "ESupp{" + name +
                ", froms=" + froms +
                ", tos=" + tos +
                '}';
    }

    public Set<EArgument> getFroms() {
        return froms;
    }

    public Set<EArgument> getTos() {
        return tos;
    }

    public String getName() {
        return name;
    }
}
