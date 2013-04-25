package Graph;

import Share.Comm;
import Share.log;

public class edgeNominal {
//
//	public Node node1, node2;
//	public int numSharedDocs;
//	public double mutualInfo;
//	
//	public edgeNominal(Node n1, Node n2)
//	{
//		node1 = n1;
//		node2 = n2;
//		node2.parent = null;
//		initNumSharedDocs();
//		initMutualInformation();
//	}
//	
//	public edgeNominal(Node n1, Node n2, double mi)
//	{
//		node1 = n1;
//		node2 = n2;
//		node2.parent = null;
//		initNumSharedDocs();
//		this.mutualInfo = mi;
//	}
//	
//	public edgeNominal(edgeNominal edge)
//	{
//		node1 = edge.node1;
//		node2 = edge.node2;
//		node2.parent = node1;
//		numSharedDocs = edge.numSharedDocs;
//		mutualInfo = edge.mutualInfo;
//		initMutualInformation();
//	}
//	
//	protected void initNumSharedDocs()
//	{
//		this.numSharedDocs = NodeHelper.numShareDocs(node1, node2);
//	}
//	
//	/**
//	 * Initial the mutual information with the given two random variables' distribution.
//	 */
//	protected void initMutualInformation()
//	{
//		double mi = NodeHelper.mutualInfo(node1, node2);
//	}
//	
//	
//	public boolean isExist(nodeNominal node)
//	{
//		return (node1.wordId == node.wordId || node2.wordId == node.wordId)? true : false;
//	}
//	
//	public Node getNeighborNode(nodeNominal node)
//	{
//		if (node1.wordId == node.wordId)
//		{
//			return node2;
//		}
//
//		if (node2.wordId == node.wordId)
//		{
//			return node1;
//		}
//		
//		return null;
//	}
//	
//	public String toString()
//	{
//		return "{" + node1.wordId + "," + node2.wordId + "," + numSharedDocs + "," + mutualInfo + "}";
//	}
//	
//
//	public boolean isSelfEdge()
//	{
//		if (node1 == node2)
//		{
//			return true;
//		}
//		return false;
//	}
	

}
