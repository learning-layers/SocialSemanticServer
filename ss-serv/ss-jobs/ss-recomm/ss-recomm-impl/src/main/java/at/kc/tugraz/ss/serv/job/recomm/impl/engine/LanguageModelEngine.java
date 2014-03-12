package at.kc.tugraz.ss.serv.job.recomm.impl.engine;

import at.kc.tugraz.ss.serv.job.recomm.impl.common.DoubleMapComparator;
import at.kc.tugraz.ss.serv.job.recomm.impl.common.IntMapComparator;
import at.kc.tugraz.ss.serv.job.recomm.impl.common.Utilities;
import at.kc.tugraz.ss.serv.job.recomm.impl.file.WikipediaReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LanguageModelEngine {

	private WikipediaReader                   reader;
	private int                               tagAssignments = 0;
	private final Map<String, Map<Integer, Double>> userMaps;
	private final Map<String, Map<Integer, Double>> resMaps;
	
  public LanguageModelEngine(){
    
    this.userMaps = new HashMap<String, Map<Integer, Double>>();
    this.resMaps  = new HashMap<String, Map<Integer, Double>>();
    
    reader = new WikipediaReader(0, false);
  }
  
	public void loadFile(String filename) throws Exception{
		Map<String, Map<Integer, Double>> userMaps = new HashMap<String, Map<Integer, Double>>();
		Map<String, Map<Integer, Double>> resMaps = new HashMap<String, Map<Integer, Double>>();
		  WikipediaReader reader = new WikipediaReader(0, false);
		reader.readFile(filename);
		
		List<Map<Integer, Integer>> userStats = Utilities.getUserMaps(reader.getUserLines());
		int i = 0;
		for (Map<Integer, Integer> map : userStats) {
			Map<Integer, Double> resultMap = new HashMap<Integer, Double>();
			for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
				resultMap.put(entry.getKey(), (double)entry.getValue() / (double)Utilities.getMapCount(map));
			}
			userMaps.put(reader.getUsers().get(i++), resultMap);
		}
		List<Map<Integer, Integer>> resStats = Utilities.getResMaps(reader.getUserLines());
		i = 0;
		for (Map<Integer, Integer> map : resStats) {
			Map<Integer, Double> resultMap = new HashMap<Integer, Double>();
			for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
				resultMap.put(entry.getKey(), (double)entry.getValue() / (double)Utilities.getMapCount(map));
			}
			resMaps.put(reader.getResources().get(i++), resultMap);
		}
		
		resetStructures(userMaps, resMaps, reader);
	}
	
	public synchronized List<String> getTags(String user, String resource, int count) {
		Map<Integer, Double> resultMap = new LinkedHashMap<Integer, Double>();
		Double userSize = 0.0;
		Map<Integer, Double> userMap = this.userMaps.get(user);
		if (userMap != null) {
			userSize = (double)userMap.size();
		}
		Double resSize = 0.0;
		Map<Integer, Double> resMap = this.resMaps.get(resource);
		if (resMap != null) {
			resSize = (double)resMap.size();
		}
		if (resMap == null && userMap == null) {
			return getTopTags(count);
		}
		
		for (int i = 0; i < this.reader.getTags().size(); i++) {
			double pt = (double)this.reader.getTagCounts().get(i) / this.tagAssignments;
			Double userVal = 0.0;
			if (userMap != null) {
				userVal = userMap.get(i);
				if (userVal == null) {
					userVal = 0.0;
				}
			}
			Double resVal = 0.0;
			if (resMap != null) {
				resVal = resMap.get(i);
				if (resVal == null) {
					resVal = 0.0;
				}
			}
			resultMap.put(i, Utilities.getSmoothedTagValue(userVal, userSize, resVal, resSize, pt));
		}
		
		Map<Integer, Double> sortedResultMap = new TreeMap<Integer, Double>(new DoubleMapComparator(resultMap));
		sortedResultMap.putAll(resultMap);		
		int i = 0;
		List<String> tagList = new ArrayList<String>();
		for (Integer key : sortedResultMap.keySet()) {
			if (i++ < count) {
				tagList.add(this.reader.getTags().get(key));
			} else {
				break;
			}
		}
		return tagList;
	}
	
	private synchronized void resetStructures(
    Map<String, Map<Integer, Double>> userMaps,
    Map<String, Map<Integer, Double>> resMaps, 
    WikipediaReader reader) {
			
    this.userMaps.clear(); 
    this.userMaps.putAll(userMaps);
    
		this.resMaps.clear();
    this.resMaps.putAll(resMaps);
		
    this.reader = reader;
		this.tagAssignments = this.reader.getTagAssignmentsCount();
	}
	
	private List<String> getTopTags(int count) {
		List<String> tagList = new ArrayList<String>();
		Map<Integer, Integer> countMap = new LinkedHashMap<Integer, Integer>();
		for (int i = 0; i < this.reader.getTagCounts().size(); i++) {
			countMap.put(i, this.reader.getTagCounts().get(i));
		}
		Map<Integer, Integer> sortedCountMap = new TreeMap<Integer, Integer>(new IntMapComparator(countMap));
		sortedCountMap.putAll(countMap);
		int i = 0;
		for (Integer key : sortedCountMap.keySet()) {
			if (i++ < count) {
				tagList.add(this.reader.getTags().get(key));
			} else {
				break;
			}
		}
		return tagList;
	}
}
