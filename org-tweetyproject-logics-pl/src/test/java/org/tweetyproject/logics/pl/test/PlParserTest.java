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
package org.tweetyproject.logics.pl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import org.tweetyproject.commons.ParserException;
import org.tweetyproject.commons.Signature;
import org.tweetyproject.logics.pl.parser.PlParser;
import org.tweetyproject.logics.pl.syntax.Conjunction;
import org.tweetyproject.logics.pl.syntax.Contradiction;
import org.tweetyproject.logics.pl.syntax.Disjunction;
import org.tweetyproject.logics.pl.syntax.Equivalence;
import org.tweetyproject.logics.pl.syntax.ExclusiveDisjunction;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.Proposition;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.PlSignature;
import org.tweetyproject.logics.pl.syntax.Tautology;

/**
 * JUnit Test class for PlParser.
 * 
 *  @author Matthias Thimm
 *  @author Anna Gessler
 */

public class PlParserTest {
	PlParser parser;
	public static final int DEFAULT_TIMEOUT = 2000;
	
	@Before
	public void initParser() throws ParserException, IOException {
		parser = new PlParser();	
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void PropositionTest() throws ParserException, IOException {
		PlFormula f = (PlFormula) parser.parseFormula("a");
		Proposition p = new Proposition("a");
		assertTrue(f.equals(p));
		
		f = (PlFormula) parser.parseFormula("abba");
		p = new Proposition("abba");
		assertTrue(f.equals(p));
		
		f = (PlFormula) parser.parseFormula("(a)");
		p = new Proposition("a");
		assertTrue(f.equals(p));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void NegationTest() throws ParserException, IOException {
		PlFormula f = (PlFormula) parser.parseFormula("!a");
		Negation n = new Negation(new Proposition("a"));
		assertTrue(f.equals(n));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void ImplicationTest() throws ParserException, IOException {
		PlFormula f = (PlFormula) parser.parseFormula("a => b");
		Implication d = new Implication(new Proposition("a"), new Proposition("b"));
		assertTrue(f.equals(d));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void EquivalenceTest() throws ParserException, IOException {
		PlFormula f = (PlFormula) parser.parseFormula("a <=> b");
		Equivalence d = new Equivalence(new Proposition("a"), new Proposition("b"));
		assertTrue(f.equals(d));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void DisjunctionTest() throws ParserException, IOException {
		PlFormula f = (PlFormula) parser.parseFormula("a || b");
		Disjunction d = new Disjunction(new Proposition("a"), new Proposition("b"));
		assertTrue(f.equals(d));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void ConjunctionTest() throws ParserException, IOException {
		PlFormula f = (PlFormula) parser.parseFormula("a && b");
		Conjunction c = new Conjunction(new Proposition("a"), new Proposition("b"));
		assertTrue(f.equals(c));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void ExclusiveDisjunctionTest() throws ParserException, IOException {
		PlFormula f = (PlFormula) parser.parseFormula("a ^^ b");
		ExclusiveDisjunction c = new ExclusiveDisjunction(new Proposition("a"), new Proposition("b"));
		assertTrue(f.equals(c));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void TautologyTest() throws ParserException, IOException {
		PlFormula f = (PlFormula) parser.parseFormula("+");
		Tautology t = new Tautology();
		assertTrue(f.equals(t));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void ContradictionTest() throws ParserException, IOException {
		PlFormula f = (PlFormula) parser.parseFormula("-");
		Contradiction c = new Contradiction();
		assertTrue(f.equals(c));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void NestedFormulaTest() throws ParserException, IOException {
		PlFormula f = parser.parseFormula("!((a && b) || !c || ((c => !d) <=> c))");
		PlSignature sig = new PlSignature();
		sig.add(new Proposition("a"));
		sig.add(new Proposition("b"));
		sig.add(new Proposition("c"));
		sig.add(new Proposition("d"));
		assertTrue(f instanceof Negation);
		assertTrue(((Negation)f).getFormula() instanceof Disjunction);
		assertTrue(f.getSignature().equals(sig));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void SpecialCharactersTest() throws ParserException, IOException {
		PlFormula f = (PlFormula) parser.parseFormula("a && ?? || !(!@$$f)");
		PlSignature sig = f.getSignature();
		PlSignature sig2 = new PlSignature();
		sig2.add(new Proposition("a"));
		sig2.add(new Proposition("??"));
		sig2.add(new Proposition("@$$f"));
		assertEquals(sig,sig2);
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void ParseBeliefBaseTest() throws ParserException, IOException {
		PlBeliefSet beliefSet = new PlBeliefSet();
		beliefSet = parser.parseBeliefBase("a || b || c \n !a || b \n !b || c \n !c || (!a && !b && !c && !d)");
		assertEquals(beliefSet.size(),4);
		assertEquals(beliefSet.getSignature().size(),4);
		
		Signature sig = beliefSet.getMinimalSignature();
		PlSignature sig2 = new PlSignature();
		sig2.add(new Proposition("a"));
		sig2.add(new Proposition("b"));
		sig2.add(new Proposition("c"));
		sig2.add(new Proposition("d"));
		assertEquals(sig,sig2);
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void ParseBeliefBaseFromFileTest() throws ParserException, IOException {
		PlBeliefSet beliefSet = new PlBeliefSet();
		beliefSet = parser.parseBeliefBaseFromFile("src/main/resources/examplebeliefbase.proplogic");
		assertEquals(beliefSet.size(),4);
		
		Signature sig = beliefSet.getMinimalSignature();
		PlSignature sig2 = new PlSignature();
		sig2.add(new Proposition("a"));
		sig2.add(new Proposition("b"));
		sig2.add(new Proposition("c"));
		sig2.add(new Proposition("d"));
		assertEquals(sig,sig2);
	}
	
	@Test(expected = ParserException.class, timeout = DEFAULT_TIMEOUT) 
	public void EmptyDisjunctionTest() throws ParserException, IOException {
		parser.parseFormula(" || ");
	}
	
	@Test(expected = ParserException.class, timeout = DEFAULT_TIMEOUT)  
	public void EmptyConjunctionTest() throws ParserException, IOException {
		parser.parseFormula(" && ");
	}
	
	@Test(expected = ParserException.class, timeout = DEFAULT_TIMEOUT)  
	public void IncompleteConjunctionTest() throws ParserException, IOException {
		parser.parseFormula("a && ");
	}
	
	@Test(expected = ParserException.class, timeout = DEFAULT_TIMEOUT) 
	public void IncompleteDisjunctionTest() throws ParserException, IOException {
		parser.parseFormula(" || a");
	}
	
	@Test(expected = ParserException.class, timeout = DEFAULT_TIMEOUT) 
	public void IllegalCharacterTest() throws ParserException, IOException{
		parser.parseFormula(" ");
	}
	
	@Test(expected = ParserException.class, timeout = DEFAULT_TIMEOUT)  
	public void BrokenParenthesesTest() throws ParserException, IOException {
		parser.parseFormula("( a");
	}
	
	@Test(expected = ParserException.class, timeout = DEFAULT_TIMEOUT)  
	public void BrokenParenthesesTest2() throws ParserException, IOException {
		parser.parseFormula(" a )");
	}
	
	@Test(expected = ParserException.class, timeout = DEFAULT_TIMEOUT)  
	public void BrokenParenthesesTest3() throws ParserException, IOException {
		parser.parseFormula(") a (");
	}
	
	@Test(expected = ParserException.class, timeout = DEFAULT_TIMEOUT)  
	public void EmptyParenthesesTest() throws ParserException, IOException {
		parser.parseFormula("a || () || b");
	}
}
