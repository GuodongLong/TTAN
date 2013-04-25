package Graph;

import java.util.ArrayList;
import java.util.List;

import Share.Comm;
import Share.log;
import weka.core.Instances;

public class nodeNominal {

	public nodeNominal parent;
	public int wordId;
	public String wordStr;
//	public int classValue;
	
	public List<Integer> occurrence;
	public double[] PDF = new double[2];
	public int[] occurrenceCnt = new int[2];

	public int instancesSize = 0;
	
	public nodeNominal(Instances instances, int attrIdx)
	{		
		occurrence = new ArrayList<Integer>();
		wordId = attrIdx;
//		this.classValue = (int) instances.get(0).classValue();

		instancesSize = instances.size();
		
		// smoothing
		for(int i = 0; i < instances.size(); i++)
		{
			double value = instances.get(i).value(attrIdx);
			if (value > Comm.MIN_DOUBLE)
			{
//				log.print("value=" + value);
				occurrence.add(i);
				occurrenceCnt[1] ++;
			}
			else
			{
				occurrenceCnt[0] ++;
			}
		}
		
//		PDF[0] =  Math.max(1e-75, Math.pow((double)occurrenceCnt[0] / instances.size(), instances.attribute(attrIdx).weight()));
//		PDF[1] =  Math.max(1e-75, Math.pow((double)occurrenceCnt[1] / instances.size(), instances.attribute(attrIdx).weight()));
		PDF[0] =  Math.max(Comm.MIN_DOUBLE, (double)occurrenceCnt[0] / instances.size());
		PDF[1] =  Math.max(Comm.MIN_DOUBLE, (double)occurrenceCnt[1] / instances.size());
		wordStr   = String.valueOf(wordId);
	}
	
	/**
	 * P(X1)
	 * @return
	 */
	public double getPDF()
	{
		return PDF[1];
	}
	public double getPDF(double value)
	{
		return PDF[(int)value];
	}

	public String toString()
	{
		return "{" + wordId + ", " + wordStr + "}";
	}
	
}
