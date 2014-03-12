package at.kc.tugraz.ss.serv.job.recomm.impl.file.filtering;

import at.kc.tugraz.ss.serv.job.recomm.impl.common.DoubleMapComparator;
import at.kc.tugraz.ss.serv.job.recomm.impl.common.Utilities;
import at.kc.tugraz.ss.serv.job.recomm.impl.file.WikipediaReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CatDescFiltering {

	private WikipediaReader reader;
	private List<Map<Integer, Integer>> userMaps;
	private List<Set<Integer>> userResourceLists;
	
	public CatDescFiltering(WikipediaReader reader) {
		this.reader = reader;
		this.userMaps = Utilities.getUserMaps(this.reader.getUserLines());
		this.userResourceLists = Utilities.getUserResourceLists(this.reader.getUserLines());
	}
	
	public double getTRR(int userID) {
		double trr = (double)this.userMaps.get(userID).keySet().size() / (double)this.userResourceLists.get(userID).size();
		return trr;
	}
	
	public double getTPP(int userID) {
		double tpp = Utilities.getMapCount(this.userMaps.get(userID)) / (double)this.userResourceLists.get(userID).size();
		return tpp;
	}
	
	public double getOrphanRatio(int userID) {
		Map<Integer, Integer> userMap = this.userMaps.get(userID);
		int n = (int)Math.ceil((double)Collections.max(userMap.values()) / 100.0);
		int count = 0;
		for (int val : userMap.values()) {
			if (val <= n) {
				count++;
			}
		}
		return (double)count / (double)userMap.size();
	}
	
	// Statics -----------------------------------------------------------------------------------------------------------------------
	
	public static void splitSample(String filename) throws Exception {
		WikipediaReader reader = new WikipediaReader(-1, false);
		reader.readFile(filename);
		CatDescFiltering filter = new CatDescFiltering(reader);
		
		Map<Integer, Double> trrMap = new LinkedHashMap<Integer, Double>();
		List<Double> orphan = new ArrayList<Double>();
		for (int i = 0; i < reader.getUsers().size(); i++) {
			trrMap.put(i, filter.getTRR(i));
		}
		Map<Integer, Double> sortedTrrMap = new TreeMap<Integer, Double>(new DoubleMapComparator(trrMap));
		sortedTrrMap.putAll(trrMap);
		
		int splitSize = reader.getUsers().size() / 2;
		List<Double> trrList = new ArrayList<Double>(sortedTrrMap.values());
		System.out.println("TRR split value: " + trrList.get(splitSize));
	}
}
