package org.tweetyproject.arg.peaf.io;

import org.tweetyproject.arg.peaf.syntax.*;

/**
 * Creates an EAF from a PEAF (by getting rid of probabilities)
 * <p>
 * Users of this must be aware that the created PEAF and EAF's objects are linked together. Therefore,
 * the created file is not a new copy of argument, support and attack objects. This class only creates a
 * view.
 *
 * @author Taha Dogan Gunes
 */
public class PEAFToEAFConverter {
    public static EAFTheory convert(NamedPEAFTheory peaf) {
        EAFTheory eafTheory = new EAFTheory();

        for (EArgument argument : peaf.getArguments()) {
            eafTheory.addArgument(argument);
        }

        for (PSupport support : peaf.getSupports()) {
            eafTheory.addSupport(support);
        }

        for (EAttack attack : peaf.getAttacks()) {
            eafTheory.addAttack(attack);
        }

        return eafTheory;
    }
}
