package Transform;

import java.io.FileWriter;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
//import Framework.DataInsts;
import framework.DataLoader;
import Share.FileOption;

public class TransformToNominal {
//	public static String fileName = FileOption.NRM_file_2class;
//	public static String fileName = FileOption.CS_IN_DOMAIN;
	public static String fileName = FileOption.CS_OUT_DOMAIN;
	public static void main(String[] args) throws Exception
	{
		Instances instances = DataLoader.loadDataFromFile(fileName);
		StringBuilder sb = new StringBuilder();
		
		sb.append("@relation nrm_text_nominal\n\n");
		for(int i = 0; i < instances.numAttributes()-1; i++)
		{
			Attribute attr = instances.attribute(i);
			sb.append("@attribute w_" + attr.name() + " {0,1}\n");			
		}
		sb.append("@attribute class {Minority,Majority}\n\n");
//		sb.append("@attribute class {Financial,Human,Natural,Social,Physical}\n\n");
		sb.append("@data\n\n");
		for(Instance instance : instances)
		{
			for(int i = 0; i < instance.numAttributes()-1; i++)
			{
				double value = instance.value(i);
				if (value > 0)
				{
					sb.append("1 ");
				}
				else
				{
					sb.append("0 ");
				}
			}
			
			int classValue = (int) instance.classValue();
			switch(classValue)
			{
//				case 0: sb.append("Financial");break;
//				case 1: sb.append("Human");break;
//				case 2: sb.append("Natural");break;
//				case 3: sb.append("Social");break;
//				case 4: sb.append("Physical");break;
				case 0: sb.append("Minority");break;
				case 1: sb.append("Majority");break;
				default: sb.append("NONE");break;
			}
			
			sb.append("\n");
		}

		FileWriter fw = new FileWriter(fileName.replace(".arff", "-nominal.arff"));
		fw.write(sb.toString());		
		fw.close();
	}
}
