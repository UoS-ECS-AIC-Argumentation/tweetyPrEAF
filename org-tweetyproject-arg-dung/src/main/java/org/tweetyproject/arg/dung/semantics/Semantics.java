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
 package org.tweetyproject.arg.dung.semantics;

/**
 * This enum lists all semantics.
 * @author Matthias Thimm
 */
public enum Semantics {
	CF ("conflict-free semantics", "CF"),
	ADM ("admissible semantics", "ADM"),
	WAD ("weakly-admissible semantics", "WAD"),
	CO ("complete semantics", "CO"),
	GR ("grounded semantics", "GR"),
	PR ("preferred semantics", "PR"),
	ST ("stable semantics", "ST"),
	STG ("stage semantics", "STG"),
	STG2 ("stage2 semantics", "STG2"),
	SST ("semi-stable semantics", "SST"),
	ID ("ideal semantics", "ID"),
	EA ("eager semantics", "EA"),
	CF2 ("CF2 semantics", "CF2"),
	SCF2 ("SCF2 semantics", "SCF2"),
	N ("Naive semantics", "N"),
	diverse ("diverse semantics", "div");
	
	public static final Semantics GROUNDED_SEMANTICS = GR,
		STABLE_SEMANTICS = ST,
		PREFERRED_SEMANTICS = PR,
		COMPLETE_SEMANTICS = CO,
		ADMISSIBLE_SEMANTICS = ADM,
		WEAKLY_ADMISSIBLE_SEMANTICS = WAD,
		CONFLICTFREE_SEMANTICS = CF,
		SEMISTABLE_SEMANTICS = SST,
		IDEAL_SEMANTICS = ID,
		EAGER_SEMANTICS = EA,
		STAGE_SEMANTICS = STG,
		STAGE2_SEMANTICS = STG2,
		CF2_SEMANTICS = CF2,
		SCF2_SEMANTICS = SCF2,
		NAIVE_SEMANTICS = N;
		
	/** The description of the semantics. */
	private String description;
	/** The abbreviation of the semantics. */
	private String abbreviation;
	
	/**
	 * Creates a new semantics.
	 * @param description some description
	 * @param abbreviation an abbreviation
	 */
	private Semantics(String description, String abbreviation){
		this.description = description;
		this.abbreviation = abbreviation;
	}
	
	/**
	 * Returns the description of the semantics.
	 * @return the description of the semantics.
	 */
	public String description(){
		return this.description;
	}
	
	/**
	 * Returns the abbreviation of the semantics.
	 * @return the abbreviation of the semantics.
	 */
	public String abbreviation(){
		return this.abbreviation;
	}
}
