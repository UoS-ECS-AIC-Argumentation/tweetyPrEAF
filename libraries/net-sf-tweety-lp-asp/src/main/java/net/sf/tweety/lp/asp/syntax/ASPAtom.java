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
 *  Copyright 2018 The TweetyProject Team <http://tweetyproject.org/contact/>
 */
package net.sf.tweety.lp.asp.syntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.tweety.logics.commons.error.LanguageException;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.commons.syntax.interfaces.ComplexLogicalFormula;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolSignature;

/**
 * This class models an atom, which is a basic structure for building literals
 * and rules for logic programs.
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 * @author Matthias Thimm
 * @author Anna Gessler
 *
 */
public class ASPAtom implements ASPLiteral {

	/** The predicate identifying the atom */
	protected Predicate predicate;

	/** A list of arguments of the atom */
	protected List<Term<?>> arguments = new LinkedList<Term<?>>();

	/**
	 * Empty constructor.
	 */
	public ASPAtom() {
		this.predicate = null;
		this.arguments = new ArrayList<Term<?>>();
	}

	/**
	 * Creates a new atom with the given predicate and terms.
	 * 
	 * @param p
	 *            a predicate
	 * @param terms
	 *            arguments of the atom
	 */
	public ASPAtom(Predicate p, List<Term<?>> terms) {
		this.predicate = p;
		this.arguments = terms;
	}

	/**
	 * Creates a new atom with the given predicate name and no terms.
	 * 
	 * @param name
	 */
	public ASPAtom(String name) {
		this(new Predicate(name));
	}

	/**
	 * Copy-Constructor: Generates a deep copy of the given FOL atom.
	 * 
	 * @param other
	 *            The FOL atom acting as source for the deep copy
	 */
	public ASPAtom(FOLAtom other) {
		this.predicate = new Predicate(other.getPredicate().getName(), other.getArguments().size());
		for (Term<?> t : other.getArguments())
			this.arguments.add((Term<?>) t.clone());
	}

	/**
	 * Copy-Constructor: Generates a deep copy of the given atom.
	 * 
	 * @param other
	 *            The atom acting as source for the deep copy
	 */
	public ASPAtom(ASPAtom other) {
		this.predicate = new Predicate(other.getName(), other.getPredicate().getArity());
		for (Term<?> t : other.getArguments()) {
			this.arguments.add((Term<?>) t.clone());
		}
	}

	/**
	 * Creates an atom with the given predicate as name and the given terms as
	 * argument
	 * 
	 * @param symbol
	 *            The name of the atom
	 * @param terms
	 *            A list of Term<?> defining the arguments of the term
	 */
	public ASPAtom(String symbol, Term<?>... terms) {
		this.predicate = new Predicate(symbol, terms.length);
		for (int i = 0; i < terms.length; ++i) {
			this.arguments.add(terms[i]);
		}
	}

	public ASPAtom(Predicate p) {
		this();
		this.predicate = p;
	}

	@Override
	public String getName() {
		return predicate.getName();
	}

	@Override
	public String toString() {
		String res = this.predicate.getName();
		if (this.predicate.getArity() > 0) {
			res += "(";
			for (int i = 0; i < arguments.size()-1; i++) {
				res += arguments.get(i).toString() + ",";
			}
			res += arguments.get(arguments.size()-1).toString() + ")";
		}
		return res;
	}

	@Override
	public SortedSet<ASPLiteral> getLiterals() {
		SortedSet<ASPLiteral> literals = new TreeSet<ASPLiteral>();
		literals.add(this);
		return literals;
	}

	@Override
	public Set<Predicate> getPredicates() {
		Set<Predicate> predicates = new HashSet<Predicate>();
		predicates.add(this.predicate);
		return predicates;
	}

	@Override
	public Set<ASPAtom> getAtoms() {
		Set<ASPAtom> atoms = new HashSet<ASPAtom>();
		atoms.add(this);
		return atoms;
	}

	@Override
	public FolSignature getSignature() {
		FolSignature sig = new FolSignature();
		sig.add(this);
		return sig;
	}

	@Override
	public ASPAtom clone() {
		return new ASPAtom(this);
	}

	@Override
	public ComplexLogicalFormula substitute(Map<? extends Term<?>, ? extends Term<?>> map)
			throws IllegalArgumentException {
		// TODO
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public ComplexLogicalFormula exchange(Term<?> v, Term<?> t) throws IllegalArgumentException {
		// TODO
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public boolean isGround() {
		if (arguments == null)
			return true;

		for (Term<?> t : arguments) {
			if (t instanceof Variable)
				return false;
			else if (t instanceof ASPAtom) {
				return ((ASPAtom) t).isGround();
			}
		}
		return true;
	}

	@Override
	public boolean isWellFormed() {
		// TODO
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public Class<? extends Predicate> getPredicateCls() {
		return this.predicate.getClass();
	}

	@Override
	public Set<Term<?>> getTerms() {
		Set<Term<?>> terms = new HashSet<Term<?>>();
		terms.addAll(arguments);
		return terms;
	}

	@Override
	public <C extends Term<?>> Set<C> getTerms(Class<C> cls) {
		Set<C> reval = new HashSet<C>();
		for (Term<?> arg : arguments) {
			if (arg.getClass().equals(cls)) {
				@SuppressWarnings("unchecked")
				C castArg = (C) arg;
				reval.add(castArg);
			}
			// Recursively add terms for all inner functional terms
			reval.addAll(arg.getTerms(cls));
		}
		return reval;
	}

	@Override
	public <C extends Term<?>> boolean containsTermsOfType(Class<C> cls) {
		for (Term<?> t : arguments)
			if (t.containsTermsOfType(cls))
				return true;
		return false;
	}

	@Override
	public Predicate getPredicate() {
		return this.predicate;
	}

	@Override
	public RETURN_SET_PREDICATE setPredicate(Predicate newer) {
		Predicate old = this.predicate;
		this.predicate = newer;
		return AtomImpl.implSetPredicate(old, this.predicate, arguments);
	}

	@Override
	public void addArgument(Term<?> arg) throws LanguageException {
		this.arguments.add(arg);
	}

	@Override
	public List<? extends Term<?>> getArguments() {
		return Collections.unmodifiableList(arguments);
	}

	@Override
	public boolean isComplete() {
		return getTerms().size() == predicate.getArity();
	}

	@Override
	public int compareTo(ASPLiteral o) {
		if (o instanceof StrictNegation) {
			return -1;
		}
		return toString().compareTo(o.toString());
	}

	@Override
	public ASPAtom cloneWithAddedTerm(Term<?> term) {
		ASPAtom reval = (ASPAtom) this.clone();
		reval.predicate.setArity(reval.predicate.getArity() + 1);
		reval.arguments.add(term);
		return reval;
	}

	@Override
	public ASPAtom getAtom() {
		return this;
	}

	@Override
	public ASPLiteral complement() {
		return new StrictNegation(this);
	}

	@Override
	public ASPAtom substitute(Term<?> v, Term<?> t) throws IllegalArgumentException {
		ASPAtom reval = new ASPAtom(this);
		reval.arguments.clear();
		for (int i = 0; i < arguments.size(); i++) {
			if (arguments.get(i).equals(v)) {
				reval.arguments.add(t);
			} else {
				reval.arguments.add(arguments.get(i));
			}
		}
		return reval;
	}

	public Term<?> getTerm(int i) {
		return this.arguments.get(i);
	}

}