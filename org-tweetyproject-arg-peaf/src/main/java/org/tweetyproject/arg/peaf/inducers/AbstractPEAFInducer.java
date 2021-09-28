package org.tweetyproject.arg.peaf.inducers;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.tweetyproject.arg.peaf.syntax.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;

public abstract class AbstractPEAFInducer implements PEAFInducer {

    protected PEAFTheory peafTheory;

    public AbstractPEAFInducer(PEAFTheory peafTheory) {
        this.peafTheory = peafTheory;

        if (isSupportLinksCyclic()){
            throw new CyclicException("This PEAF can not be induced, because a cyclic was found.");
        }
    }

    private boolean isSupportLinksCyclic() {
        // Adapted from https://www.geeksforgeeks.org/detect-cycle-in-a-graph/
        // Mark all the vertices as not visited and
        // not part of recursion stack

        Map<EArgument, Boolean> visited = Maps.newHashMap();
        Map<EArgument, Boolean>  recStack = Maps.newHashMap();
        for (EArgument argument : peafTheory.getArguments()) {
            visited.put(argument, false);
            recStack.put(argument, false);
        }

        // Call the recursive helper function to
        // detect cycle in different DFS trees
        for (EArgument arg : this.peafTheory.getArguments()) {
            if (isCyclicUtil(arg, visited, recStack))
                return true;
        }

        return false;
    }

    public void induceNTimes(Consumer<InducibleEAF> consumer, int n) {
        while (n > 0 ) {
            this.induce(consumer);
            n--;
        }
    }

    public PEAFTheory getPeafTheory() {
        return peafTheory;
    }

    // This function is a variation of DFSUtil() in
    // https://www.geeksforgeeks.org/archives/18212
    private boolean isCyclicUtil(EArgument arg, Map<EArgument, Boolean> visited,
                                 Map<EArgument, Boolean> recStack)
    {
        // Mark the current node as visited and
        // part of recursion stack
        if (recStack.get(arg))
            return true;

        if (visited.get(arg))
            return false;

        visited.put(arg, true);
        recStack.put(arg, true);
        Set<ESupport> set = arg.getSupports();

        for (ESupport c : set) {
            for (EArgument to : c.getTos()) {
                if (isCyclicUtil(to, visited, recStack))
                    return true;
            }
        }
        recStack.put(arg, false);

        return false;
    }
}
