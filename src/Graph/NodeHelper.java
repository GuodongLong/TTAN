package Graph;

import weka.core.Instance;
import Share.Comm;
import Share.log;

public class NodeHelper {	

	/**
	 * P(X2|X1,Y)
	 * Two nodes are generated from the class Y, so the Y is true, and we need not consider it.
	 * @param node1
	 * @param node2
	 * @return
	 */
	public static double getCondPDF(graphNominal graph, Node parentNode, Node childNode, Instance instance, int classValue)
	{
		nodeNominal child = null, parent = null;
		if (classValue == Comm.MINOR_CLASS)
		{
			child  = childNode.minorNode;
			parent = parentNode.minorNode;
		}
		else
		{
			child  = childNode.majorNode;
			parent = parentNode.majorNode;
		}
		
		Edge edge = null;
		for(Edge e : childNode.connEdges)
		{
			if ((e.node1.wordId == parentNode.wordId) || (e.node2.wordId == parentNode.wordId))
			{
				edge = e;
				break;
			}
		}
		if (edge == null)
		{
			log.print("ERROR: edge should no be null!!!");
		}
		
		int value1 = 0, value2 = 0;
		if (instance.value(parent.wordId) > Comm.MIN_DOUBLE)
		{
			value1 = 1;
		}
		if (instance.value(child.wordId) > Comm.MIN_DOUBLE)
		{
			value2 = 1;
		}
		int numShDocs = edge.stats.total.numShareDocs(parent, child, value1, value2);
		int parentDocs = 1 + parent.occurrenceCnt[value2];
		double PDF = (double)numShDocs / parentDocs;
		return PDF;
	}
}
