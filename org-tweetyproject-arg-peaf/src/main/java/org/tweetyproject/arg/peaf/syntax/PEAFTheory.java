package org.tweetyproject.arg.peaf.syntax;

import java.util.*;
import java.util.function.Consumer;


public class PEAFTheory extends AbstractEAFTheory<PSupport, PAttack> {

    public PEAFTheory() {

    }

    public PEAFTheory(int noArguments) {
        for (int i = 0; i < noArguments; i++) {
            this.addArgument(i);
        }
    }


    private PSupport createSupport(String name, Set<EArgument> froms, Set<EArgument> tos, double cp) {
        PSupport support = new PSupport(name, froms, tos, cp);
        for (EArgument to : tos) {
            to.setSupportedBy(support);
        }
        for (EArgument from : froms) {
            from.setSupports(support);
        }
        return support;
    }

    protected PAttack createAttack(String name, Set<EArgument> froms, Set<EArgument> tos, double cp) {
        if (tos.contains(eta)) {
            throw new RuntimeException("Argument eta can't be attacked.");
        }
        PAttack attack = new PAttack(name, froms, tos, cp);
        for (EArgument from : froms) {
            from.addAttack(attack);
        }
        for (EArgument to : tos) {
            to.addAttackedBy(attack);
        }
        return attack;
    }

    public EArgument addArgument(int identifier) {
        EArgument argument = this.createArgument(Integer.toString(identifier));
        this.addArgument(argument);
        return argument;
    }

    public void addSupport(int[] fromIndices, int[] toIndices, double cp) {
        Set<EArgument> froms = createEmptyArgSet(fromIndices);
        Set<EArgument> tos = createEmptyArgSet(toIndices);

        int identifier = supports.size();
        PSupport support = this.createSupport(Integer.toString(identifier), froms, tos, cp);
        this.addSupport(support);
    }

    public void addAttack(int[] fromIndices, int[] toIndices, double cp) {
        Set<EArgument> froms = createEmptyArgSet(fromIndices);
        Set<EArgument> tos = createEmptyArgSet(toIndices);

        int identifier = supports.size();
        PAttack attack = this.createAttack(Integer.toString(identifier), froms, tos, cp);
        this.addAttack(attack);
    }

}
