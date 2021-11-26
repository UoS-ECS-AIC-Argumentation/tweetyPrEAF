package org.tweetyproject.arg.peaf.syntax;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EArgument {
    private String name;
    private final Set<EAttack> attacks;
    private final Set<EAttack> attackedBy;

    private final Set<ESupport> supports;
    private final Set<ESupport> supportedBy;

    public EArgument(String name) {
        this.name = name;
        attacks = new HashSet<>();
        attackedBy = new HashSet<>();
        supports = new HashSet<>();
        supportedBy = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void addAttack(EAttack attack) {
        if (attack.getFroms().contains(this)) {
            attacks.add(attack);
        } else {
            throw new RuntimeException("Attack is not from this argument.");
        }
    }

    public void addAttackedBy(EAttack attack) {
        if (attack.getTos().contains(this)) {
            attackedBy.add(attack);
        } else {
            throw new RuntimeException("Attack is not to this argument.");
        }
    }

    public Set<ESupport> getSupports() {
        return supports;
    }

    public Set<ESupport> getSupportedBy() {
        return supportedBy;
    }

    @Override
    public String toString() {
        return "Arg{" + name + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EArgument argument = (EArgument) o;
        return name.equals(argument.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void setSupports(ESupport support) {
        this.supports.add(support);
    }

    public void setSupportedBy(ESupport support) {
        this.supportedBy.add(support);
    }

    public boolean isSupportedBy(EArgument argument) {
        Set<ESupport> supports = argument.getSupports();
        for (ESupport support : supports) {
            Set<EArgument> argSet = support.getTos();
            if (argSet.contains(this)) {
                return true;
            }
        }
        return false;
    }

    public Set<EAttack> getAttacks() {
        return attacks;
    }

    public Set<EAttack> getAttackedBy() {
        return attackedBy;
    }

    public void setName(String name) {
        this.name = name;
    }
}
