package org.tweetyproject.arg.peaf.syntax;

import org.tweetyproject.arg.peaf.io.preaf.EdgeListWriter;

import java.util.Set;

/**
 * This class implements an abstract argumentation theory
 * in the sense of Probabilistic Evidential Argumentation Frameworks (PrEAF).
 * </br>
 * </br>See
 * </br>
 * </br> Li, Hengfei. Probabilistic argumentation. 2015. PhD Thesis. Aberdeen University.
 *
 * @author Taha Dogan Gunes
 */
public class PEAFTheory extends AbstractEAFTheory<PSupport> {

    /**
     * Default constructor; initializes an empty PEAFTheory
     */
    public PEAFTheory() {
    }

    /**
     * Optional constructor; initializes an PEAFTheory with arguments
     *
     * @param noArguments the number of arguments
     */
    public PEAFTheory(int noArguments) {
        for (int i = 0; i < noArguments; i++) {
            this.addArgument(i);
        }
    }

    /**
     * Internal method to create a support object
     *
     * @param name  the name of the support
     * @param froms the set of arguments that the support originates from
     * @param tos   the set of arguments that the support targets
     * @param cp    the probability assigned to the support link (must be in range [0.0, 1.0])
     * @return PSupport object that is created but not stored
     */
    private PSupport createSupport(String name, Set<EArgument> froms, Set<EArgument> tos, double cp) {
        PSupport support = new PSupport(name, froms, tos, cp);
        for (EArgument from : froms) {
            from.addSupport(support);
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
     * Add a support with indices for one to one mapping (fromIndex and toIndex)
     *
     * @param fromIndex the index of the argument originates the support
     * @param toIndex   the index of the argument getting supported
     * @param cp        the probability assigned to the support link (must be in range [0.0, 1.0])
     */
    public void addSupport(int fromIndex, int toIndex, double cp) {
        this.addSupport(new int[]{fromIndex}, new int[]{toIndex}, cp);
    }

    /**
     * Add a support with indices for many to many mapping
     *
     * @param fromIndices integer array with argument, represents indices of arguments
     * @param toIndices   integer array with argument, represents indices of arguments
     * @param cp          the probability assigned to the support link (must be in range [0.0, 1.0])
     */
    public void addSupport(int[] fromIndices, int[] toIndices, double cp) {
        Set<EArgument> froms = createEmptyArgSet(fromIndices);
        Set<EArgument> tos = createEmptyArgSet(toIndices);

        int identifier = supports.size();
        PSupport support = this.createSupport(Integer.toString(identifier), froms, tos, cp);
        this.addSupport(support);
    }

    /**
     * Add attack between arguments with indices
     *
     * @param fromIndex the index of the EArgument that originates the attack
     * @param toIndex   the index of the EArgument that receieves the attack
     */
    public void addAttack(int fromIndex, int toIndex) {
        this.addAttack(new int[]{fromIndex}, new int[]{toIndex});
    }

    /**
     * Returns an ascii tree for debug purposes.
     * This is usually used in the unit tests.
     *
     * @return the ascii tree in string
     */
    public String getASCIITree() {
        StringBuilder builder = new StringBuilder();
        for (PSupport support : supports) {
            StringBuilder builder1 = EdgeListWriter.getStringBuilder(support.getFroms(), support.getTos(), " -> ");
            if (builder1 != null) {
                builder.append(builder1);
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    /**
     * Helper function to print the ascii tree
     */
    public void printASCIITree() {
        System.out.println(this.getASCIITree());
    }


}
