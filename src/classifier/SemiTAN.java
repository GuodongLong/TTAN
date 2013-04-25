package classifier;

import java.util.Random;

import eval.multiThreadEval;
import framework.DataLoader;
import framework.Sparse;
import framework.mainFrame;
import Share.Comm;
import Share.FileOption;
import Share.log;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.RandomTree;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;

public class SemiTAN  extends AbstractClassifier  {

	public static int globeThreadId = 0;
	public int threadId = 0;
	public Classifier finalClassifier = null;
	public Instances testData = null;
//	public double  kl = 0.874, w1 = kl / (1 + kl), w2 = 1 - w1;
	public double w1 = 0.1, w2 = 0.9;
	public SemiTAN()
	{
		super();
		threadId = globeThreadId;
		globeThreadId++;
	}
	
	@Override
	public void buildClassifier(Instances data) throws Exception {
		// TODO Auto-generated method stub		
//		Instances D_l = TTAN.D_l;
//		Instances D_u = new Instances(data);
		Instances testData = new Instances(SemiTAN.D_u);
		Instances unlabeledData = new Instances(data);

		// initial the labels for unlabeled data
//		for(Instance instance : D_u)
//		{
//			double predictClass = TTAN.tan_l.classifyInstance(instance);
//			instance.setClassValue(predictClass);
//		}

		int errCnt = 0, modified = 0;
		for(Instance instance : unlabeledData)
		{
//			double[] dist = SemiTAN.tan_l.distributionForInstance(instance);
//			double predictClass = 0;
//			if (dist[1] > dist[0])
//			{
//				predictClass = 1;
//			}
			double predictClass = SemiTAN.tan_l.classifyInstance(instance);
			double realClass = instance.classValue();
			if(predictClass != realClass)
			{
				errCnt++;
			}
			instance.setClassValue(predictClass);
		}
		double accuracy = 1 - (double) errCnt / unlabeledData.size();
		log.print("errCnt = " + errCnt + ",accuracy=" + accuracy);
		
		// 2. EM iteration
		for(int iter = 0; iter < Comm.MAX_ITERATION; iter++)
		{
			errCnt = 0;
			modified = 0;
			accuracy = 0;
			
			// 2.1. integrate two datasets: D_l, D_u
			Instances trainData = new Instances(D_l);
			trainData.addAll(unlabeledData);
		
			// 2.2. generate a tree based on two datasets, 
//			Classifier cl = new TAN();
//			Classifier cl = new NaiveBayes();
			Classifier cl = new NB();
			cl.buildClassifier(trainData);
			finalClassifier = cl;
		
			// 2.2. update the labels for D_u
			for(Instance instance : unlabeledData)
			{
				double predictClass = cl.classifyInstance(instance);
				double currClass = instance.classValue();
				if (predictClass != currClass)
				{
					modified++;
				}
				instance.setClassValue(predictClass);
			}
			for(Instance instance : testData)
			{
				double predictClass = cl.classifyInstance(instance);
				double realClass = instance.classValue();
				if (predictClass != realClass)
				{
					errCnt++;
				}
			}
			accuracy = 1 - (double) errCnt / testData.size();
			log.print("The " + iter + "th iteration with modified = " + modified + ", errCnt = " + errCnt + ", accuracy = " + accuracy);
			if ( (modified == 0))
			{
				log.print("Quit while modified = " + modified + " or errCnt = " + errCnt);
				break;
			}
		}
	}

	@Override
	public double classifyInstance(Instance instance) throws Exception {
		// TODO Auto-generated method stub
		return finalClassifier.classifyInstance(instance);
	}

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		// TODO Auto-generated method stub
		return finalClassifier.distributionForInstance(instance);
	}

	@Override
	public Capabilities getCapabilities() {
		// TODO Auto-generated method stub
		return finalClassifier.getCapabilities();
	}



	public static String labeledFile  = FileOption.CS_IN_DOMAIN_Nominal;
	public static String unlabeledFile = FileOption.CS_OUT_DOMAIN_Nominal;

//	public static String labeledFile  = FileOption.CS_IN_DOMAIN;
//	public static String unlabeledFile = FileOption.CS_OUT_DOMAIN;

	public static Instances D_l, D_u;
	public static Classifier tan_l = null;
	public static void main(String[] args) throws Exception
	{
		// load data
		D_l = DataLoader.loadDataFromFile(labeledFile);
		D_u = DataLoader.loadDataFromFile(unlabeledFile);
//		D_u = Sparse.removeValues(D_u);
		
//		tan_l = new TAN();
//		tan_l = new NaiveBayes();
		tan_l = new NB();
		tan_l.buildClassifier(D_l);
		
//		Instances instances = new Instances(D_u);
//		instances.addAll(D_u);
//		Classifier cl = new TTAN();
//		Classifier cl = new NaiveBayes();
		Classifier cl = new SemiTAN();
		cl.buildClassifier(D_u);
		
//		cl.buildClassifier(D_u);
		Evaluation eval = new Evaluation(D_u);
		eval.evaluateModel(cl, D_u);
	    log.print(eval.toSummaryString());
		
//		multiThreadEval eval = new multiThreadEval(D_u);
//	    eval.crossValidateModel(cl, D_u, 10, new Random(1));
	}
}
