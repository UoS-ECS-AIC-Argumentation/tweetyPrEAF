package org.tweetyproject.arg.peaf.evaluation.converters;

import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;

/**
 * EtaToAllConverter class converts a DungTheory object to an EAFTheory by
 * adding support links from eta to all other arguments.
 *
 * @author Taha Dogan Gunes
 */
public class EtaToAllConverter extends DAFToEAFConverter {

    /**
     * Converts DungTheory object to EAFTheory by adding supports from eta to all other arguments.
     *
     * @param dungTheory a DungTheory object
     * @return an EAFTheory object
     */
    @Override
    public EAFTheory convert(DungTheory dungTheory) {
        EAFTheory eafTheory = super.convert(dungTheory);

        // ignore eta by i = 1;
        for (int i = 1; i < eafTheory.getNumberOfArguments(); i++) {
            eafTheory.addSupport(new int[]{0}, new int[]{i});
        }

        return eafTheory;
    }

}
