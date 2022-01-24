package org.tweetyproject.arg.peaf.syntax;


import com.google.common.collect.Sets;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.commons.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class implements an abstract argumentation theory in the sense of Evidential Argumentation Frameworks (EAF).
 * </br>
 * </br>See
 * </br>
 * </br> Li, Hengfei. Probabilistic argumentation. 2015. PhD Thesis. Aberdeen University.
 *
 * @author Taha Dogan Gunes
 */
public class EAFTheory extends AbstractEAFTheory<ESupport> {

    /**
     * Default constructor; initializes an empty EAFTheory
     */
    public EAFTheory() {
    }

    /**
     * Optional constructor; initializes an EAFTheory with arguments
     *
     * @param noArguments the number of arguments
     */
    public EAFTheory(int noArguments) {
        for (int i = 0; i < noArguments; i++) {
            this.addArgument(i);
        }
    }

    /**
     * Creates a new EAFTheory from an PEAFTheory (the probabilities are eliminated)
     *
     * @param peafTheory PEAFTheory object
     * @return
     */
    public static EAFTheory newEAFTheory(PEAFTheory peafTheory) {
        EAFTheory eafTheory = new EAFTheory();
        for (EArgument argument : peafTheory.getArguments()) {
            eafTheory.addArgument(argument);
        }

        for (EAttack attack : peafTheory.getAttacks()) {
            EAttack attack2 = eafTheory.createAttack(attack.name, attack.getFroms(), attack.getTos());
            eafTheory.addAttack(attack2);
        }

        for (PSupport support : peafTheory.getSupports()) {
            ESupport support2 = eafTheory.createSupport(support.name, support.getFroms(), support.getTos());
            eafTheory.addSupport(support2);
        }

        return eafTheory;
    }


    /**
     * Creates an arguments with a name
     *
     * @param name the name of the argument
     * @return EArgument object
     */
    protected EArgument createArgument(String name) {
        return new EArgument(name);
    }

    /**
     * Internal method to create a support object
     *
     * @param name  the name of the support
     * @param froms the set of arguments that the support originates from
     * @param tos   the set of arguments that the support targets
     * @return ESupport object
     */
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


    /**
     * Add an argument with an integer identifier
     *
     * @param identifier the identifier of the argument
     * @return EArgument object
     */
    public EArgument addArgument(int identifier) {
        EArgument argument = this.createArgument(Integer.toString(identifier));
        this.addArgument(argument);
        return argument;
    }


    /**
     * Add a support with index arrays (froms and tos)
     *
     * @param fromIndices integer array with argument, represents indices of arguments
     * @param toIndices   integer array with argument, represents indices of arguments
     */
    public void addSupport(int[] fromIndices, int[] toIndices) {
        Set<EArgument> froms = createEmptyArgSet(fromIndices);
        Set<EArgument> tos = createEmptyArgSet(toIndices);

        int identifier = supports.size();
        ESupport support = this.createSupport(Integer.toString(identifier), froms, tos);
        this.addSupport(support);
    }

    /**
     * Gets arguments of the EAF
     *
     * @return list of arguments inside the EAF (insert ordered)
     */
    public ArrayList<EArgument> getArguments() {
        return arguments;
    }

    /**
     * Gets supports of the EAF
     *
     * @return list of supports inside the EAF (insert ordered)
     */
    public ArrayList<ESupport> getSupports() {
        return supports;
    }

    /**
     * Gets attacks of the EAF
     *
     * @return list of attacks inside the EAF (insert ordered)
     */
    public ArrayList<EAttack> getAttacks() {
        return attacks;
    }

    /**
     * This method converts this EAF to a DAF with using Algorithm 1 from
     * Oren et. al. 2010 "Moving Between Argumentation Frameworks"
     *
     * @return DungTheory object
     */
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

    /**
     * Checks if a set of arguments whether they are self-supporting or not
     * This is used for conversion from EAF to DAF.
     * A set of arguments A is self-supporting if and only if for all a in A, A e-supports a
     *
     * @param A a set of arguments
     * @return true if the set of arguments are self-supporting or not.
     */
    public boolean checkIsSelfSupporting(Set<EArgument> A) {
        // Check if the subset is self-supporting (Line 4, if A is self-supporting)
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

    /**
     * Pretty print of the EAFTheory
     */
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
