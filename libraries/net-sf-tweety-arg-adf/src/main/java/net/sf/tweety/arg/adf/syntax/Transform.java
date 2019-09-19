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
package net.sf.tweety.arg.adf.syntax;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * An interface which allows for transform operations on the recursive structure of AcceptanceCondition.
 * 
 * @author Mathias Hofer
 *
 * @param <R>
 *            intermediate type which results from transforming the current node
 *            and is shared between the parent and its children
 * @param <C>
 *            the result which gets consumed by some provided consumer
 */
public interface Transform<C, R> {

	public R transformDisjunction(Consumer<C> consumer, Collection<R> subconditions, int polarity);

	public R transformConjunction(Consumer<C> consumer, Collection<R> subconditions, int polarity);

	public R transformImplication(Consumer<C> consumer, R left, R right, int polarity);

	public R transformEquivalence(Consumer<C> consumer, R left, R right, int polarity);

	public R transformExclusiveDisjunction(Consumer<C> consumer, R left, R right, int polarity);

	public R transformNegation(Consumer<C> consumer, R sub, int polarity);

	public R transformArgument(Consumer<C> consumer, Argument argument, int polarity);

	public R transformContradiction(Consumer<C> consumer, int polarity);

	public R transformTautology(Consumer<C> consumer, int polarity);
	
}