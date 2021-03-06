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
package org.tweetyproject.logics.fol.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.commons.LogicalSymbols;
import org.tweetyproject.logics.commons.syntax.Constant;
import org.tweetyproject.logics.commons.syntax.Predicate;
import org.tweetyproject.logics.commons.syntax.Sort;
import org.tweetyproject.logics.fol.parser.FolParser;
import org.tweetyproject.logics.fol.syntax.FolFormula;
import org.tweetyproject.logics.fol.syntax.FolSignature;
import org.tweetyproject.logics.fol.syntax.Implication;
import org.tweetyproject.logics.fol.syntax.Contradiction;
import org.tweetyproject.logics.fol.syntax.ExclusiveDisjunction;
import org.tweetyproject.logics.fol.syntax.FolBeliefSet;
import org.tweetyproject.logics.fol.syntax.Tautology;
/**
 * JUnit Test class for FolParser.
 * 
 *  @author Anna Gessler
 */
public class FolParserTest {

	FolParser parser;
	public static final int DEFAULT_TIMEOUT = 5000;
	
	@Before
	public void initParser() {
		parser = new FolParser();
		FolSignature sig = new FolSignature(true); //Create new signature with equality
		Sort sortAnimal = new Sort("Animal");
		sig.add(sortAnimal); 
		Constant constantPenguin = new Constant("penguin",sortAnimal);
		Constant constantKiwi = new Constant("kiwi",sortAnimal);
		sig.add(constantPenguin);
		sig.add(constantKiwi);
		Predicate p1 = new Predicate("SunIsShining");
		sig.add(p1);
		List<Sort> predicateList = new ArrayList<Sort>();
		predicateList.add(sortAnimal);
		Predicate p2 = new Predicate("Flies",predicateList);
		sig.add(p2); 
		List<Sort> predicateList2 = new ArrayList<Sort>();
		predicateList2.add(sortAnimal);
		predicateList2.add(sortAnimal);
		Predicate p3 = new Predicate("Knows",predicateList2);
		sig.add(p3); 
		parser.setSignature(sig);
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void ParseForallQuantificationTest() throws ParserException, IOException {
		FolFormula f1 = (FolFormula)parser.parseFormula("forall X:(!Knows(kiwi,X) && SunIsShining)");
		FolSignature sig = f1.getSignature();
		
		assertTrue(f1.containsQuantifier());
		assertTrue(sig.containsSort("Animal"));
		assertTrue(sig.containsConstant("kiwi"));
		assertFalse(sig.containsConstant("penguin"));
		assertTrue(sig.containsPredicate("SunIsShining"));
		assertTrue(sig.containsPredicate("Knows"));
		assertFalse(sig.containsPredicate("Flies"));
		assertEquals(f1.getTerms().size(),2);
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void ParseExistsQuantificationTest() throws ParserException, IOException {
		FolFormula f1 = (FolFormula)parser.parseFormula("exists X:(!Knows(kiwi,X) && SunIsShining)");
		FolSignature sig = f1.getSignature();
		
		assertTrue(f1.containsQuantifier());
		assertTrue(sig.containsSort("Animal"));
		assertTrue(sig.containsConstant("kiwi"));
		assertFalse(sig.containsConstant("penguin"));
		assertTrue(sig.containsPredicate("Knows"));
		assertTrue(sig.containsPredicate("SunIsShining"));
		assertFalse(sig.containsPredicate("Flies"));
		assertEquals(f1.getTerms().size(),2);
	}
	
	@Test(timeout = DEFAULT_TIMEOUT) 
	public void EqualityPredicateTest() throws ParserException, IOException {
		FolFormula f1 = (FolFormula) parser.parseFormula("/==(kiwi,penguin)||(kiwi == penguin)");
		FolFormula f2 = (FolFormula) parser.parseFormula("exists X: (!Flies(X) => (X==kiwi))");
		
		assertTrue(f1.getSignature().containsPredicate(LogicalSymbols.EQUALITY()));
		assertTrue(f1.getSignature().containsPredicate(LogicalSymbols.INEQUALITY()));
		assertTrue(f1.getSignature().containsConstant("kiwi"));
		assertTrue(f1.getSignature().containsConstant("penguin"));
		assertTrue(f2.containsQuantifier());
		assertTrue(f2.getSignature().containsPredicate(LogicalSymbols.EQUALITY()));
		assertTrue(f2.getSignature().containsPredicate("Flies"));
		assertTrue(f2.getSignature().containsConstant("kiwi"));
	}
	
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void TautologyTest() throws ParserException, IOException {
		FolFormula f = (FolFormula) parser.parseFormula("+");
		Tautology t = new Tautology();
		assertTrue(f.equals(t));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void ContradictionTest() throws ParserException, IOException {
		FolFormula f = (FolFormula) parser.parseFormula("-");
		Contradiction c = new Contradiction();
		assertTrue(f.equals(c));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void ImplicationTest() throws ParserException, IOException {
		Implication f = (Implication) parser.parseFormula("SunIsShining=>SunIsShining");
		FolSignature sig = f.getSignature();
		assertTrue(sig.containsPredicate("SunIsShining"));
		assertFalse(f.isDnf());
		assertEquals(f.getFormulas().getFirst(),parser.parseFormula("SunIsShining"));
		assertEquals(f.getFormulas().getSecond(),parser.parseFormula("SunIsShining"));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void XorTest() throws ParserException, IOException {
		ExclusiveDisjunction f = (ExclusiveDisjunction) parser.parseFormula("SunIsShining ^^ Flies(kiwi)");
		assertFalse(f.isDnf());
		assertEquals(f.get(0),parser.parseFormula("SunIsShining"));
		assertEquals(f.get(1),parser.parseFormula("Flies(kiwi)"));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void NestedQuantifiedFormulaTest() throws ParserException, IOException {
		FolFormula f1 = (FolFormula)parser.parseFormula("exists X:(!Knows(kiwi,X)) && SunIsShining");
		FolFormula f2 = (FolFormula)parser.parseFormula("SunIsShining && forall X:(!Knows(kiwi,X))");
		FolFormula f3 = (FolFormula)parser.parseFormula("SunIsShining || forall X:(Flies(X)) && SunIsShining || SunIsShining && exists Y:(Knows(Y,kiwi))");
		FolFormula f4 = (FolFormula)parser.parseFormula("exists VARIABLE:(forall OtherVariable :(Knows(VARIABLE,OtherVariable)))");
		
		assertTrue(f1.containsQuantifier());
		assertTrue(f2.containsQuantifier());
		assertTrue(f3.containsQuantifier());
		assertTrue(f1.getSignature().containsPredicate("SunIsShining"));
		assertTrue(f1.getSignature().containsPredicate("Knows"));
		assertTrue(f2.getSignature().containsPredicate("SunIsShining"));
		assertTrue(f2.getSignature().containsPredicate("Knows"));
		assertTrue(f3.getSignature().containsPredicate("SunIsShining"));
		assertTrue(f3.getSignature().containsPredicate("Knows"));
		assertTrue(f3.getSignature().containsPredicate("Flies"));
		assertTrue(f4.getSignature().containsPredicate("Knows"));
	}
	
	@Test(timeout = DEFAULT_TIMEOUT)
	public void ParseBeliefBaseFromFileTest() throws ParserException, IOException {
		parser = new FolParser();
		FolBeliefSet beliefSet = new FolBeliefSet();
		beliefSet = parser.parseBeliefBaseFromFile("src/main/resources/examplebeliefbase.fologic");
		assertEquals(beliefSet.size(),6);
		
		FolSignature sig = (FolSignature) beliefSet.getMinimalSignature();
		assertEquals(sig.getConstants().size(),4);
		assertEquals(sig.getPredicates().size(),4);
		assertEquals(sig.getSorts().size(),2);
		}

	@Test(expected = ParserException.class,timeout = DEFAULT_TIMEOUT) 
	public void EmptyQuantificationTest() throws ParserException, IOException {
		parser.parseFormula("forall X:()");
	}
	
	@Test(expected = ParserException.class) 
	public void WrongArityTest() throws ParserException, IOException {
		parser.parseFormula("Flies(kiwi,X)");
	}	
}
