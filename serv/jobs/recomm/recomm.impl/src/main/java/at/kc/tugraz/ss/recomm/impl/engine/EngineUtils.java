package at.kc.tugraz.ss.recomm.impl.engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import at.kc.tugraz.ss.recomm.impl.common.IntMapComparator;

import at.kc.tugraz.ss.recomm.impl.file.WikipediaReader;

public class EngineUtils {

	public static List<String> calcTopTags(WikipediaReader reader) {
		List<String> tagList = new ArrayList<>();
		Map<Integer, Integer> countMap = new LinkedHashMap<Integer, Integer>();
		for (int i = 0; i < reader.getTagCounts().size(); i++) {
			countMap.put(i, reader.getTagCounts().get(i));
		}
		Map<Integer, Integer> sortedCountMap = new TreeMap<Integer, Integer>(new IntMapComparator(countMap));
		sortedCountMap.putAll(countMap);
		for (Integer key : sortedCountMap.keySet()) {
			tagList.add(reader.getTags().get(key));
		}
		return tagList;
	}
}
