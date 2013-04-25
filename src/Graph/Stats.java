package Graph;

public class Stats {

	public StatsDet minor, major, total;
	public Stats(Node node1, Node node2)
	{
		minor = new StatsDet(node1.minorNode, node2.minorNode);
		major = new StatsDet(node1.majorNode, node2.majorNode);

		total = new StatsDet();
		total.addOne(minor);
		total.addOne(major);
	}
	
}
