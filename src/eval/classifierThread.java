package eval;

import Share.log;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class classifierThread extends Thread {
//	Evaluation eval;
	Classifier classifier;
	Instances train, test;
//	Object[] forPredictionsPrinting;
	public int corrCnt     = 0;
	public int totCnt      = 0;
	public double accuracy = 0;
	public int threadId = 0;
	
	
	public classifierThread(Classifier classifier, Instances train, Instances test)
	{
		this.threadId = multiThreadEval.getThreadId();
		this.classifier = classifier;
		this.train = train;
		this.test = test;
		this.totCnt = test.size();
	}
	
	@Override
	public final void run() 
	{
		try
		{
			Classifier copiedClassifier = AbstractClassifier.makeCopy(classifier);
			copiedClassifier.buildClassifier(train);
//			log.print("The " + threadId + "th classifier is built!!!");
//			accuracy = getAccuracy(copiedClassifier, test);
//			classifier = AbstractClassifier.makeCopy(classifier);
//			classifier.buildClassifier(train);
			log.print("The " + threadId + "th classifier is built!!!");
			accuracy = getAccuracy(copiedClassifier, test);
		}
		catch (Exception e)
		{
			log.print(e.getStackTrace().toString());
			log.print(e.toString());
		}	
		multiThreadEval.finishOneThreads();
		log.print("The " + threadId + "th thread is finshed! accuracy = " + accuracy);
	}
	
	
	public double getAccuracy(Classifier classifier, Instances test) throws Exception
	{
		for(Instance instance : test)
		{
			int predClass = (int) classifier.classifyInstance(instance);
			int realClass = (int) instance.classValue();
			if (predClass == realClass)
			{
				corrCnt++;
			}
		}
		return (double)corrCnt / totCnt;
	}
}
