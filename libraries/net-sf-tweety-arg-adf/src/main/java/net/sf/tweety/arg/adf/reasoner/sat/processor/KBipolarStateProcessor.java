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
package net.sf.tweety.arg.adf.reasoner.sat.processor;

import net.sf.tweety.arg.adf.reasoner.sat.encodings.BipolarSatEncoding;
import net.sf.tweety.arg.adf.reasoner.sat.encodings.KBipolarSatEncoding;
import net.sf.tweety.arg.adf.reasoner.sat.encodings.PropositionalMapping;
import net.sf.tweety.arg.adf.reasoner.sat.encodings.SatEncoding;
import net.sf.tweety.arg.adf.sat.SatSolverState;
import net.sf.tweety.arg.adf.syntax.adf.AbstractDialecticalFramework;

/**
 * @author Mathias Hofer
 *
 */
public final class KBipolarStateProcessor implements StateProcessor {

	private final SatEncoding bipolar = new BipolarSatEncoding();
	
	private final SatEncoding kBipolar = new KBipolarSatEncoding();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.tweety.arg.adf.reasoner.processor.StateProcessor#process(java.lang
	 * .Object, net.sf.tweety.arg.adf.syntax.AbstractDialecticalFramework)
	 */
	@Override
	public void process(SatSolverState state, PropositionalMapping encodingContext, AbstractDialecticalFramework adf) {
		bipolar.encode(state::add, encodingContext, adf);
		if (!adf.bipolar()) {
			kBipolar.encode(state::add, encodingContext, adf);
		}
	}

}
