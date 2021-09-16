package org.tweetyproject.arg.peaf.inducers.jargsemsat.jargsemsat.alg;

import java.util.Iterator;
import java.util.Vector;

import org.tweetyproject.arg.peaf.inducers.jargsemsat.jargsemsat.datastructures.DungAF;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.jargsemsat.datastructures.Encoding;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.jargsemsat.datastructures.Labelling;

public class SemiStableSemantics extends CompleteSemantics {

	public static boolean extensions(Vector<Labelling> extensions, DungAF af, Encoding enc, String arg, boolean firstonly)
	{
		Vector<Labelling> preferred = new Vector<Labelling>();
		
		PreferredSemantics.extensions(preferred, af, enc, null, false);
		
		for (Iterator<Labelling> extIter = preferred.iterator(); extIter.hasNext();)
		{
			Labelling ext = extIter.next();
			boolean toInsert = true;
			for (Iterator<Labelling> intIter = preferred.iterator(); intIter.hasNext();)
			{
				Labelling internal = intIter.next();
				if (!internal.equals(ext) && ext.undecargs().containsAll(internal.undecargs()))
					toInsert = false;
			}
			
			if (toInsert){
				extensions.addElement(ext);
				
				if (firstonly)
					return true;
			}
		}
		
		if (arg != null)
		{
			for (Iterator<Labelling> it = extensions.iterator(); it.hasNext();){
				if (!it.next().getExtension().contains(arg))
					return false;
			}
			return true;
		}
		
		return true;
	}
	
	public static boolean credulousAcceptance(String arg, DungAF af, Encoding enc)
	{
		Vector<Labelling> extensions = new Vector<Labelling>();
		SemiStableSemantics.extensions(extensions, af, enc, null, false);
		
		for (Iterator<Labelling> l = extensions.iterator(); l.hasNext();)
		{
			if (l.next().inargs().contains(arg))
				return true;
		}
		
		return false;
	}
	
	public static boolean skepticalAcceptance(String arg, DungAF af, Encoding enc)
	{
		return extensions(null, af, enc, arg, false);
	}

	public static boolean someExtension(Labelling ret, DungAF af, Encoding enc)
	{
		Vector<Labelling> res = new Vector<Labelling>();
		boolean val = extensions(res, af, enc, null, true);
		ret.copyFrom(res.firstElement());
		return val;
		
	}
	
}
