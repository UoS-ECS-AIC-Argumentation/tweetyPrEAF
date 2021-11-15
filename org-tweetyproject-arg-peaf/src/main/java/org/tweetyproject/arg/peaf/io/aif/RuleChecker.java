/******************************************************************************
 * This research was sponsored by the U.S. Army Research Laboratory and the
 * U.K. Ministry of Defence under the Biennial Program Plane 2013 (BPP13),
 * Project 6, Task 3: Collaborative Intelligence Analysis.
 * The U.S. and U.K. Governments are authorized to reproduce and distribute
 * reprints for Government purposes notwithstanding any copyright notation
 * hereon.
 * **************************************************************************
 *
 * This checks whether the rules of the WORKBOX view are correct
 *
 *
 * @author Alice Toniolo
 * @version 2.0
 * @since Nov 2017
 *
 */

package org.tweetyproject.arg.peaf.io.aif;


import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.tweetyproject.arg.peaf.io.aif.ersdata.Link;
import org.tweetyproject.arg.peaf.io.aif.ersdata.Node;

import java.util.*;


public class RuleChecker {
    //private static final String ER7="Links not valid, pro/con at the same time";
    //private static final String ER7="A CQ can only have one tail";
    //private static final String ER8="Pro links form a cycle";
    private ArrayList special;
    private HashMap prohead;
    private Set cycle;
    private DefaultDirectedGraph<String, DefaultEdge> graphnlg;

    public RuleChecker() {
        special = new ArrayList();
        prohead = new HashMap();
        cycle = null;
        graphnlg = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class); //with actual ids
    }

    public HashMap testRules(HashMap nodes, ArrayList links, HashSet prefnodes) {
        DefaultDirectedGraph<String, DefaultEdge> graph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
        HashMap probs = new HashMap();
        HashMap samehead = testSingleRules(graph, nodes, links, probs);
        testManyRules(nodes, samehead, links, probs);
        testGraphStructure(graph);
        testPreferences(prefnodes, nodes, probs);
        return probs;
    }

    public DefaultDirectedGraph<String, DefaultEdge> getGraph() {
        return graphnlg;
    }


    private void testPreferences(HashSet prefnodes, HashMap nodes, HashMap probs) {
        Iterator iter = prefnodes.iterator();
        String node;
        while (iter.hasNext()) {
            node = (String) iter.next();
            Node nd = (Node) nodes.get(node);
            //System.out.println(nd.getPref_left()+"<"+nd.getPref_right());
            if (nd.getPref_left() == null || nd.getPref_right() == null) {
                HashSet idprob;
                if (probs.containsKey(nd.getID())) {
                    idprob = (HashSet) probs.get(nd.getID());
                } else {
                    idprob = new HashSet();
                }
                idprob.add("ER-08:[" + nd.getID() + "] " + "Preference is not specified");
                probs.put(nd.getID(), idprob);
            }

        }

    }

    private HashMap testSingleRules(DefaultDirectedGraph graph, HashMap nodes, ArrayList links, HashMap probs) {
        ArrayList samehead = new ArrayList();

        /* List of Constraints over the rules
         *
         * - Only one link out of the Pro: V
         * - No links between nodes: checked at interface level
         *
         * - No repetition on the tail ids (double link) V
         * -  pro/Con must have head and 1 tail          V
         * - all tails/head of a link should be nodes    V
         *
         * - same set of tails/head -> same type and annot/text same not allowed
         * - same set of tails/head -> if type is different not allowed
         *
         * - if cq link, max 1 head-1tail
         */

        Iterator iter = links.iterator();
        Link link;
        HashSet idprob;
        HashMap headLinks = new HashMap();
        boolean check = true;
        ArrayList pros = new ArrayList();
        while (iter.hasNext()) {
            link = (Link) iter.next();
            String id = link.getId();
            String head = link.getHead();
            //check heads!

            check = true;
            if (head == null || head.replace(" ", "").length() < 1) {
                if (probs.containsKey(id)) {
                    idprob = (HashSet) probs.get(id);
                } else {
                    idprob = new HashSet();
                }
                idprob.add("ER-01:[" + id + "] " + "Link does not have a head");
                probs.put(id, idprob);
                check = false;

            } else {
                if (!nodes.containsKey(head)) {
                    if (probs.containsKey(id)) {
                        idprob = (HashSet) probs.get(id);
                    } else {
                        idprob = new HashSet();
                    }
                    idprob.add("ER-02:[" + id + "] " + "Link does not have a valid head");
                    probs.put(id, idprob);
                    check = false;
                }
            }
            //check double heads

            if (link.getPotHeads().size() > 1) {
                //	System.out.println("do I get here");
                if (probs.containsKey(id)) {
                    idprob = (HashSet) probs.get(id);
                } else {
                    idprob = new HashSet();
                }
                idprob.add("ER-09:[" + id + "] " + "Link has two heads");
                probs.put(id, idprob);
                check = false;
            }

            //now check tails
            ArrayList<String> tails = link.getTails();
            if (tails.size() < 1) {
                if (probs.containsKey(id)) {
                    idprob = (HashSet) probs.get(id);
                } else {
                    idprob = new HashSet();
                }
                idprob.add("ER-03:[" + id + "] " + "Link does not contain tails");
                probs.put(id, idprob);
                check = false;

            } else {

                boolean type = link.getCQtype();
                if (type) {/*
				//disabled due to the different CQ handling
					if(tails.size()>1){
						if(probs.containsKey(id)){
							idprob=(HashSet) probs.get(id);
						}else{
							idprob=new HashSet();
						}
						idprob.add("ER-07:["+id+"] "+ER7);
						probs.put(id, idprob);
						check=false;
					}*/
                } else {

                    System.out.println("Tails: " + tails);
                    System.out.println("Nodes: " + nodes);
                    Iterator itt = tails.iterator();
                    ArrayList test = new ArrayList();
                    String etail;
                    while (itt.hasNext()) {
                        String tail = (String) itt.next();
                        System.out.println("Tail: " + tail);
                        if (test.contains(tail)) {
                            if (probs.containsKey(id)) {
                                idprob = (HashSet) probs.get(id);
                            } else {
                                idprob = new HashSet();
                            }


                            if (!nodes.containsKey(tail)) {
                                idprob.add("ER-10:[" + id + "] Link does not have a valid tail");
                                probs.put(id, idprob);
                                check = false;
                            } else {
                                etail = ((Node) nodes.get(tail)).getID();
                                idprob.add("ER-04:[" + id + "] Link has a double premise [" + etail + "]");
                                probs.put(id, idprob);
                                check = false;
                            }

                        }
                        else {
                            test.add(tail);
                            if (!nodes.containsKey(tail)) {
                                if (probs.containsKey(id)) {
                                    idprob = (HashSet) probs.get(id);
                                } else {
                                    idprob = new HashSet();
                                }
                                if (!nodes.containsKey(tail)) {
                                    idprob.add("ER-11:[" + id + "] " + "Link does not have a valid tail");
                                    probs.put(id, idprob);
                                    check = false;

                                } else {
                                    etail = ((Node) nodes.get(tail)).getID();
                                    idprob.add("ER-05:[" + id + "] " + "Link has a tail that does not exist" + " [" + etail + "]");
                                    probs.put(id, idprob);
                                    check = false;
                                }
                            }
                        }

                    }
                }
            }
            if (check) {

                //the rule is ok!
                //Add the rule to the graph


                if (link.isPro()) {
                    graph.addVertex(head);
                    graph.addVertex(id);
                    graph.addEdge(id, head);
                    graphnlg.addVertex(id);
                    String hid = ((Node) nodes.get(head)).getID();
                    graphnlg.addVertex(hid);
                    graphnlg.addEdge(id, hid);

                    Iterator itt = tails.iterator();
                    String tail;

                    while (itt.hasNext()) {
                        tail = (String) itt.next();
                        graph.addVertex(tail);
                        graph.addEdge(tail, id);
                        String tid = ((Node) nodes.get(tail)).getID();
                        graphnlg.addVertex(tid);
                        graphnlg.addEdge(tid, id);

                    }
                    ArrayList prolink;
                    if (prohead.containsKey(head)) {
                        prolink = (ArrayList) prohead.get(head);
                    } else {
                        prolink = new ArrayList();
                    }
                    prolink.add(link.getId());
                    prohead.put(head, prolink);

                } else {//CON
                    //is it special? Con & tail and head same dimension
                    if (tails.size() == 1) {
                        special.add(link);
                    }
                    //add it to nlg
                    String hid = ((Node) nodes.get(head)).getID();
                    graphnlg.addVertex(hid);
                    Iterator itt = tails.iterator();
                    String tail;
                    while (itt.hasNext()) {
                        tail = (String) itt.next();
                        String tid = ((Node) nodes.get(tail)).getID();
                        graphnlg.addVertex(tid);
                    }
                }
                //create array of same head links

                if (headLinks.containsKey(head)) {
                    samehead = (ArrayList) headLinks.get(head);
                } else {
                    samehead = new ArrayList();
                }
                samehead.add(link);
                headLinks.put(head, samehead);

            }
        }


        return headLinks;
    }


    private void testManyRules(HashMap nodes, HashMap headLinks, ArrayList links, HashMap probs) {
        //System.out.println(headLinks);
        //Now I check the problems related to many links
        Iterator iter = headLinks.keySet().iterator();
        ArrayList samehead;
        HashSet idprob;
        ArrayList tail1;
        ArrayList tail2;
        String key;
        Iterator iti, itj;
        Link link1, link2;
        int j = 0;
        while (iter.hasNext()) {
            key = (String) iter.next();

            samehead = (ArrayList) headLinks.get(key);
            if (samehead.size() > 1) {
                //now check tails
                iti = samehead.iterator();
                while (iti.hasNext()) {
                    link1 = (Link) iti.next();
                    j++;
                    if (j <= samehead.size()) {
                        itj = samehead.listIterator(j);
                        while (itj.hasNext()) {
                            //System.out.println(samehead);
                            link2 = (Link) itj.next();
                            //	 System.out.println("IAM COMING"+link1+link2);
                            tail1 = (ArrayList) link1.getTails();
                            tail2 = (ArrayList) link2.getTails();
                            if (tail1.size() == tail2.size()) {

                                Iterator itx = tail1.iterator();
                                boolean cond = false;
                                String idt;
                                while (itx.hasNext()) {
                                    idt = (String) itx.next();
                                    if (!tail2.contains(idt)) {
                                        cond = true;
                                        break;
                                    }
                                }
                                if (!cond) {//they have the same inputs
                                    if (link1.isPro() == link2.isPro()) {
										/*
										 * SAME PRO/CON LINK 
										 * 
										String prob="ER-06:["+link1.get("id")+"-"+link2.get("id")+"] "+ER7;
										if(probs.containsKey(link1.get("id"))){
											idprob=(ArrayList) probs.get(link1.get("id"));
										}else{
											idprob=new HashSet();
										}
										idprob.add(prob);
										probs.put(link1.get("id"), idprob);
										if(probs.containsKey(link2.get("id"))){
											idprob=(ArrayList) probs.get(link2.get("id"));
										}else{
											idprob=new HashSet();
										}
										idprob.add(prob);
										probs.put(link2.get("id"), idprob);*/

                                    } else {
                                        if (link1.getText().equals(link2.getText())) {
                                            //they are the same
                                            String prob = "ER-06: [" + link1.getId() + "-" + link2.getId() + "] " + "Same links";
                                            if (probs.containsKey(link1.getId())) {
                                                idprob = (HashSet) probs.get(link1.getId());
                                            } else {
                                                idprob = new HashSet();
                                            }
                                            idprob.add(prob);
                                            probs.put(link1.getId(), idprob);
                                            if (probs.containsKey(link2.getId())) {
                                                idprob = (HashSet) probs.get(link2.getId());
                                            } else {
                                                idprob = new HashSet();
                                            }
                                            idprob.add(prob);
                                            probs.put(link2.getId(), idprob);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


    }


    private void testGraphStructure(DefaultDirectedGraph graph) {
        // TODO Auto-generated method stub
        CycleDetector cycledet = new CycleDetector(graph);
        Set cycleV = cycledet.findCycles();
        if (!cycleV.isEmpty()) {
            //for now we say that this is not allowed....
            //other way to handle this is to use all input to the cycle as premises
            cycle = cycleV;
        }//else no cycles detected!! Great!!!

    }

    public ArrayList getSpecial() {
        return special;
    }

    public HashMap getSamehead() {
        return prohead;
    }

    public Set getCycle() {
        return cycle;
    }

}
