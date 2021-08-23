package org.tweetyproject.arg.peaf.inducers;

import org.tweetyproject.arg.peaf.syntax.InducibleEAF;

import java.util.function.Consumer;

public interface PEAFInducer {
    void induce(Consumer<InducibleEAF> consumer);
}
