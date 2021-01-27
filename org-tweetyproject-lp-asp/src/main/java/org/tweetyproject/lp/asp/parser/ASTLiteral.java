/* Generated By:JJTree&JavaCC: Do not edit this line. ASPCore2ParserTokenManager.java */
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
/* Generated By:JJTree: Do not edit this line. ASTAtom.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.tweetyproject.lp.asp.parser;

public class ASTLiteral extends SimpleNode {
	protected boolean neg;
	public String name;

	public ASTLiteral(int id) {
		super(id);
	}

	public ASTLiteral(ASPCore2Parser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(ASPCore2ParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public void neg(boolean b) {
		this.neg = b;
	}
	
	public void name(String n) {
		this.name = n;
	}
}
/*
 * JavaCC - OriginalChecksum=b6e43a9d164ef598138628698fbb9360 (do not edit this
 * line)
 */