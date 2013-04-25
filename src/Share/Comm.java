package Share;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Comm {

//	public static String workPath = "/home/gulong/workspace/TTAN/";
//	public static String logPath  = workPath + "log/";
//	public static String dataPath = workPath + "data/";
//	public static String tempPath = workPath + "temp/";

	public static String workPath = "E:\\don\\TTAN\\";
	public static String logPath  = workPath + "log\\";
	public static String dataPath = workPath + "data\\";
	public static String tempPath = workPath + "temp\\";
	
	public static String NRMFileName = dataPath + "labeled_nrm_Weka.arff";
	public static String WikiFileName = dataPath + "labeled_wiki_Weka.arff";
	public static String UnlabeledFileName = dataPath + "unlabeled_wiki_Weka.arff";
	
	public static String WekaTemplate = dataPath + "template_Weka.txt";
	
	public static String TrainSetFileName = Comm.tempPath + "trainSet.arff";
	public static String TestSetFileName  = Comm.tempPath + "testSet.arff";
	
	public static double MIN_DOUBLE = 1e-75;
	public static double MIN_MUTUAL_INFO = 0.1; //0.005406894170951938
	public static int    ATTR_SIZE = 6112;
	
	public static int MINOR_CLASS = 0;
	public static int MAJOR_CLASS = 1;
	
	public static int MAX_ITERATION = 10;
	
	/**
	 * Array
	 */
	
	public static void clearArray(int[] ar)
	{
		for(int i = 0; i < ar.length; i++)
		{
			ar[i] = 0;
		}
	}

	/**
	 * Normalized the array.
	 * @param ar
	 */
	public static void NormalizeArray(double[] ar)
	{
		int iSum = 0;
		for(int i = 0; i < ar.length; i++)
		{
			iSum += ar[i];
		}
		for(int i = 0; i < ar.length; i++)
		{
			ar[i] = ar[i]/iSum;
		}
	}
	
	/**
	 * Double Format
	 */
	public static String formatDouble(double d)
	{
		return formatDouble(d, "#0.000000");
	}
	
	public static String formatDouble(double d, String str)
	{
		NumberFormat format = new DecimalFormat(str);
		return format.format(d);
	}
	
	/**
	 * Get the maximal value of the given array.
	 * @param arr
	 * @return
	 * @throws NullPointerException
	 */
	public static double max(double[] arr) throws NullPointerException
	{
		if (arr == null || arr.length < 1)
		{
			log.print("Error num of array.");
			throw new NullPointerException();
		}
		double maxVal = arr[0];
		for(double d: arr)
		{
			if (d > maxVal)
			{
				maxVal = d;
			}
		}
		return maxVal;
	}

	
	
}
