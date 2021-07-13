package org.tweetyproject.arg.eaf.syntax;


import java.util.ArrayList;
import java.util.Objects;

public class Argument {
    private final String name;
    private final ArrayList<Attack> attacks;
    private final ArrayList<Attack> attackedBy;

    private final ArrayList<Support> sourceOf;
    private final ArrayList<Support> targetOf;

    public Argument(String name) {
        this.name = name;
        attacks = new ArrayList<>();
        attackedBy = new ArrayList<>();
        sourceOf = new ArrayList<>();
        targetOf = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addAttack(Attack attack) {
        if (attack.getFrom() == this) {
            attacks.add(attack);
        }
        else {
            throw new RuntimeException("Attack is not from this argument.");
        }
    }

    public void addAttackedBy(Attack attack) {
        if (attack.getTo() == this) {
            attackedBy.add(attack);
        }
        else {
            throw new RuntimeException("Attack is not to this argument.");
        }
    }

    @Override
    public String toString() {
        return "Argument{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Argument argument = (Argument) o;
        return name.equals(argument.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void setTargetOf(Support support) {

    }

    public void setSourceOf(Support support) {

    }
}
