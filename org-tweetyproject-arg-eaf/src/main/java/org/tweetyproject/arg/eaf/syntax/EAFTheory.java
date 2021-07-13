package org.tweetyproject.arg.eaf.syntax;


import java.util.ArrayList;

public class EAFTheory{

    private Argument eta;

    private ArrayList<Argument> arguments = new ArrayList<>();
    private ArrayList<Support> supports = new ArrayList<>();
    private ArrayList<Attack> attacks = new ArrayList<>();

    public EAFTheory() {
        super();
        eta = new Argument("eta");
        this.addArgument(eta);
    }

    private void addArgument(Argument argument) {
        if (arguments.size() == 0) {
            eta = argument;
        }
        arguments.add(argument);
    }

    private boolean addAttack(Attack attack) {
        return attacks.add(attack);
    }

    private boolean addSupport(Support support) {
        return supports.add(support);
    }

    private Attack createAttack(String name, Argument from, Argument to) {
        if (to.equals(eta)) {
            throw new RuntimeException("Argument eta can't be attacked.");
        }
        Attack attack = new Attack(name, from, to);
        from.addAttack(attack);
        to.addAttackedBy(attack);
        return attack;
    }

    private Argument createArgument(String name) {
        return new Argument(name);
    }

    public Support createSupport(String name, Argument from, Argument to) {
        Support support = new Support(name, from, to);
        to.setTargetOf(support);
        from.setSourceOf(support);
        return support;
    }


    public void addArgument(int identifier) {
        Argument argument = this.createArgument(Integer.toString(identifier));
        this.addArgument(argument);
    }

    public void addAttack(int from, int to) {
        Argument fromArg = arguments.get(from);
        Argument toArg = arguments.get(to);
        int identifier = attacks.size();
        Attack attack = this.createAttack(Integer.toString(identifier), fromArg, toArg);
        this.addAttack(attack);
    }

    public void addSupport(int from, int to) {
        Argument fromArg = arguments.get(from);
        Argument toArg = arguments.get(to);
        int identifier = supports.size();
        Support support = this.createSupport(Integer.toString(identifier), fromArg, toArg);
        this.addSupport(support);
    }
}
