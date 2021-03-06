/* Generated By:JJTree&JavaCC: Do not edit this line. ASPParserTokenManager.java */
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
/* Generated By:JJTree: Do not edit this line. ASTOptFunc.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.tweetyproject.lp.asp.parser;

public class ASTOptFunc extends SimpleNode {
	public String func;
	public boolean maximize;

	public ASTOptFunc(int id) {
		super(id);
	}

	public ASTOptFunc(ASPParser p, int id) {
		super(p, id);
	}

	public void func(String f) {
		this.func = f;
	}

	/** Accept the visitor. **/
	public Object jjtAccept(ASPParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public void maximize(boolean b) {
		this.maximize = b;
	}
}
/*
 * JavaCC - OriginalChecksum=2e63ad8cc991694fac69c3adaf9f6a1a (do not edit this
 * line)
 */
