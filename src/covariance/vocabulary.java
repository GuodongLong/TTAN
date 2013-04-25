package covariance;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import Share.Comm;


public class vocabulary {

	public static HashMap<Integer, String> terms = new HashMap<Integer, String>();
	
	public static void init() throws IOException
	{
		File file = new File(Comm.dataPath + "labeled_Nrm\\vocabulary.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		int idx = 1;
		while((line = br.readLine()) != null)
		{
			if (line.length() < 1)
			{
				continue;
			}
			String[] str = line.split(",");
			int key = idx;
			String value =str[0]; 
			terms.put(key, value);
			
			idx++;
		}
		br.close();
		fr.close();
	}
	
	public static String getTerm(int key)
	{
		if (terms.size() == 0)
		{
			try {
				init();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String value = terms.get(key);
		if (value == null)
		{
			value = "null";
		}
		return value;
	}
}
