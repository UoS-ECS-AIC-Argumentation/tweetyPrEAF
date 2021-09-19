package org.tweetyproject.arg.peaf.inducers;

import org.tweetyproject.arg.peaf.syntax.InducibleEAF;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.function.Consumer;

public abstract class AbstractPEAFInducer implements PEAFInducer {

    protected PEAFTheory peafTheory;

    public AbstractPEAFInducer(PEAFTheory peafTheory) {
        this.peafTheory = peafTheory;
    }

    public void induceNTimes(Consumer<InducibleEAF> consumer, int n) {
        while (n > 0 ) {
            this.induce(consumer);
            n--;
        }
    }
}
