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
package net.sf.tweety.arg.adf.sat;

import java.util.Collection;
import java.util.LinkedList;

import net.sf.tweety.commons.Interpretation;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.syntax.Disjunction;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.PlBeliefSet;
import net.sf.tweety.logics.pl.syntax.PlFormula;
import net.sf.tweety.logics.pl.syntax.Proposition;

/**
 * A dummy state that can be used in combination with non-incremental
 * Sat-Solvers at positions where a SatSolverState is required.
 * <p>
 * Maintains an internal collection of disjunctions.
 * 
 * @author Mathias Hofer
 *
 */
public class SimpleSatSolverState implements SatSolverState {

	private SatSolver satSolver;

	private Collection<PlFormula> state;
	
	private Collection<Disjunction> assume;

	/**
	 * @param satSolver
	 */
	public SimpleSatSolverState(SatSolver satSolver) {
		this.satSolver = satSolver;
		this.state = new LinkedList<PlFormula>();
		this.assume = new LinkedList<Disjunction>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.tweety.arg.adf.sat.SatSolverState#witness()
	 */
	@Override
	public Interpretation<PlBeliefSet, PlFormula> witness() {
		if (!assume.isEmpty()) {
			Collection<PlFormula> temporaryState = new LinkedList<PlFormula>();
			temporaryState.addAll(state);
			temporaryState.addAll(assume);
			assume.clear();
			return satSolver.getWitness(temporaryState);
		}
		return satSolver.getWitness(state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.tweety.arg.adf.sat.SatSolverState#assume(net.sf.tweety.logics.pl.
	 * syntax.Proposition, boolean)
	 */
	@Override
	public void assume(Proposition proposition, boolean value) {
		Disjunction clause = new Disjunction();
		if (value) {
			clause.add(proposition);
		} else {
			clause.add(new Negation(proposition));
		}
		assume.add(clause);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.tweety.arg.adf.sat.SatSolverState#add(net.sf.tweety.logics.pl.
	 * syntax.Disjunction)
	 */
	@Override
	public boolean add(Disjunction clause) {
		return state.add(clause);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.tweety.arg.adf.sat.SatSolverState#add(java.util.Collection)
	 */
	@Override
	public boolean add(Collection<Disjunction> clauses) {
		return state.addAll(clauses);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.tweety.arg.adf.sat.SatSolverState#remove(net.sf.tweety.logics.pl.
	 * syntax.Disjunction)
	 */
	@Override
	public boolean remove(Disjunction clause) {
		return state.remove(clause);
	}

}