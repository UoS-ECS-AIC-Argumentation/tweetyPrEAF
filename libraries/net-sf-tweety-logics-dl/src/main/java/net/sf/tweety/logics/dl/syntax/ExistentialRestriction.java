/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
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
 *  Copyright 2018 The Tweety Project Team <http://tweetyproject.org/contact/>
 */
package net.sf.tweety.logics.dl.syntax;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.commons.util.Pair;
import net.sf.tweety.logics.commons.syntax.Predicate;

/**
 * This class models an existential restriction in description logics, 
 * i.e. an expression of the form "exists R.C" for a role R and a Concept C.
 * 
 * @author Anna Gessler
 *
 */
public class ExistentialRestriction extends ComplexConcept  {
	
	/**
	 * The role and the concept that is being restricted by it.
	 */
	private Pair<ComplexConcept,AtomicRole> formulas;

	/**
	 * Creates a new ALC existential restriction with the given role
	 * and concept.
	 * 
	 * @param r the role
	 * @param c the concept that is being restricted by the role
	 */
	public ExistentialRestriction(ComplexConcept c, AtomicRole r) {
		formulas.setSecond(r);
		formulas.setFirst(c);
	}

	public ExistentialRestriction(Pair<ComplexConcept, AtomicRole> f) {
		formulas.setSecond(f.getSecond());
		formulas.setFirst(f.getFirst());
	}

	/**
	 * Get the role and concept that are part of the existential restriction.
	 * @return an atomic role and a concept
	 */
	public Pair<ComplexConcept,AtomicRole> getFormulas() {
	 return this.formulas;	
	}

	@Override
	public DlSignature getSignature() {
		DlSignature sig = new DlSignature();
		sig.add(this.formulas.getFirst());
		sig.add(this.formulas.getSecond());
		return sig;
	}
	
	public String toString() {
		return "(exists " + " " + this.formulas.getFirst().toString() + " " + this.getFormulas().getSecond().toString() +")";
	}

	@Override
	public Set<Predicate> getPredicates() {
		Set<Predicate> ps = new HashSet<Predicate>();
		ps.addAll(formulas.getFirst().getPredicates());
		ps.addAll(formulas.getSecond().getPredicates());
		return ps;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public ComplexConcept clone() {
		return new ExistentialRestriction(this.getFormulas());
	}

	@Override
	public ComplexConcept collapseAssociativeFormulas() {
		 return new ExistentialRestriction(formulas.getFirst().collapseAssociativeFormulas(),formulas.getSecond().collapseAssociativeFormulas());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formulas.getFirst() == null) ? 0 : formulas.getFirst().hashCode());
		result = prime * result
				+ ((formulas.getSecond() == null) ? 0 : formulas.getSecond().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExistentialRestriction other = (ExistentialRestriction) obj;
		if (this.getFormulas()== null) {
			if (other.getFormulas() != null)
				return false;
		} else if (!this.getFormulas().equals(other.getFormulas()))
			return false;
		return true;
	}
	
}
