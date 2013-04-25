package Graph;

import java.util.ArrayList;
import java.util.List;

import Share.Comm;

public class Node {

	public nodeNominal minorNode, majorNode;
	public Node parent;
	public int wordId;
	// related edges for this node.
	public List<Edge> connEdges;
	public List<Node> children;

	public Node(nodeNominal minor, nodeNominal major, int wid)
	{
		minorNode = minor;
		majorNode = major;
		wordId = wid;
		connEdges = new ArrayList<Edge>();
		children  = new ArrayList<Node>();
	}
	public void addEdge(Edge edge)
	{
		connEdges.add(edge);
	}
	
	public void addChild(Node node)
	{
		children.add(node);
	}
	
	public double getPDF(int value, int classValue)
	{
		nodeNominal node = null;
		if (classValue == Comm.MINOR_CLASS)
		{
			node = minorNode;
		}
		else
		{
			node = majorNode;
		}
		return node.getPDF(value);
	}
}
