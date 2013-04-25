package classifier;

/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    NaiveBayes.java
 *    Copyright (C) 1999 University of Waikato, Hamilton, New Zealand
 *
 */
		
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import eval.multiThreadEval;
import framework.DataLoader;
import framework.Normalization;
import framework.mainFrame;
import Graph.Edge;
import Graph.Node;
import Graph.NodeHelper;
import Graph.Stats;
import Graph.StatsDet;
import Graph.edgeNominal;
import Graph.graphNominal;
import Graph.nodeNominal;
import Graph.spanningTreeNominal;
import Share.Comm;
import Share.FileOption;
import Share.log;

public class TAN extends AbstractClassifier {

//	public static graphNominal minorTree = null;
	public graphNominal spTrees;
	public double[] classRatio;
	public TAN()
	{
		super();
	}
	//
	@Override
	public void buildClassifier(Instances instances)
	{		
		classRatio = new double[instances.numClasses()];
		for(Instance instance : instances)
		{
			int classValue = (int)instance.classValue();
			classRatio[classValue]++;
		}
		for(int i = 0; i < classRatio.length; i++)
		{
			classRatio[i] = classRatio[i] / instances.size(); 
		}
		// generate one spanning tree for each class
		try {
			spTrees = genSpanningTree(instances);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * only could be used to 2-class problem
	 */
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		double[] distr = new double[instance.numClasses()];
		int[] distr_scale = new int[instance.numClasses()];
		for(int i  = 0; i < distr_scale.length; i++)
		{
			distr[i] = classRatio[i];
			distr_scale[i] = 0;
		}
		
		double max = 0;
		// decompose the joint distribution, and calculate the probability that the given instance belongs to each class.
		for(int c = 0; c < instance.numClasses(); c++)
		{
			for(int i = 0; i < spTrees.numNodes(); i++)
			{
				if (instance.value(i) < Comm.MIN_DOUBLE)
				{
					continue;
				}
				Node childNode  = spTrees.nodes.get(i);
				Node parentNode = childNode.parent;
				
				double prob = 0;
				if (parentNode != null)
				{
					prob = NodeHelper.getCondPDF(spTrees, parentNode, childNode, instance, c); // P(X2|X1)
//					prob = child.getPDF(1);
				}
				else
				{
					prob = childNode.getPDF(1, c);
				}
				if(prob == 0)
				{
					log.print("prob is 0");
				}
				
				distr[c] *= prob;
//				distr[c] *= m_Distributions[i][c].getProbability(1);
				
				// Danger of probability underflow
				if ((distr[c] > 0) && (distr[c] < Comm.MIN_DOUBLE))  
				{
					distr[c] *= 1e75;
					distr_scale[c]++;
				}
			}
		}
		if (distr_scale[0] > distr_scale[1])
		{
			distr[0] = 0;
			distr[1] = 1;
		}
		else if (distr_scale[0] < distr_scale[1])
		{
			distr[0] = 1;
			distr[1] = 0;			
		}
//		else // keep the original value
//		{
//			log.print("distr has equal scale on 2 classes." + distr[0] + "," + distr[1]);
//		}

//		log.print(distr_scale);
//		log.print(distr);
//		log.print(instance.classValue());

		Normalization.Normalize(distr);
		return distr;		
	}
	
	
	/**
	 * generate spanning tree.
	 * @param instance
	 * @param idx: 0 - minority, 1 - majority
	 * @return
	 * @throws IOException 
	 */	
	public graphNominal genSpanningTree(Instances instances) throws IOException
	{	
		// init a new graph
		graphNominal newGraph = new graphNominal();
		
		log.print("instances.numAttributes()" + instances.numAttributes());
		
		// init nodes for the new graph
		List<Node> nodes = initNodes(instances);
		for(Node node : nodes)
		{
			newGraph.addNode(node);
		}
		log.print("init nodes " + newGraph.numNodes());
		
		// init edges for the new graph
		for(int i = 0; i < newGraph.numNodes(); i++)
		{
			Node node1 = newGraph.nodes.get(i);
			for(int j = i + 1; j < newGraph.numNodes(); j++)
			{
				Node node2 = newGraph.nodes.get(j);
				Stats stats = new Stats(node1, node2);
//				double tmp = stats.total.MI + 1;
//				StatsDet tt = stats.total;
//				log.print("MI = " + stats.stotal.MI + ", ( " + tt.shared00 + "," + tt.shared01 + "," + tt.shared10 + "," + tt.shared11 + ")");
				double mi = stats.total.MI;
//				if (mi > Comm.MIN_MUTUAL_INFO)
				if (stats.total.shared11 >= 20)
				{
					Edge edge = new Edge(node1, node2, stats);
					newGraph.addEdge(edge);
					node1.addEdge(edge);
					node2.addEdge(edge);
//					log.print("node1:" + edge.node1.wordId + ", node2:" + edge.node2.wordId + ", mi = " + edge.stats.total.MI);
				}
			}
			if (i%1000== 0)
			{
				log.print("init edges for node " + i);
			}
		}

		log.print("size of nodes in graph is " + newGraph.numNodes() + ", size of edges " + newGraph.numEdges());
		
		// generate spanning tree
		graphNominal spTree = spanningTreeNominal.genSpTree(newGraph);

		log.print("size of nodes in tree is  " + spTree.numNodes() + ", size of edges " + spTree.numEdges());
		
		return spTree;
	}

	@Override
	public double classifyInstance(Instance instance) throws Exception {
		// TODO Auto-generated method stub
		
	    double [] dist = distributionForInstance(instance);
	    if (dist == null) {
	      throw new Exception("Null distribution predicted");
	    }
	    switch (instance.classAttribute().type()) {
		    case Attribute.NOMINAL:
		      double max = 0;
		      int maxIndex = 0;
	
		      /* Pick up the max probability of class that the instance belongs to. */
		      for (int i = 0; i < dist.length; i++) {
		        if (dist[i] > max) {
		          maxIndex = i;
		          max = dist[i];
		        }
		      }
		      
		      if (max > 0) {
		        return maxIndex;
		      } else {
		        return Utils.missingValue();
		      }
		    case Attribute.NUMERIC:
		      return dist[0];
		    default:
		      return Utils.missingValue();
	    }
	}

	@Override
	public Capabilities getCapabilities() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Init all nodes based on the given instances
	 * @param instances
	 * @return
	 */
	public List<Node> initNodes(Instances instances)
	{
		Instances minorInsts = new Instances(instances, 0);
		Instances majorInsts = new Instances(instances, 0);
		for(Instance instance : instances)
		{
			if ((int)instance.classValue() == Comm.MINOR_CLASS)
			{
				minorInsts.add(instance);
			}
			else
			{
				majorInsts.add(instance);
			}
		} 
		log.print("minor instances " + minorInsts.size() + ", major instances " + majorInsts.size());
		
		List<Node> nodes = new ArrayList<Node>();
		for(int i = 0; i < instances.numAttributes(); i++)
		{
			nodeNominal minorNode = new nodeNominal(minorInsts, i);
			nodeNominal majorNode = new nodeNominal(majorInsts, i);
			Node node = new Node(minorNode, majorNode, i);
			nodes.add(node);
		}
		
		return nodes;
	}
	
	

	public static String labeledFile  = FileOption.CS_OUT_DOMAIN_Nominal;
	public static String unlabeledFile = FileOption.CS_IN_DOMAIN_Nominal;

	public static Instances D_l, D_u;
	
	public static void main(String[] args) throws Exception
	{
		// load data
		D_l = DataLoader.loadDataFromFile(labeledFile);
		D_u = DataLoader.loadDataFromFile(unlabeledFile);
//		Instances instances = new Instances(D_u);
//		instances.addAll(D_u);
//		Classifier cl = new TAN();
		Classifier cl = new NaiveBayes();

		cl.buildClassifier(D_l);
		Evaluation eval = new Evaluation(D_u);
		eval.evaluateModel(cl, D_u);
//		multiThreadEval eval = new multiThreadEval(D_u);
//	    eval.crossValidateModel(cl, D_u, 10, new Random(1));
	    log.print(eval.toSummaryString());
	}
}


