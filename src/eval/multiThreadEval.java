package eval;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import framework.mainFrame;
import Share.log;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.output.prediction.AbstractOutput;
import weka.core.Instances;

public class multiThreadEval extends Evaluation {

	public static int activeThreads = 0;
	public static int totalThreads = 0;
	public static int maxThreads = 1;
	public static int ThreadId = 0;

	public int corrCnt = 0, totCnt = 0;
	public double accuracy = 0;
	public static List<Instances> lstTest = new ArrayList<Instances>();	
	
	public multiThreadEval(Instances data) throws Exception {
		super(data);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void crossValidateModel(Classifier classifier,
          Instances data, int numFolds, Random random,
          Object... forPredictionsPrinting) throws Exception 
    {
		totalThreads = numFolds; 
				
		// Make a copy of the data we can reorder
		data = new Instances(data);
		data.randomize(random);
		if (data.classAttribute().isNominal()) {
			data.stratify(numFolds);
		}
		
		// We assume that the first element is a
		// weka.classifiers.evaluation.output.prediction.AbstractOutput object
		AbstractOutput classificationOutput = null;
		if (forPredictionsPrinting.length > 0) {
			// print the header first
			classificationOutput = (AbstractOutput) forPredictionsPrinting[0];
			classificationOutput.setHeader(data);
			classificationOutput.printHeader();
		}
		
		// Do the folds
		List<classifierThread> threads = new ArrayList<classifierThread>();
		for (int i = 0; i < numFolds; i++) {
			Instances train = data.trainCV(numFolds, i, random);
			Instances test = data.testCV(numFolds, i);			
			lstTest.add(test);
			
//			setPriors(train);
//			log.print("k_MarginResolution = " + this.k_MarginResolution);
//			Classifier copiedClassifier = AbstractClassifier.makeCopy(classifier);
//			copiedClassifier.buildClassifier(train);
//			log.print("k_MarginResolution = " + this.k_MarginResolution);
//			evaluateModel(copiedClassifier, test, forPredictionsPrinting);
			
			try
			{
				activeOneThreads();
				Thread thread1 = new classifierThread(classifier, train, test);
				threads.add((classifierThread)thread1);
				thread1.start();           // Start the Thread
				log.print("the " + i + "th thread is started!!!");
			}
			catch(Exception e)	
			{
				e.printStackTrace();
			}
		}

		m_NumFolds = numFolds;
		while(true)
		{
			if (!isAllThreadFinished())
			{
				waitOneSec(10);
				continue;
			}
			else
			{
				waitOneSec(5);
				break;
			}
		}
		
		for(classifierThread thread : threads)
		{
			corrCnt += thread.corrCnt;
			totCnt += thread.totCnt;
		}
		accuracy = (double)corrCnt / totCnt;
		
		log.print("All thread is finished!!!, accuracy = " + accuracy);
		
		if (classificationOutput != null)
			classificationOutput.printFooter();
    }
	
	public static void activeOneThreads()
	{
		while(true)
		{
			if (checkActiveThreads())
			{
				return;
			}
			waitOneSec(10);
		}
	}
	
	public static synchronized boolean checkActiveThreads()
	{
		if (activeThreads < maxThreads)
		{
			activeThreads++;
			log.print("activeThreads=" + activeThreads + ", maxThreads=" + maxThreads);
			return true;
		}
		return false;
	}
	
	public static synchronized void finishOneThreads()
	{
		activeThreads--;
		log.print("activeThreads=" + activeThreads + ", maxThreads=" + maxThreads);
	}
	public static synchronized int getActiveThreadsNum()
	{
		return activeThreads;
	}
	public static synchronized boolean isAllThreadFinished()
	{
		if (activeThreads == 0)
		{
			return true;
		}
		return false;
	}
	
	public static synchronized int getThreadId()
	{
		return ThreadId++;
	}
	
	public static void waitOneSec(int k)
	{
		try {
			Thread.sleep(1000 * k);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.print(e.toString());
			log.print(e.getStackTrace());
		}
	}
	
//
//	  /**
//	   * Evaluates the classifier on a given set of instances. Note that
//	   * the data must have exactly the same format (e.g. order of
//	   * attributes) as the data used to train the classifier! Otherwise
//	   * the results will generally be meaningless.
//	   *
//	   * @param classifier machine learning classifier
//	   * @param data set of test instances for evaluation
//	   * @param forPredictionsPrinting varargs parameter that, if supplied, is
//	   * expected to hold a weka.classifiers.evaluation.output.prediction.AbstractOutput
//	   * object
//	   * @return the predictions
//	   * @throws Exception if model could not be evaluated
//	   * successfully
//	   */
//	  public double[] evaluateModel(Classifier classifier,
//	                                Instances testData,
//	                                Object... forPredictionsPrinting) throws Exception 
//	  {
//		    StringBuilder sb = new StringBuilder();
//		    double [] dist = new double[testData.size()];
//			double error = 0, totSize = 0;
//			int minorityErrCnt = 0, majorityErrCnt = 0;
//			int minorityCnt = 0, majorityCnt = 0;
//			int    idx   = 0;
//			for(Instance instance : testData)
//			{
//				double pred = classifier.classifyInstance(instance);
//				dist[idx++] = pred;
//				double real = instance.classValue();
//				if (real == 0)
//				{
//					totSize += 1 * mainFrame.rateFor2Class;
//					minorityCnt++;
//				}
//				else
//				{
//					totSize += 1;
//					majorityCnt++;
//				}
//				
//				if (pred != real) // predict error
//				{
//					if (real == 0)  // minority class
//					{
//						error += mainFrame.rateFor2Class;
//						minorityErrCnt++;
//					}
//					else
//					{
//						error += 1;
//						majorityErrCnt++;
//					}
//				}
//			}
//			
//			// accuracy & other statisticals
//			double accuracy = 1 - error/totSize;
//			sb.append("classifier's accuracy is " + accuracy 
//					+ ", minority(err/cnt) " + minorityErrCnt + "/" + minorityCnt  
//					+ ", majority(err/cnt) " + majorityErrCnt + "/" + majorityCnt  
//					+ ", total cnt " + testData.size() 
//					);	
//			results.init(minorityErrCnt, minorityCnt, majorityErrCnt, majorityCnt, accuracy, testData.size() , sb.toString());
//			
//			return dist;
//			
//			
//	  }
}
