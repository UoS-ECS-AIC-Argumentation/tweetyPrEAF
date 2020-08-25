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
package net.sf.tweety.arg.adf.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.arg.adf.semantics.interpretation.Interpretation;
import net.sf.tweety.arg.adf.syntax.Argument;

/**
 * This is a monotone collection, hence you can only add elements to it, but not remove them.
 * 
 * @author Mathias Hofer
 *
 */
public final class InterpretationTrieSet extends AbstractSet<Interpretation> {

	private Argument[] order;
	
	private final Node root = new InnerNode();
	
	private int size = 0;
	
	public InterpretationTrieSet() {}
	
	public InterpretationTrieSet(List<Interpretation> interpretations) {
		addAll(interpretations);
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<Interpretation> iterator() {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 */
	@Override
	public boolean add(Interpretation e) {
		if (this.order == null) {
			this.order = arguments(e);
		}
		root.add(e, orderedDecided(e, order));
		size++;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return size;
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractSet#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		if (o instanceof Interpretation && size > 0) {
			Interpretation interpretation = (Interpretation) o;
			return root.contains(interpretation, orderedDecided(interpretation, order));
		}
		return false;
	}
	
	public static List<Argument> orderedDecided(Interpretation interpretation, Argument[] order) {
		if (interpretation.numDecided() <= 0) {
			throw new IllegalArgumentException("Interpretation must have decided arguments!");
		}
		List<Argument> orderedDecided = new LinkedList<>();
		for (Argument arg : order) {
			if (!interpretation.undecided(arg)) {
				orderedDecided.add(arg);
			}
		}		
		return orderedDecided;
	}
	
	/**
	 * Creates an array of the arguments in <code>interpretation</code> but
	 * orders them s.t. the undecided ones are at the end.
	 * 
	 * @param interpretation
	 * @return the arguments as an array and in a more efficient order
	 */
	private static Argument[] arguments(Interpretation interpretation) {
		Argument[] arguments = new Argument[interpretation.size()];
		int index = 0;
		for (Argument a : interpretation.satisfied()) {
			arguments[index++] = a;
		}
		for (Argument a : interpretation.unsatisfied()) {
			arguments[index++] = a;
		}
		for (Argument a : interpretation.undecided()) {
			arguments[index++] = a;
		}
		return arguments;
	}
	
	private interface Node {
		
		void add(Interpretation interpretation, List<Argument> orderedDecided);
		
		boolean contains(Interpretation interpretation, List<Argument> orderedDecided);
	}
	
	private static final class InnerNode implements Node {
		
		private final Map<Argument, Node> trueNodes = new HashMap<>();
		
		private final Map<Argument, Node> falseNodes = new HashMap<>();
		
		public void add(Interpretation interpretation, List<Argument> orderedDecided) {
			Argument decided = orderedDecided.get(0);
			if (orderedDecided.size() > 1) {
				Node next = null;
				if (interpretation.satisfied(decided)) {
					next = trueNodes.computeIfAbsent(decided, a -> new InnerNode());
				} else {
					next = falseNodes.computeIfAbsent(decided, a -> new InnerNode());
				}
				next.add(interpretation, orderedDecided.subList(1, orderedDecided.size()));
			} else {
				if (interpretation.satisfied(decided)) {
					trueNodes.put(decided, LeafNode.INSTANCE);
				} else {
					falseNodes.put(decided, LeafNode.INSTANCE);
				}
			}
		}
		
		public boolean contains(Interpretation interpretation, List<Argument> orderedDecided) {
			Argument decided = orderedDecided.get(0);
			Node next = null;
			if (interpretation.satisfied(decided)) {
				next = trueNodes.get(decided);
			} else {
				next = falseNodes.get(decided);
			}
			
			if (next == null) {
				return true;
			} else if (orderedDecided.size() > 1) {
				return next.contains(interpretation, orderedDecided.subList(1, orderedDecided.size()));
			}
			return true;
		}
		
		
	}
	
	private static enum LeafNode implements Node {
		INSTANCE;

		/* (non-Javadoc)
		 * @see net.sf.tweety.arg.adf.util.InterpretationTrieSet.Node#add(net.sf.tweety.arg.adf.semantics.interpretation.Interpretation, java.util.List)
		 */
		@Override
		public void add(Interpretation interpretation, List<Argument> orderedDecided) {}

		/* (non-Javadoc)
		 * @see net.sf.tweety.arg.adf.util.InterpretationTrieSet.Node#contains(net.sf.tweety.arg.adf.semantics.interpretation.Interpretation, java.util.List)
		 */
		@Override
		public boolean contains(Interpretation interpretation, List<Argument> orderedDecided) {
			return true;
		}
		
	}
	
}
