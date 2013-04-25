package framework;

import java.util.Enumeration;
import java.util.Random;

import classifier.TTAN;

import eval.multiThreadEval;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import Share.Comm;
import Share.FileOption;
import Share.log;

public class mainFrame {

//	public static String fileName = FileOption.NRM_file_2class_nominal;
	public static String labeledFile  = FileOption.CS_OUT_DOMAIN_Nominal;
	public static String unlabeledFile = FileOption.CS_IN_DOMAIN_Nominal;

	public static Instances D_l, D_u;
	
	public static void main(String[] args) throws Exception
	{
		// load data
		D_l = DataLoader.loadDataFromFile(labeledFile);
		D_u = DataLoader.loadDataFromFile(unlabeledFile);
		Comm.ATTR_SIZE = D_l.numAttributes() - 1;
		 
	    // train classifier
//	    Classifier cl = new NaiveBayes();
//	    cl.buildClassifier(D_l);
//	    int corrCnt = 0;
//		for(Instance instance : D_u)
//		{
//			double predictClass = cl.classifyInstance(instance);
//			double realClass = instance.classValue();
//			if (predictClass == realClass)
//			{
//				corrCnt++;
//			}
//			instance.setClassValue(predictClass);
//		}
//		double accuracy = (double)corrCnt / D_u.size();
//	    log.print("accuracy = " + accuracy);
		
		Classifier cl = new TTAN();
	    Evaluation eval = new multiThreadEval(D_u);
	    eval.crossValidateModel(cl, D_u, 10, new Random(1));
	    log.print("accuracy = " + ((multiThreadEval)eval).accuracy);
	}
	
	
	/**
	 * It is used to check which type the attribute is.
	 * @param trainData
	 */
	public void checkAttribute(Instances trainData)
	{
		Instances m_Instances = trainData;
	    int attIndex = 0;
	    Enumeration enu = m_Instances.enumerateAttributes();
	    while (enu.hasMoreElements()) {
	        Attribute attribute = (Attribute) enu.nextElement();
		    for (int j = 0; j < m_Instances.numClasses(); j++) {
			switch (attribute.type()) {
				case Attribute.NUMERIC: 
					log.print("Numeric");
				    break;
				case Attribute.NOMINAL:
					log.print("Nominal");
				    break;
				default:
					log.print("default");
				    break;
				}
		    }
	    }
	}
	

	
}
