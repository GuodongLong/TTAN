package classifier;

import java.io.File;
import java.util.Random;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import Exception.DataFileNotFind;
import Exception.DataFileReadError;
import Exception.DataSourceLoadError;
import Exception.IllegalEnumValue;
import Exception.TimerException;
import Share.log;

public class InstanceHelper {


	/**
	 * Load data from file.
	 * @param fileName
	 * @throws DataFileReadError 
	 * @throws DataFileNotFind 
	 * @throws DataSourceLoadError 
	 * @throws TimerException 
	 * @throws Exception, IllegalEnumValue 
	 */
	public static Instances loadDataFromFile(String fileName) throws IllegalEnumValue, DataFileReadError, DataFileNotFind, DataSourceLoadError, TimerException
	{
		log.print("Begin load data from file " + fileName);
		
		/* Validate file */
		File file = new File(fileName);
		if (!file.exists() || !file.isFile())
		{
			log.print("File " + fileName + " is not exist or is not a real file.");
			throw new DataFileNotFind();
		}

		/* Load data */
//		DataSource dataSource = null;
//		try {
//			dataSource = new DataSource(fileName);
//		} catch (Exception e) {
//			log.print("Read " + fileName + " file error.");
//			e.printStackTrace();
//			throw new DataFileReadError();
//		}
		
		/* Get instances */
		Instances instances = null;
		try {
			instances = DataSource.read(fileName);//dataSource.getDataSet(0);
			instances.setClassIndex(instances.numAttributes() - 1);
		} catch (Exception e) {
			log.print("The " + fileName + " cannot be loaded correctly." + e.toString());
			e.printStackTrace();
			throw new DataSourceLoadError();
		}
		
		log.print("Total " + instances.size() + " samples were loaded!");
		
		return instances;
	}
	

	/**
	 * Random split the instances into train set and test set.
	 * @param trainRatio
	 * @param seed
	 * @return
	 */
	public static Instances[] randomSplitList(Instances instances, double trainRatio, int seed)
	{
		Random random = new Random(seed);
		int trainSize = (int) (instances.size() * trainRatio);
		int testSize = instances.size() - trainSize;
		
		Instances[] listInstances = new Instances[2];
		listInstances[0] = new Instances(instances, trainSize);
		listInstances[1] = new Instances(instances, testSize);

		Instances tmpInsts = new Instances(instances);
		tmpInsts.randomize(random);
		for(int i = 0; i < instances.size(); i++)
		{
			if (i < trainSize)
			{
				listInstances[0].add(tmpInsts.get(i));
			}
			else
			{
				listInstances[1].add(tmpInsts.get(i));
			}
		}
		return listInstances;
	}
	
	/**
	 * Get a random list of instance.
	 * @param trainRate
	 * @return
	 */
	public static Instances[] splitSet(Instances instances, double trainRate)
	{
		int trainEndIdx = (int) (instances.size() * trainRate);

		Instances[] listInstances = new Instances[2];
		
		listInstances[0] = subList(instances, 0, trainEndIdx - 1);
		listInstances[1] = subList(instances, trainEndIdx, instances.size() - 1);
		
		return listInstances;
	}
	
	public static Instances subList(Instances instances, int beginIdx, int endIdx)
	{
		Instances tmpInstances = new Instances(instances, instances.size());
		int idx = 0;
		for(Instance instance: instances)
		{
			idx++;
			if(idx >= beginIdx && idx <= endIdx)
			{
				tmpInstances.add(instance); /* It generate a copy of the instance object, and add it into instances */
			}
		}
		
		return tmpInstances;
	}
}
