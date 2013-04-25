package Graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import Share.FileOption;
import Share.log;

public class spanningTreeNominal {

	/**
	 * generate a spanning tree based on the given graph
	 * Edge_size(tree) = Node_size(graph) - 1 
	 * @param graph
	 * @return
	 * @throws IOException 
	 */
	public static graphNominal genSpTree(graphNominal graph) throws IOException
	{
		graphNominal tmpGraph = new graphNominal(graph);
		List<graphNominal> trees = new ArrayList<graphNominal>();
		
		// init the first node of the spanning tree
		Random random = new Random(1);
		Boolean isNewTree = true;
		graphNominal currTree = null;
		// iteratively add edge and node
		int nodeSize = tmpGraph.numNodes();
		log.print("nodeSize=" + nodeSize);
		int countNode = 0;
		while(countNode < nodeSize)
		{
			Edge maxEdge = null;
			Node maxNode = null;
			if (isNewTree) // create a new tree
			{
				int index = 0;
				if (tmpGraph.numNodes() > 1)
				{
					index = random.nextInt(tmpGraph.numNodes() - 1);	
				}
				currTree = new graphNominal();
				trees.add(currTree);
				
				maxNode = tmpGraph.getNode(index);
				if (maxNode == null)
				{
					log.print("");
					continue;
				}
				currTree.root = maxNode;
				currTree.addNode(maxNode);
				tmpGraph.removeNode(maxNode);
//				log.print("create new tree, and add new node " + maxNode.wordId + " in current tree. countNode = " + countNode);
				countNode++;
				isNewTree = false;
				continue;
			}
			
			// find the maximal edge which is connected to current tree.
			double maxMI = -1;
			Iterator iter = currTree.nodes.entrySet().iterator();
			while(iter.hasNext())
			{
				Entry<Integer, Node> entry = (Entry<Integer, Node>) iter.next();
//				Integer key  = entry.getKey();
				Node    node = entry.getValue();
				for(Edge edge : node.connEdges)
				{
					if (edge.mutualInfo() > maxMI)
					{
						Node tmpNode = null;
						if (node.wordId == edge.node1.wordId)
						{
							tmpNode = edge.node2;
						}
						else
						{
							tmpNode = edge.node1;
						}
						if (!currTree.isExist(tmpNode)) // another node is not exist in current tree.
						{
							maxMI = edge.mutualInfo();
							maxEdge = edge;
							maxNode = tmpNode;
						}
					}
				}
			}
			
			if (maxEdge != null)  // add node and edge to the current tree
			{
				if (maxNode.wordId == maxEdge.node1.wordId)
				{
					maxNode.parent = maxEdge.node2;
					maxEdge.node2.addChild(maxNode);
				}
				else
				{
					maxNode.parent = maxEdge.node1;
					maxEdge.node1.addChild(maxNode);
				}
				currTree.addNode(maxNode);
				currTree.addEdge(maxEdge);
				tmpGraph.removeNode(maxNode);
				countNode++;
//				log.print("add new node " + maxNode.wordId + " in current tree. countNode = " + countNode + ", maxMI = " + maxMI);
			}
			else // it is a disjoint tree
			{
				isNewTree = true;
//				log.print("A new disjoint tree's nodes size is " + currTree.numNodes() + ", edges size is " + currTree.numEdges() + ", countNode = " + countNode);
			}
		}
		
		// combine all disjoint trees together to form a forest.
		log.print("start to combine all trees " + trees.size());
		graphNominal forest = new graphNominal();
		for(graphNominal tree : trees)
		{
			// view the tree on the graph tools.
//			if (tree.numNodes() > 1)
//			{
//				log.print("big tree " + tree.numNodes());
//				TreeView.transformData(tree, FileOption.CS_IN_DOMAIN_Nominal.replace(".arff", idx + ".xml"));
//				idx++;
//				TreeView tv = new TreeView();
//				tv.init();
//			}
			forest.addNode(tree.nodes);
			forest.addEdge(tree.edges);
		}
		
		return forest;
	}
	public static int idx = 0;
}
