package org.tweetyproject.arg.adf.reasoner.sat.parallel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.tweetyproject.arg.adf.syntax.Argument;
import org.tweetyproject.arg.adf.syntax.adf.AbstractDialecticalFramework;

public final class RandomDecomposer extends AbstractDecomposer {

	@Override
	Set<Argument> partition(AbstractDialecticalFramework adf, int count) {
		List<Argument> list = new ArrayList<>(adf.getArguments());
		Collections.shuffle(list);
		return Set.copyOf( list.subList(0, count) );
	}

}
