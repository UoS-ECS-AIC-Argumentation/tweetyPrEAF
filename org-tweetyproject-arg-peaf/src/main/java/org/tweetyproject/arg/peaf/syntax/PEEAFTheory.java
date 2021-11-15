package org.tweetyproject.arg.peaf.syntax;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class PEEAFTheory {

    public abstract class Element {
        protected final String identifier;

        Element(String identifier) {
            this.identifier = identifier;
        }
    }

    public class Argument extends Element {
        private final String name;

        Argument(String identifier, String name) {
            super(identifier);
            this.name = name;
        }

        @Override
        public String toString() {
            return "Arg{" +
                    "" + name +
                    '}';
        }

        public String getName() {
            return name;
        }
    }

    public class Support extends Element {
        private Set<Argument> froms = Sets.newHashSet();
        private Argument to;
        private double probability;

        Support(String identifier, double probability) {
            super(identifier);
            this.probability = probability;
        }

        public void addFrom(Argument from) {
            this.froms.add(from);
        }

        public void setTo(Argument to) {
            this.to = to;
        }

        public Set<Argument> getFroms() {
            return froms;
        }

        public Argument getTo() {
            return to;
        }

        @Override
        public String toString() {
            return "Supp{" + identifier + ", " +
                    "froms=" + froms +
                    ", to=" + to +
                    ", probability=" + probability +
                    '}';
        }

        public double getProbability() {
            return probability;
        }
    }

    public class Attack extends Element {
        private Argument from;
        private Element to;
        private double probability;

        Attack(String identifier, double probability) {
            super(identifier);
            this.probability = probability;
        }

        public void setFrom(Argument from) {
            this.from = from;
        }

        public void setTo(Element to) {
            this.to = to;
        }

        public Argument getFrom() {
            return from;
        }

        public Element getTo() {
            return to;
        }

        @Override
        public String toString() {
            return "Att{" + identifier + ", " +
                    "from=" + from +
                    ", to=" + to +
                    ", probability=" + probability +
                    '}';
        }

        public double getProbability() {
            return probability;
        }
    }

    protected Argument eta;
    protected Set<Argument> argumentsSet = Sets.newHashSet();
    protected Map<String, Element> identifierElementMap = Maps.newHashMap();


    protected ArrayList<Argument> arguments = new ArrayList<>();
    protected ArrayList<Support> supports = new ArrayList<>();
    protected ArrayList<Attack> attacks = new ArrayList<>();

    public PEEAFTheory() {

    }

    public void addArgument(String identifier, String textContent) {
        Argument arg = new Argument(identifier, textContent);
        this.arguments.add(arg);
        this.argumentsSet.add(arg);
        identifierElementMap.put(identifier, arg);
    }

    public void addSupport(String identifier, String[] fromIdentifiers, String toIdentifier, double probability) {
        Support support = new Support(identifier, probability);

        for (String fromIdentifier : fromIdentifiers) {
            Argument from = checkAndGetArgument(fromIdentifier);
            support.addFrom(from);
        }

        Argument to = checkAndGetArgument(toIdentifier);
        support.setTo(to);

        supports.add(support);
        identifierElementMap.put(identifier, support);
    }

    public void addAttack(String identifier, String fromIdentifier, String toIdentifier, double probability) {
        Attack attack = new Attack(identifier, probability);

        Argument from = checkAndGetArgument(fromIdentifier);
        Element to = checkAndGetElement(toIdentifier);

        attack.setFrom(from);
        attack.setTo(to);

        attacks.add(attack);
        identifierElementMap.put(identifier, attack);
    }

    private Argument checkAndGetArgument(String identifier) {
        Argument to = (Argument) this.identifierElementMap.get(identifier);
        if (to == null) {
            throw new RuntimeException("The argument with id=`" + identifier + "` was not found.");
        }
        return to;
    }

    private Element checkAndGetElement(String identifier) {
        Element to = this.identifierElementMap.get(identifier);
        if (to == null) {
            throw new RuntimeException("The element with id=`" + identifier + "` was not found.");
        }
        return to;
    }

    public ArrayList<Argument> getArguments() {
        return arguments;
    }

    public ArrayList<Support> getSupports() {
        return supports;
    }

    public ArrayList<Attack> getAttacks() {
        return attacks;
    }

    public void prettyPrint() {
        System.out.println("\nPEEAF:");
        System.out.println("-- Arguments --");
        int i = 0;
        for (Argument argument : this.getArguments()) {
            System.out.println(i + ". " + argument.toString());
            i++;
        }

        System.out.println("");
        System.out.println("-- Supports --");
        i = 0;
        for (Support support : this.getSupports()) {
            System.out.println(i + ". " + support.toString());
            i++;
        }

        System.out.println("");
        System.out.println("-- Attacks --");
        i = 0;
        for (Attack attack : this.getAttacks()) {
            System.out.println(i + ". " + attack.toString());
            i++;
        }

        System.out.println("\n");
    }

}