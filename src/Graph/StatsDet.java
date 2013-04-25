package Graph;

public class StatsDet {

	public int shared11 = 0;
	public int shared10 = 0;
	public int shared01 = 0;
	public int shared00 = 0;
	public double MI = 0;
	

	public StatsDet()
	{
	}
	
	public StatsDet(nodeNominal node1, nodeNominal node2)
	{
		int i = 0, j = 0;
		while(true)
		{
			if (i == node1.occurrence.size() || j == node2.occurrence.size())
			{
				break;
			}
			int n1 = node1.occurrence.get(i);
			int n2 = node2.occurrence.get(j);
			if (n1 == n2)
			{
				i++;
				j++;
				shared11++;
				continue;
			}
			else if (n1 < n2)
			{
				i++;
				continue;
			}
			else if (n1 > n2)
			{
				j++;
				continue;
			}
		}
		
		//smoothing
		shared11 = 1 + shared11;
		shared10 = 2 + node1.occurrenceCnt[1] - shared11;
		shared01 = 2 + node2.occurrenceCnt[1] - shared11;
		shared00 = 4 + node1.instancesSize - shared11 - shared10 - shared01;
		
		MI = mutualInfo(node1, node2);
	}

	public double mutualInfo(nodeNominal node1, nodeNominal node2)
	{
		double mi = 0;

//		double jointPDF = getJointPDF(node1, node2, 1, 1);
//		double node1PDF = node1.getPDF(1);
//		double node2PDF = node2.getPDF(1);
//		mi += jointPDF * Math.log(jointPDF / (node1PDF * node2PDF));
		for(int value1 = 1; value1 <= 1; value1++)
		{
			for(int value2 = 1; value2 <= 1; value2++)
			{
				double jointPDF = getJointPDF(node1, node2, value1, value2);
				double node1PDF = node1.getPDF(value1);
				double node2PDF = node2.getPDF(value2);
				mi += jointPDF * Math.log(jointPDF / (node1PDF * node2PDF));
			}
		}
		return mi;	
	}
	
	public double getJointPDF(nodeNominal node1, nodeNominal node2, int value1, int value2)
	{
		int numShDocs = numShareDocs(node1, node2, value1, value2);
		int totDocs = node1.instancesSize;
		double PDF = (double)numShDocs / totDocs;
		return PDF;
	}
	
	public int numShareDocs(nodeNominal node1, nodeNominal node2, int value1, int value2)
	{	
		int shared = 0;
		if (value1 == 1 && value2 == 1)
		{
			shared = shared11;
		}
		else if (value1 == 1 && value2 == 0)
		{
			shared = shared10;
		}
		else if (value1 == 0 && value2 == 1)
		{
			shared = shared01;
		}
		else if (value1 == 0 && value2 == 0)
		{
			shared = shared00;
		}
		
		return shared;
	}

	public void addOne(StatsDet newStat)
	{
		shared11 += newStat.shared11;
		shared10 += newStat.shared10;
		shared01 += newStat.shared01;
		shared00 += newStat.shared00;
		MI       += newStat.MI;
	}
}
