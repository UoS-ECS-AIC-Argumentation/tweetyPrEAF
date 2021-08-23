package org.tweetyproject.arg.peaf.inducers;

import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

public abstract class AbstractPEAFInducer implements PEAFInducer {

    protected PEAFTheory peafTheory;

    public AbstractPEAFInducer(PEAFTheory peafTheory) {
        this.peafTheory = peafTheory;
    }

}
