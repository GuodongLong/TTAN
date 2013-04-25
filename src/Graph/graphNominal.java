package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import Share.Comm;

public class graphNominal {

	public Node root;
	public List<Edge> edges;
	public HashMap<Integer, Node> nodes;
	
	public graphNominal()
	{
		nodes = new HashMap<Integer, Node>((int)(Comm.ATTR_SIZE * 1.2));
		edges = new ArrayList<Edge>(Comm.ATTR_SIZE);
	}
	
	/**
	 * Copy the value of all elements of the given Graph object.
	 * @param graph
	 */
	public graphNominal(graphNominal graph)
	{
		nodes = new HashMap<Integer, Node>(Comm.ATTR_SIZE * Comm.ATTR_SIZE / 2);
		edges = new ArrayList<Edge>(Comm.ATTR_SIZE);
		initNodes(graph);
		initEdges(graph);
	}
	
	/**
	 * Create new object
	 * @param graph
	 */
	public void initNodes(graphNominal graph)
	{
//		addNode(graph.nodes);
		Iterator iter = graph.nodes.entrySet().iterator();
		
		while(iter.hasNext())
		{
			Entry<Integer, Node> entry = (Entry<Integer, Node>) iter.next();
			Integer key  = entry.getKey();
			Node    node = entry.getValue();
			addNode(key, node);
		}
	}

	
	/**
	 * Create new object
	 * @param graph
	 */
	public void initEdges(graphNominal graph)
	{
		addEdge(graph.edges);
	}
	
	/**
	 * justify whether the given node belongs to current graph.
	 * @param node
	 * @return
	 */
	public boolean isExist(Node node)
	{
		return (nodes.get(node.wordId) != null)? true : false;
	}
	
	public int numNodes()
	{
		return nodes.size();
	}
	
	public int numEdges()
	{
		return edges.size();
	}
	
	public void addNode(Node e)
	{
		nodes.put(e.wordId, e);
	}
	
	public void addNode(int key, Node node)
	{
		nodes.put(key, node);
	}

	public void addNode(HashMap<Integer, Node> hsNodes)
	{
		Iterator iter = hsNodes.entrySet().iterator();		
		while(iter.hasNext())
		{
			Entry<Integer, Node> entry = (Entry<Integer, Node>) iter.next();
			Integer key  = entry.getKey();
			Node    node = entry.getValue();
			addNode(key, node);
		}
	}
	
	public void removeNode(Node e)
	{
		nodes.remove(e.wordId);
	}
	
	public Node getNode(int index)
	{
		Iterator<Entry<Integer, Node>> iter = nodes.entrySet().iterator();
		int idx = 0;
		while(iter.hasNext())
		{
			Entry<Integer, Node> entry = iter.next();
			if (idx == index)
			{
				Node    node = entry.getValue();
				return node;
			}
			idx++;
		}

		return  null;
	}
	
//
//	public void addEdge(Edge edge)
//	{
//		int key = calcEdgeKey(edge);
//		edges.put(key, edge);
//	}
	
	public void addEdge(Edge e)
	{
		edges.add(e);
	}
	
	public void addEdge(List<Edge> es)
	{
		edges.addAll(es);
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Graph info\n");
		sb.append("nodes:");
		Iterator iter = (Iterator) nodes.keySet().iterator();		
		while(iter.hasNext())
		{
			Entry<Integer, Node> entry = (Entry<Integer, Node>) iter.next();
			Integer key  = entry.getKey();
			Node    node = entry.getValue();
			sb.append(node.toString());
			sb.append(",");
		}
//		for(Node node : nodes)
//		{
//			sb.append(node.toString() + ",");
//		}
		sb.append("\n");
		sb.append("edges:");
		
		for(Edge edge : edges)
		{
			sb.append(edge.toString() + ",");
		}
//		Iterator iter = (Iterator) edges.keySet().iterator();		
//		while(iter.hasNext())
//		{
//			Entry<Integer, Edge> entry = (Entry<Integer, Edge>) iter.next();
//			Integer key  = entry.getKey();
//			Edge    edge = entry.getValue();
//			sb.append(key);
//			sb.append("-");
//			sb.append(edge.toString());
//		}
		sb.append("\n");
		return sb.toString();
	}
	

	
}
