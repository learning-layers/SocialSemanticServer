package at.kc.tugraz.ss.serv.job.recomm.impl.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import at.kc.tugraz.ss.serv.job.recomm.impl.processing.ThreeLTCalculator;
import at.kc.tugraz.ss.serv.job.recomm.impl.file.WikipediaReader;

public class ThreeLayersEngine {
  
  private WikipediaReader reader = null;
  private ThreeLTCalculator calculator = null;
  private List<String> topTags = new ArrayList<String>();
  
  public void loadFile(String filename) throws Exception{
    
    WikipediaReader reader = new WikipediaReader(0, false);
    
    //CHANGED dtheiler: throwing exception; re-implemented if exception handling is necessary here
    reader.readFile(filename);
    
    Collections.sort(reader.getUserLines());
    //System.out.println("read in and sorted file");
    
    ThreeLTCalculator calculator = new ThreeLTCalculator(reader, reader.getUserLines().size(), 5, 5, true, true, false);
    
    resetStructure(reader, calculator);
  }
  
  public synchronized List<String> getTags(String user, String resource, List<String> topics, int limit, boolean timeBased) {
    List<String> tags = new ArrayList<String>();
    if (this.reader == null || this.calculator == null) {
      return tags;
    }
    int userID = this.reader.getUsers().indexOf(user);
    int resID = this.reader.getResources().indexOf(resource);
    List<Integer> topicIDs = new ArrayList<Integer>();
    if (topics != null) {
      for (String t : topics) {
        int tID = this.reader.getCategories().indexOf(t);
        if (tID != -1) {
          topicIDs.add(tID);
        }
      }
    }
    
    Map<Integer, Double> tagIDs = this.calculator.getRankedTagList(userID, resID, topicIDs, System.currentTimeMillis() / 1000.0, limit, timeBased, false);
    for (Map.Entry<Integer, Double> tEntry : tagIDs.entrySet()) {
      tags.add(this.reader.getTags().get(tEntry.getKey()));
    }
    if (tags.size() < limit) {
      for (String t : this.topTags) {
        if (tags.size() < limit) {
          if (!tags.contains(t)) {
            tags.add(t);
          }
        } else {
          break;
        }
      }
    }
    return tags;
  }
  
  public synchronized void resetStructure(WikipediaReader reader, ThreeLTCalculator calculator) {
    this.reader = reader;
    this.calculator = calculator;
    this.topTags = EngineUtils.calcTopTags(this.reader);
  }
}
