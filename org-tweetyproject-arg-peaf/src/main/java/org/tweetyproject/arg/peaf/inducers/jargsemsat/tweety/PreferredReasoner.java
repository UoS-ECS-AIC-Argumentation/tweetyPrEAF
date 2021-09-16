package org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety;

import java.util.Collection;
import java.util.Vector;

import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.jargsemsat.alg.PreferredSemantics;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.jargsemsat.datastructures.DungAF;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.jargsemsat.datastructures.Encoding;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.jargsemsat.datastructures.Labelling;



public class PreferredReasoner
		extends org.tweetyproject.arg.dung.reasoner.SimplePreferredReasoner{

	@Override
	public Collection<Extension> getModels(DungTheory dungTheory) {
		DungAF af = DungTheoryToDungAF.fromDungTheory(dungTheory);

		Vector<Labelling> ret = new Vector<Labelling>();

		PreferredSemantics.extensions(ret, af, Encoding.defaultEncoding(), null,
				false);

		return DungTheoryToDungAF.DungAFToExtensions(dungTheory, ret);
	}

}
