/******************************************************************************
 * This research was sponsored by the U.S. Army Research Laboratory and the
 * U.K. Ministry of Defence under the Biennial Program Plane 2013 (BPP13),
 * Project 6, Task 3: Collaborative Intelligence Analysis.
 * The U.S. and U.K. Governments are authorized to reproduce and distribute
 * reprints for Government purposes notwithstanding any copyright notation
 * hereon.
 * **************************************************************************
 *
 * This is a JSON converter to HashMaps
 *
 *
 * @author Alice Toniolo
 * @version 1.0
 * @since April 2014
 *
 */
package org.tweetyproject.arg.peaf.io.aif;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

public class AIFDBConverter {

    public static LinkedTreeMap convertToCIS(LinkedTreeMap graph) {
        JsonHelper jsh = new JsonHelper();

        LinkedTreeMap newGraph = new LinkedTreeMap();
        ArrayList nodes = (ArrayList) graph.get("nodes");
        newGraph.put("nodes", nodes);
        ArrayList<LinkedTreeMap> edges = (ArrayList<LinkedTreeMap>) graph.get("edges");
        ArrayList newedges = new ArrayList();

        for (LinkedTreeMap edge : edges) {
            String edg = jsh.convertInputJson(edge);
            edg = edg.replace("source", "fromID");
            edg = edg.replace("target", "toID");
            LinkedTreeMap newEdge = jsh.convertInputMapG(edg);
            newedges.add(newEdge);
        }
        newGraph.put("edges", newedges);
//        	String hl=jsh.convertInputJson(newGraph);
//        	System.out.println(hl);
        return newGraph;
    }
}
