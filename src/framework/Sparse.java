package framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import Exception.DataFileNotFind;
import Exception.DataFileReadError;
import Exception.DataSourceLoadError;
import Exception.IllegalEnumValue;
import Exception.TimerException;
import Share.log;

public class Sparse {

	public static double SPARSE_RATE = 0.5;
	public static int TOTAL_VALUES = 0;
	public static Instances removeValues(Instances instances) throws IllegalEnumValue, DataFileReadError, DataFileNotFind, DataSourceLoadError, TimerException, IOException
	{
		for(Instance instance : instances)
		{
			for(int i = 0; i < instance.numAttributes() - 1; i++)
			{
				if (instance.value(i) > 0)
				{
					TOTAL_VALUES++;
				}
			}
		}
		double leftValues = (1 - SPARSE_RATE) * TOTAL_VALUES;
		log.print("leftValues= " + leftValues + ", sparse_rate=" + SPARSE_RATE + ", total_values = " + TOTAL_VALUES);
		int MIN_ATTR = (int) (leftValues / instances.size() - 0.499999999999);
		log.print("MIN_ATTR=" + MIN_ATTR);
		Instances newInstances = new Instances(instances);
		Random random = new Random(1);
		int valueCnt = 0, changeCnt = 0;
		for(Instance instance : newInstances)
		{
			int tmpCnt = 0, tmpChgCnt = 0;
			List<Integer> lstAttrs = new ArrayList<Integer>();
			for(int i = 0; i < instance.numAttributes() - 1; i++)
			{
				if (instance.value(i) > 0)
				{
					lstAttrs.add(i);
					valueCnt ++; tmpCnt ++;
				}
			}
			
			int leftAttrCnt = lstAttrs.size();
			double filterRatio = (double)MIN_ATTR / lstAttrs.size();
			for(int index : lstAttrs)
			{
				if (leftAttrCnt <= MIN_ATTR)
				{
					break;
				}
				if (random.nextDouble() > filterRatio)
				{
					changeCnt++; tmpChgCnt++;
					instance.setValue(index, 0);
					leftAttrCnt--;
				}
			}
			
			int dist = tmpCnt - tmpChgCnt;
			NormalizeArray(instance);
			
			
//			log.printNoAddon(dist + "," + tmpCnt + "," + tmpChgCnt);
		}
		
		log.print("Total " + valueCnt + " values and " + changeCnt + " values are changed, changed ratio is " + (double)changeCnt/valueCnt);
		return newInstances;
	}

	/**
	 * Normalized the array.
	 * @param ar
	 */
	public static void NormalizeArray(Instance instance)
	{
		int iSum = 0;
		for(int i = 0; i < instance.numAttributes() - 1; i++)
		{
			iSum += instance.value(i);
		}
		for(int i = 0; i < instance.numAttributes() - 1; i++)
		{
			instance.setValue(i, instance.value(i)/iSum);
		}
	}
}
