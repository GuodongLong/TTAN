package covariance;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Share.Comm;
import Share.log;


public class TextProcess {

	public static double min_corr = 0.01;
	
	public static void main(String[] args) throws IOException
	{
		TextProcess tp = new TextProcess();
		tp.constructGraph();
	}
	
	public void constructGraph() throws IOException
	{
		//1. read data from file.
		List<link> links = readData();
		if (links.size() == 0)
		{
			log.print("Error: the size of links is zeor");
			return;
		}

		//2. construct itemsets.
		List<itemset> itemsets = split(links);
		
		// 3. output the results.
		File output = new File(Comm.dataPath + "labeled_Nrm\\corr_graph.txt");
		FileWriter fw = new FileWriter(output);
		for(itemset itset : itemsets)
		{
			fw.write(itset.toString(true));
		}
		fw.close();
	}
	
	public List<link> readData() throws IOException
	{
		List<link> links = new ArrayList<link>();	
		File input = new File(Comm.dataPath + "labeled_Nrm\\sig_corr.txt");
		FileReader fr = new FileReader(input);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while((line = br.readLine()) != null)
		{
			if (line.length() < 2)
			{
				break;
			}
			String[] dat = line.split(" ");
			link lnk = new link(Integer.valueOf(dat[0]),Integer.valueOf(dat[1]));
			links.add(lnk);
		}
		br.close();
		fr.close();
		
		return links;
	}
	

	/**
	 * Split all links into itemsets
	 * @param lnks
	 * @return
	 */
	public List<itemset> split(List<link> lnks)
	{
		List<link> links = new ArrayList<link>();
		links.addAll(lnks);

		List<itemset> itemsets = new ArrayList<itemset>();
		itemset its = new itemset(links.get(0)); 
		itemsets.add(its);
		
		boolean ischanging = true;
		while(links.size() > 0)
		{
			// add links into itemset.
			while(ischanging)
			{
				ischanging = false;
				for(link lnk : links)
				{
					Integer item1 = lnk.node1;
					Integer item2 = lnk.node2;
					for(itemset itset : itemsets)
					{
						if (itset.isExist(item1) || itset.isExist(item2))
						{
							itset.put(item1);
							itset.put(item2);
							lnk.isremoved = true;
							ischanging = true;
						}
					}
				}

				// remove the added links
				int size = links.size();
				int idx = 0;
				for(int i = 0; i < size; i++)
				{
					link lnk = links.get(i - idx);
					if (lnk.isremoved)
					{
						links.remove(lnk);
						idx++;
					}
				}
			}
			
			// generate a new itemset = no change happen && links is not null
			if (links.size() > 0)
			{
				ischanging = true;
				itemset itset = new itemset(links.get(0));
				itemsets.add(itset);
			}
			else
			{
				log.print("error branch!" + links.size());
			}
		}
		
		return itemsets;
	}
	
	
}
