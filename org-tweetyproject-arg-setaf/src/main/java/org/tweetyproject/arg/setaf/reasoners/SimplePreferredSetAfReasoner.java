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
 *  Copyright 2016 The TweetyProject Team <http://tweetyproject.org/contact/>
 */
package org.tweetyproject.arg.setaf.reasoners;

import java.util.*;

import org.tweetyproject.arg.setaf.semantics.*;
import org.tweetyproject.arg.setaf.syntax.*;


/**
 * This reasoner for setaf theories performs inference on the preferred extensions.
 * Computes the set of all preferred extensions, i.e., all maximal admissible sets.
 * It does so by first computing all complete extensions and then check for
 * set maximality.
 * 
 * @author Matthias Thimm, Sebastian Franke
 *
 */
public class SimplePreferredSetAfReasoner extends AbstractExtensionSetAfReasoner {	

	/* (non-Javadoc)
	 * @see org.tweetyproject.arg.setaf.reasoner.AbstractExtensionReasoner#getModels(org.tweetyproject.arg.setaf.syntax.DungTheory)
	 */
	@Override
	public Collection<SetAfExtension> getModels(SetAf bbase) {
		Collection<SetAfExtension> completeExtensions = new SimpleCompleteSetAfReasoner().getModels(bbase);
		Set<SetAfExtension> result = new HashSet<SetAfExtension>();
		boolean maximal;
		for(SetAfExtension e1: completeExtensions){
			maximal = true;
			for(SetAfExtension e2: completeExtensions)
				if(e1 != e2 && e2.containsAll(e1)){
					maximal = false;
					break;
				}
			if(maximal)
				result.add(e1);			
		}		
		return result;
	}

	/* (non-Javadoc)
	 * @see org.tweetyproject.arg.setaf.reasoner.AbstractExtensionReasoner#getModel(org.tweetyproject.arg.setaf.syntax.DungTheory)
	 */
	@Override
	public SetAfExtension getModel(SetAf bbase) {
		// just return the first found preferred extension
		Collection<SetAfExtension> completeExtensions = new SimpleCompleteSetAfReasoner().getModels(bbase);
		boolean maximal;
		for(SetAfExtension e1: completeExtensions){
			maximal = true;
			for(SetAfExtension e2: completeExtensions)
				if(e1 != e2 && e2.containsAll(e1)){
					maximal = false;
					break;
				}
			if(maximal)
				return e1;			
		}		
		// this should not happen
		throw new RuntimeException("Hmm, did not find a maximal set in a finite number of sets. Should not happen.");
	}
}
