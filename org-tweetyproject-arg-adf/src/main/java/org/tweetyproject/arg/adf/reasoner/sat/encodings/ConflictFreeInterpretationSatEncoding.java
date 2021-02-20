/*
 *  This file is part of "TweetyProject", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  TweetyProject is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright 2019 The TweetyProject Team <http://tweetyproject.org/contact/>
 */
package org.tweetyproject.arg.adf.reasoner.sat.encodings;

import java.util.function.Consumer;

import org.tweetyproject.arg.adf.semantics.link.Link;
import org.tweetyproject.arg.adf.syntax.Argument;
import org.tweetyproject.arg.adf.syntax.acc.AcceptanceCondition;
import org.tweetyproject.arg.adf.syntax.adf.AbstractDialecticalFramework;
import org.tweetyproject.arg.adf.syntax.pl.Clause;
import org.tweetyproject.arg.adf.syntax.pl.Literal;
import org.tweetyproject.arg.adf.transform.TseitinTransformer;

/**
 * @author Mathias Hofer
 *
 */
public class ConflictFreeInterpretationSatEncoding implements SatEncoding {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.tweety.arg.adf.reasoner.strategy.sat.SatEncoding#encode(net.sf.tweety.arg.
	 * adf.reasoner.sat.SatEncodingContext)
	 */
	@Override
	public void encode(Consumer<Clause> consumer, AbstractDialecticalFramework adf, PropositionalMapping context) {
		for (Argument s : adf.getArguments()) {
			AcceptanceCondition acc = adf.getAcceptanceCondition(s);
			
			TseitinTransformer transformer = TseitinTransformer.ofPositivePolarity(r -> context.getLink(adf.link(r, s)), false);
			Literal accName = transformer.collect(acc, consumer);
			
			// the propositions represent the assignment of s
			Literal trueRepr = context.getTrue(s);
			Literal falseRepr = context.getFalse(s);

			// link the arguments to their acceptance conditions
			consumer.accept(Clause.of(trueRepr.neg(), accName));
			consumer.accept(Clause.of(falseRepr.neg(), accName.neg()));

			// draw connection between argument and outgoing links
			for (Link relation : adf.linksFrom(s)) {
				Literal linkRepr = context.getLink(relation);

				consumer.accept(Clause.of(trueRepr.neg(), linkRepr));
				consumer.accept(Clause.of(falseRepr.neg(), linkRepr.neg()));
			}

			// make sure that we never satisfy s_t and s_f at the same time
			consumer.accept(Clause.of(trueRepr.neg(), falseRepr.neg()));
		}
	}

}
