package org.tweetyproject.arg.peaf.evaluation.converters;

import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;

public class EtaToAllConverter extends DAFToEAFConverter {

    @Override
    public EAFTheory convert(DungTheory dungTheory) {
        EAFTheory eafTheory = super.convert(dungTheory);

        // ignore eta by i = 1;
        for (int i = 1; i < eafTheory.getNumberOfNodes(); i++) {
            eafTheory.addSupport(new int[]{0}, new int[]{i});
        }

        return eafTheory;
    }

}
