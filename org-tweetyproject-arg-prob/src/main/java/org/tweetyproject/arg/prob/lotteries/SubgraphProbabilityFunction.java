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
package org.tweetyproject.arg.prob.lotteries;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.tweetyproject.arg.dung.divisions.Division;
import org.tweetyproject.arg.dung.ldo.syntax.LdoFormula;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.semantics.Semantics;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.commons.util.SetTools;
import org.tweetyproject.graphs.Graph;
import org.tweetyproject.math.probability.Probability;
import org.tweetyproject.math.probability.ProbabilityFunction;

/**
 * Represents a probability function on the subgraphs of some Dung theory.
 * 
 * @author Matthias Thimm
 */
public class SubgraphProbabilityFunction extends ProbabilityFunction<DungTheory>{

	/** The theory of this probability function.*/
	private DungTheory theory;
	
	/**
	 * Creates a new uniform probability function for the given theory, i.e.
	 * all sub graphs of the given theory receive the same probability.
	 * @param theory some theory.
	 */
	public SubgraphProbabilityFunction(DungTheory theory){
		super();
		this.theory = theory;
		Collection<Graph<Argument>> subtheories = theory.getSubgraphs();
		for(Graph<Argument> g: subtheories){
			this.put(new DungTheory(g), new Probability(1d/subtheories.size()));
		}
	}
	
	/**
	 * Returns the theory of this probability function.
	 * @return the theory of this probability function.
	 */
	public DungTheory getTheory(){
		return this.theory;
	}
	
	/**
	 * Returns the epistemic probability of the given argument, i.e. the probability
	 * that the given argument is present in some randomly sampled sub graph.
	 * @param arg some argument
	 * @return a probability
	 */
	public Probability getEpistemicProbability(Argument arg){
		double d = 0;
		for(DungTheory theory: this.keySet()){
			if(theory.contains(arg))
				d += this.probability(theory).doubleValue();
		}
		return new Probability(d);
	}
	
	/**
	 * Returns the epistemic probability of the given attack, i.e. the probability
	 * that the given attack is present in some randomly sampled sub graph.
	 * @param att some attack
	 * @return a probability
	 */
	@SuppressWarnings("unlikely-arg-type")
	public Probability getEpistemicProbability(Attack att){
		double d = 0;
		for(DungTheory theory: this.keySet()){
			if(theory.contains(att))
				d += this.probability(theory).doubleValue();
		}
		return new Probability(d);
	}
	
	/**
	 * Returns the probability of the given formula being acceptable wrt.
	 * the given semantics and this probability function, i.e. the sum
	 * of the probabilities of all sub-graphs that are dividers of the 
	 * given formula.  
	 * @param f some formula
	 * @param semantics some semantics.
	 * @return a probability
	 */
	public Probability getAcceptanceProbability(LdoFormula f, Semantics semantics){
		double p = 0;
		for(Graph<Argument> divider: f.getDividers(this.theory, semantics)){
			p += this.probability(new DungTheory(divider)).doubleValue();
		}
		return new Probability(p);		
	}
	
	/**
	 * Returns the probability of the given division being acceptable wrt.
	 * the given semantics and this probability function, i.e. the sum
	 * of the probabilities of all sub-graphs that are dividers of the 
	 * given division.  
	 * @param d some division
	 * @param semantics some semantics.
	 * @return a probability
	 */
	public Probability getAcceptanceProbability(Division d, Semantics semantics){
		double p = 0;
		for(Graph<Argument> divider: d.getDividers(this.theory, semantics)){
			p += this.probability(new DungTheory(divider)).doubleValue();
		}
		return new Probability(p);
	}
	
	/**
	 * Returns the probability of the given argument being acceptable wrt.
	 * the given semantics and this probability functions. This is equivalent
	 * to the probability of the division ({arg},{}).  
	 * @param arg some argument
	 * @param semantics some semantics.
	 * @return a probability
	 */
	public Probability getAcceptanceProbability(Argument arg, Semantics semantics){
		Extension a1 = new Extension();
		a1.add(arg);
		Division d = new Division(a1,new Extension());
		return this.getAcceptanceProbability(d, semantics);
	}
	
	/**
	 * Returns the probability of the given set of arguments being acceptable wrt.
	 * the given semantics and this probability functions. This is equivalent
	 * to the probability of the division (ext,{}).  
	 * @param ext some set of arguments
	 * @param semantics some semantics.
	 * @return a probability
	 */
	public Probability getAcceptanceProbability(Extension ext, Semantics semantics){
		return this.getAcceptanceProbability(new Division(ext,new Extension()), semantics);
	}
	
	/**
	 * Updates this probability function with the given extension, i.e.
	 * all theories that do not contain the given arguments get probability zero.
	 * Afterwards the function is normalized.
	 * @param e some extension
	 * @return the updated probability function
	 */
	public SubgraphProbabilityFunction naiveUpdate(Extension e){
		SubgraphProbabilityFunction func = new SubgraphProbabilityFunction(this.theory);
		for(DungTheory t: this.keySet())
			if(t.containsAll(e))
				func.put(t, this.probability(t));
			else func.put(t, new Probability(0d));
		func.normalize();
		return func;
	}
	
	/**
	 * Updates this probability function with the given theory using
	 * "simple redistribution", cf. [Hunter, Thimm, 2015].
	 * @param theory some abstract theory
	 * @return the updated probability function
	 */
	public SubgraphProbabilityFunction simpleUpdate(DungTheory theory){
		return this.stickyUpdate(theory, 1);
	}
	
	/**
	 * Updates this probability function with the given theory using
	 * "sticky redistribution", cf. [Hunter, Thimm, 2015].
	 * @param theory some abstract theory
	 * @param stickyCoefficient the sticky coefficient
	 * @return the updated probability function
	 */
	public SubgraphProbabilityFunction stickyUpdate(DungTheory theory, double stickyCoefficient){
		SubgraphProbabilityFunction func = new SubgraphProbabilityFunction(this.theory);
		double p;
		for(DungTheory t: this.keySet()){
			if(t.containsAll(theory)){
				p = 0;
				Collection<Graph<Argument>> subtheories = t.getSubgraphs();
				for(Graph<Argument> sub: subtheories){
					DungTheory subTheory = new DungTheory(sub);
					subTheory.add(theory);
					if(subTheory.equals(t)){
						p += this.probability(new DungTheory(sub)).doubleValue();
					}
				}				
				func.put(t, new Probability(stickyCoefficient * p + (1-stickyCoefficient) * this.probability(t).doubleValue()));
			}
			else func.put(t, new Probability(this.probability(t).doubleValue() * (1-stickyCoefficient)));
		}
		return func;
	}
	
	/**
	 * Updates this probability function with the given theory using
	 * "rough redistribution", cf. [Hunter, Thimm, 2015].
	 * @param theory some abstract theory
	 * @return the updated probability function
	 */
	public SubgraphProbabilityFunction roughUpdate(DungTheory theory){
		SubgraphProbabilityFunction func = new SubgraphProbabilityFunction(this.theory);
		double p;
		for(DungTheory t: this.keySet()){
			if(t.containsAll(theory)){
				p = 0;
				Collection<Graph<Argument>> subtheories = t.getSubgraphs();
				for(Graph<Argument> sub: subtheories){
					DungTheory subTheory = new DungTheory(sub);
					subTheory.add(theory);
					if(t.containsAll(subTheory)){
						Set<Attack> superGraphs = this.superGraphs(this.theory, subTheory, theory);
						SetTools<Attack> setTools = new SetTools<Attack>();
						Collection<Set<Attack>> subsets = setTools.subsets(superGraphs);
						for(Collection<Attack> subAttacks: subsets){
							DungTheory subsubTheory = new DungTheory();
							subsubTheory.add(subTheory);
							subsubTheory.addAllAttacks(subAttacks);
							if(subsubTheory.equals(t)){
								p += this.probability(new DungTheory(sub)).doubleValue() * 1/subsets.size();
							}
						}
					}
				}
				func.put(t, new Probability(p));
			}
			else func.put(t, new Probability(0d));
		}
		return func;
	}
	
	/**
	 * Computes Super(G,G???,Ci) = {(??,??) ??? Arcs(G) | (?? ??? Nodes(G???) and ?? ??? Nodes(Ci))
	 * 	or (?? ??? Nodes(Ci) and ?? ??? Nodes(G???))
     * 	or (?? ??? Nodes(Ci) and ?? ??? Nodes(Ci))
	 * @param g A Dung theory G
	 * @param gp A Dung theory G'
	 * @param c A Dung Theory Ci
	 * @return a set of attacks
	 */
	private Set<Attack> superGraphs(DungTheory g, DungTheory gp, DungTheory c){
		Set<Attack> attacks = new HashSet<Attack>();
		for(Attack at: g.getAttacks()){
			if(gp.contains(at.getAttacker())&& c.contains(at.getAttacked()) ||
					c.contains(at.getAttacker())&& gp.contains(at.getAttacked()) ||
					c.contains(at.getAttacker())&& c.contains(at.getAttacked()))
				attacks.add(at);
		}
		return attacks;
	}
}
