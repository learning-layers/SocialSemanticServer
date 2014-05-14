package at.kc.tugraz.ss.recomm.impl.engine;

import at.kc.tugraz.ss.recomm.impl.common.DoubleMapComparator;
import at.kc.tugraz.ss.recomm.impl.common.IntMapComparator;
import at.kc.tugraz.ss.recomm.impl.file.WikipediaReader;
import at.kc.tugraz.ss.recomm.impl.processing.ActCalculator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BaseLevelLearningEngine {

	private WikipediaReader reader;
	private final Map<String, Map<Integer, Double>> userMaps;
	private final Map<String, Map<Integer, Double>> resMaps;
	private final List<String> topTags;
  
  public BaseLevelLearningEngine(){
   
    userMaps = new HashMap<String, Map<Integer, Double>>();
    resMaps  = new HashMap<String, Map<Integer, Double>>();
    topTags  = new ArrayList<String>();
    
    reader = new WikipediaReader(0, false);
  }
	
	public void loadFile(String filename) throws Exception{
    Map<String, Map<Integer, Double>> userMaps = new HashMap<String, Map<Integer, Double>>();
		Map<String, Map<Integer, Double>> resMaps = new HashMap<String, Map<Integer, Double>>();
		WikipediaReader reader = new WikipediaReader(0, false);
    
		reader.readFile(filename);
		Collections.sort(reader.getUserLines());
		System.out.println("read in and sorted file");
		List<Map<Integer, Double>> userRecencies = ActCalculator.getArtifactMaps(reader.getUserLines(), null, false, new ArrayList<Long>(), new ArrayList<Double>(), 0.5, true);
		int i = 0;
		for (Map<Integer, Double> map : userRecencies) {
			userMaps.put(reader.getUsers().get(i++), map);
		}
		List<Map<Integer, Double>> resRecencies = ActCalculator.getArtifactMaps(reader.getUserLines(), null, true, new ArrayList<Long>(), new ArrayList<Double>(), 0.0, true);
		i = 0;
		for (Map<Integer, Double> map : resRecencies) {
			resMaps.put(reader.getResources().get(i++), map);
		}
		
		resetStructures(userMaps, resMaps, reader);
	}
	
	public synchronized List<String> getTags(String user, String resource, int count) {
		Map<Integer, Double> resultMap = new LinkedHashMap<Integer, Double>();
		Map<Integer, Double> userMap = this.userMaps.get(user);
		Map<Integer, Double> resMap = this.resMaps.get(resource);
		// user-based and resource-based
		if (userMap != null) {
			for (Map.Entry<Integer, Double> entry : userMap.entrySet()) {
				resultMap.put(entry.getKey(), entry.getValue().doubleValue());
			}
		}
		if (resMap != null) {
			for (Map.Entry<Integer, Double> entry : resMap.entrySet()) {
				double resVal = entry.getValue().doubleValue();
				Double val = resultMap.get(entry.getKey());
				resultMap.put(entry.getKey(), val == null ? resVal : val.doubleValue() + resVal);
			}
		}	
		// sort and add MP tags if necessary
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
		if (tagList.size() < count) {
			for (String t : this.topTags) {
				if (tagList.size() < count) {
					if (!tagList.contains(t)) {
						tagList.add(t);
					}
				} else {
					break;
				}
			}
		}
		
		return tagList;
	}
	
	private synchronized void resetStructures(
    Map<String, Map<Integer, Double>> userMaps,
    Map<String, Map<Integer, Double>> resMaps, 
    WikipediaReader                   reader){
    
		this.userMaps.clear(); 
    this.userMaps.putAll(userMaps);
    
		this.resMaps.clear();
    this.resMaps.putAll(resMaps);
		
    this.topTags.clear();
    this.topTags.addAll(calcTopTags());

    this.reader = reader;
	}
	
	private List<String> calcTopTags() {
		List<String> tagList = new ArrayList<String>();
		Map<Integer, Integer> countMap = new LinkedHashMap<Integer, Integer>();
		for (int i = 0; i < this.reader.getTagCounts().size(); i++) {
			countMap.put(i, this.reader.getTagCounts().get(i));
		}
		Map<Integer, Integer> sortedCountMap = new TreeMap<Integer, Integer>(new IntMapComparator(countMap));
		sortedCountMap.putAll(countMap);
		for (Integer key : sortedCountMap.keySet()) {
			tagList.add(this.reader.getTags().get(key));
		}
		return tagList;
	}
	
	private List<String> getTopTags(int size) {
		if (size > 0 && size <= this.topTags.size()) {
			return this.topTags.subList(0, size);
		}
		return this.topTags;
	}
}
