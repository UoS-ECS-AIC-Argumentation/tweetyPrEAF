package org.tweetyproject.arg.peaf.syntax;


import com.google.common.collect.Sets;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.commons.util.Pair;
import org.tweetyproject.commons.util.SetTools;

import java.util.*;
import java.util.stream.Collectors;

public class EAFTheory extends AbstractEAFTheory<ESupport, EAttack> {



    public EAFTheory() {
    }

    public EAFTheory(int noArguments) {
        for (int i = 0; i < noArguments; i++) {
            this.addArgument(i);
        }
    }


    protected EArgument createArgument(String name) {
        return new EArgument(name);
    }

    private ESupport createSupport(String name, Set<EArgument> froms, Set<EArgument> tos) {
        ESupport support = new ESupport(name, froms, tos);
        for (EArgument to : tos) {
            to.setSupportedBy(support);
        }
        for (EArgument from : froms) {
            from.setSupports(support);
        }
        return support;
    }


    public EArgument addArgument(int identifier) {
        EArgument argument = this.createArgument(Integer.toString(identifier));
        this.addArgument(argument);
        return argument;
    }

    protected EAttack createAttack(String name, Set<EArgument> froms, Set<EArgument> tos) {
        if (tos.contains(eta)) {
            throw new RuntimeException("Argument eta can't be attacked.");
        }
        EAttack attack = new EAttack(name, froms, tos);
        for (EArgument from : froms) {
            from.addAttack(attack);
        }
        for (EArgument to : tos) {
            to.addAttackedBy(attack);
        }
        return attack;
    }

    public void addAttack(int[] fromIndices, int[] toIndices) {
        Set<EArgument> froms = createEmptyArgSet(fromIndices);
        Set<EArgument> tos = createEmptyArgSet(toIndices);

        int identifier = attacks.size();
        EAttack attack = this.createAttack(Integer.toString(identifier), froms, tos);
        this.addAttack(attack);
    }

    public void addSupport(int[] fromIndices, int[] toIndices) {
        Set<EArgument> froms = createEmptyArgSet(fromIndices);
        Set<EArgument> tos = createEmptyArgSet(toIndices);

        int identifier = supports.size();
        ESupport support = this.createSupport(Integer.toString(identifier), froms, tos);
        this.addSupport(support);
    }

    public ArrayList<EArgument> getArguments() {
        return arguments;
    }

    public ArrayList<ESupport> getSupports() {
        return supports;
    }

    public ArrayList<EAttack> getAttacks() {
        return attacks;
    }

    public Set<Set<EArgument>> convertToDAFWithMaximalSSAS() {
        // This method is using Algorithm 5 from Oren et. al. 2010 "Moving Between Argumentation Frameworks"
        // SSAS = Self-Supporting Argument Sets
        // Maximal part is for reducing redundant arguments that gets generated in the naive version of the conversion.


        Set<Set<EArgument>> Answer = new HashSet<>();

        for (EArgument a : arguments) {
            System.out.println("> Start " + a);
            Answer.addAll(computeBackSet(a, new HashSet<>()));
            System.out.println("> Stop " + a);
        }

        for (Iterator<Set<EArgument>> it = Answer.iterator(); it.hasNext(); ) {
            Set<EArgument> AS = it.next();
            if (!AS.contains(eta)) {
                it.remove();
            }

//            // Does answer has a duplicate or a superset of AS
//
            for (Iterator<Set<EArgument>> it2 = Answer.iterator(); it.hasNext(); ) {
                Set<EArgument> AS_PRIME = it.next();
                if (AS_PRIME.containsAll(AS)) {
                    it2.remove();
                }
            }

        }
        return Answer;
    }

    private Set<Set<EArgument>> computeBackSet(EArgument a, Set<Pair<Set<EArgument>, EArgument>> visited) {
        System.out.println("Visited: " + Arrays.toString(visited.toArray()));
        Set<Set<EArgument>> ans = new HashSet<>();
        for (ESupport support : a.getSupportedBy()) {
            Set<EArgument> X = support.getFroms();
            Pair<Set<EArgument>, EArgument> pair = new Pair<>(X, a);

            List<Set<EArgument>> B = new ArrayList<>();
            if (!visited.contains(pair)) {
                int i = 0;
                for (EArgument x_i : X) {
//                    Set<Pair<Set<EArgument>, EArgument>> newVisited = new HashSet<>(visited);
                    visited.add(pair);

                    if (i >= B.size()) {
                        B.add(new HashSet<>());
                    }
                    Set<EArgument> b = B.get(i);
                    b.add(x_i);


                    B.addAll(computeBackSet(x_i, visited));

                    i++;
                }
            }
            for (Set<EArgument> b : B) {
                b.add(a);
            }
            ans.addAll(B);
        }

        System.out.println("ans: " + ans);
        return ans;
    }

    public DungTheory convertToDAFNaively() {
        // This method is using Algorithm 1 from Oren et. al. 2010 "Moving Between Argumentation Frameworks"
        Set<Set<EArgument>> dungArguments = new HashSet<>(); // Line 1
        Set<Pair<Set<EArgument>, Set<EArgument>>> dungAttacks = new HashSet<>(); // Line 2

        // Line 3 - 7
        for (Set<EArgument> A : Sets.powerSet(this.getArgumentsAsSet())) {
            boolean isSelfSupporting = checkIsSelfSupporting(A);

            if (A.size() > 0 && isSelfSupporting) {
                dungArguments.add(A);
            }

        }
        // Line 8 - 12
        for (EAttack attack : attacks) { // Line 8
            Set<EArgument> X = new HashSet<>(attack.getFroms());
            for (EArgument x : X) { // Line 8
                for (EAttack xAttack : x.getAttacks()) { // Line 8
                    for (EArgument a : xAttack.getTos()) { // Line 8
                        for (Set<EArgument> D : dungArguments) { // Line 9
                            if (D.containsAll(X)) { // Line 9: check X is subset of D
                                for (Set<EArgument> A : dungArguments) { // Line 9
                                    if (A.contains(a)) { // Line 9: check if a is in A, when A is in DARGS
                                        dungAttacks.add(new Pair<>(D, A));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Map<String, Argument> mapping = new HashMap<>();
        DungTheory dungTheory = new DungTheory();
        for (Set<EArgument> dungArgument : dungArguments) {
            String nameOfArgument = dungArgument.stream()
                    .map(EArgument::getName)
                    .sorted()
                    .collect(Collectors.joining("_"));
            Argument argument = new Argument(nameOfArgument);
            mapping.put(nameOfArgument, argument);
            dungTheory.add(argument);
        }

        for (Pair<Set<EArgument>, Set<EArgument>> dungAttack : dungAttacks) {
            String nameOfFrom = dungAttack.getFirst().stream()
                    .map(EArgument::getName)
                    .sorted()
                    .collect(Collectors.joining("_"));
            Argument from = mapping.get(nameOfFrom);

            String nameOfTo = dungAttack.getSecond().stream()
                    .map(EArgument::getName)
                    .sorted()
                    .collect(Collectors.joining("_"));
            Argument to = mapping.get(nameOfTo);

            dungTheory.addAttack(from, to);
        }
        return dungTheory;
    }

    public boolean checkIsSelfSupporting(Set<EArgument> A) {
        // Check if the subset is self-supporting (Line 4, if A is self-supporting)
        // A set of arguments A is self-supporting if and only if for all a in A, A e-supports a
        boolean isSelfSupporting = true;
        for (EArgument a : A) { // for all a in A
            if (a == eta) {
                continue;
            }
            Set<EArgument> A_copy = new HashSet<>(A);

            // Definition 8 (Auxiliary Notions for EAFs)
            // A set of arguments S is self-supporting iff for all x \in S, S e-supports x

            // Assume that there exists a T that is subset of A.
            // if for all elements of T, (each of T is t) support a, then a has evidential support from A \ {a}
            A_copy.remove(a);

            boolean supports = false;


            for (Set<EArgument> T : Sets.powerSet(A_copy)) {
                for (EArgument x : T) {
                    if (a.isSupportedBy(x)) {
                        supports = true;
                    }
                }
                if (supports) {
                    break;
                }
            }


            isSelfSupporting = isSelfSupporting && supports;
        }
        return isSelfSupporting;
    }

    public void prettyPrint() {
        System.out.println("-- Arguments --");
        int i = 0;
        for (EArgument argument : this.getArguments()) {
            System.out.println(i + ". " + argument);
            i++;
        }

        System.out.println("");
        System.out.println("-- Supports --");
        i = 0;
        for (ESupport support : this.getSupports()) {
            System.out.println(i + ". " + support);
            i++;
        }

        System.out.println("");
        System.out.println("-- Attacks --");
        i = 0;
        for (EAttack attack : this.getAttacks()) {
            System.out.println(i + ". " + attack);
            i++;
        }


    }
}
