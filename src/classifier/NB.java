package classifier;

import java.util.Random;

import framework.DataLoader;
import Share.Comm;
import Share.FileOption;
import Share.log;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.RandomTree;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class NB extends AbstractClassifier {

	public double[][] wordProb;
	public int numAttr = 0, numInst = 0, numClass = 0;
	
	@Override
	public void buildClassifier(Instances data) throws Exception {
		// TODO Auto-generated method stub
		
		numAttr = data.numAttributes() - 1;
		numInst = data.size();
		numClass = data.numClasses();
		wordProb = new double[numClass][numAttr];

		int[] classDocSizes = new int[numClass];
		for(Instance instance : data)
		{
			int c = (int)instance.classValue();
			for(int i = 0; i < numAttr; i++)
			{
				if(instance.value(i) > 0)
				{
					classDocSizes[c]++;
				}
			}
		}
		
		for(int c = 0; c < numClass; c++)
		{
			for(int i = 0; i < numAttr; i++)
			{
				int occurrence = 0;
				for(Instance instance : data)
				{
					if (instance.classValue() == c)
					{
						
						if (instance.value(i) > 0)
						{
							occurrence++;
						}
					}
				}
				double prob = (double)(1 + occurrence) / (numAttr + classDocSizes[c]);
				
				wordProb[c][i] = prob;
				if (prob == 0)
				{
					log.print("occurrence=" + occurrence + ", numAttr=" + numAttr + ", classDocSizes=" + classDocSizes[c] + ",class=" + c);
				}
			}
		}
		
		
	}


	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		// TODO Auto-generated method stub
		double[] probs = new double[instance.numClasses()];
		for(int i = 0; i < probs.length; i++)
		{
			probs[i] = 1;
		}
//		double[] probs_scale = new double[instance.numClasses()];
		for(int i = 0; i < numAttr; i++)
		{
			if (instance.value(i) > 0)
			{
				for(int c = 0; c < instance.numClasses(); c++)
				{
					probs[c] *= wordProb[c][i];
					if (probs[c] < Comm.MIN_DOUBLE)
					{
						for(int k = 0; k < instance.numClasses(); k++)
						{
							probs[k] *= 1e75;
						}
					}
				}
			}
		}
		
//		log.print(probs);
		Utils.normalize(probs);
		
		return probs;
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
//		D_u = DataLoader.loadDataFromFile(unlabeledFile);
//		D_u = Sparse.removeValues(D_u);
		
//		tan_l = new TAN();
//		tan_l = new NaiveBayes();
		tan_l = new NB();
//		tan_l.buildClassifier(D_l);
		
//		Instances instances = new Instances(D_u);
//		instances.addAll(D_u);
//		Classifier cl = new TTAN();
//		Classifier cl = new NaiveBayes();
//		Classifier cl = new SemiTAN();
//		cl.buildClassifier(D_u);
		
//		cl.buildClassifier(D_u);
		Evaluation eval = new Evaluation(D_l);
		eval.crossValidateModel(tan_l, D_l, 10, new Random(1));
//		eval.evaluateModel(tan_l, D_u);
	    log.print(eval.toSummaryString());
		
//		multiThreadEval eval = new multiThreadEval(D_u);
//	    eval.crossValidateModel(cl, D_u, 10, new Random(1));
	}
	
}
