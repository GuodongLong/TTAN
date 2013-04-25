package covariance;
import java.util.HashMap;


public class itemset {
	
	public HashMap<Integer, Integer> items = new HashMap<Integer, Integer>();

	public itemset(link lnk)
	{
		put(lnk.node1);
		put(lnk.node2);
	}
	
	public itemset(Integer item1, Integer item2)
	{
		put(item1);
		put(item2);
	}	
	
	public void put(Integer item)
	{
		items.put(item, item);
	}
	
	public boolean isExist(Integer item)
	{
		return (items.get(item) != null);
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(Integer it : items.keySet())
		{
			sb.append(it + ",");
		}
		sb.append("\n");
		return sb.toString();
	}
	

	public String toString(boolean isWord)
	{
		StringBuilder sb = new StringBuilder();
		for(Integer it : items.keySet())
		{
			sb.append(vocabulary.getTerm(it) + ",");
		}
		sb.append("\n");
		return sb.toString();
	}
	
}
