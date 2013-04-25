package Graph;

public class Edge {
	
	public Node node1, node2;
	public Stats stats;

	public Edge(Node n1, Node n2, Stats stats)
	{
		this.node1          = n1;
		this.node2          = n2;
		this.stats     		= stats;
	}
	
	public double mutualInfo()
	{
		return stats.total.MI;
	}
	
	public boolean isExist(Node node)
	{
		return (node1.wordId == node.wordId || node2.wordId == node.wordId)? true : false;
	}
	
	public Node getNeighborNode(Node node)
	{
		if (node1.wordId == node.wordId)
		{
			return node2;
		}

		if (node2.wordId == node.wordId)
		{
			return node1;
		}
		
		return null;
	}
	

	public boolean isSelfEdge()
	{
		if (node1 == node2)
		{
			return true;
		}
		return false;
	}
}
