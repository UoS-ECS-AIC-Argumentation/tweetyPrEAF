package org.tweetyproject.arg.peaf.analysis;

import org.tweetyproject.arg.peaf.syntax.EArgument;

import java.util.Set;

public interface JustificationAnalysis {
    AnalysisResult query(Set<EArgument> args);
}
