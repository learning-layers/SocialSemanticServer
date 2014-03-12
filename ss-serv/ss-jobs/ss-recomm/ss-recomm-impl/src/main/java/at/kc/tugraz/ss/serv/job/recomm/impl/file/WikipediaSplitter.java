package at.kc.tugraz.ss.serv.job.recomm.impl.file;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.serv.job.recomm.impl.common.IntMapComparator;
import at.kc.tugraz.ss.serv.job.recomm.impl.common.UserData;
import at.kc.tugraz.ss.serv.job.recomm.impl.common.Utilities;
import at.kc.tugraz.ss.serv.job.recomm.impl.file.filtering.CoreFiltering;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.primitives.Ints;

public class WikipediaSplitter {

	private WikipediaReader reader;
	
	public WikipediaSplitter(WikipediaReader reader) {
		this.reader = reader;
	}
	
	public void splitFile(String filename, int testPercentage) {
		int testUserSize = this.reader.getUserLines().size() * testPercentage / 100;
		int trainUserSize = this.reader.getUserLines().size() - testUserSize;
		Collections.shuffle(this.reader.getUserLines());
		List<UserData> userSample = this.reader.getUserLines().subList(0, trainUserSize + testUserSize);
		
		//Collections.sort(userSample);
		
		List<UserData> trainUserSample = userSample.subList(0, trainUserSize);
		List<UserData> testUserSample = userSample.subList(trainUserSize, trainUserSize + testUserSize);
			
		writeWikiSample(this.reader, trainUserSample, filename + "_train", null);
		writeWikiSample(this.reader, testUserSample, filename + "_test", null);
		writeWikiSample(this.reader, userSample, filename, null);
	}
	
	public void splitUserPercentage(String filename, int percentage) {
		List<UserData> lines = new ArrayList<UserData>();
		int userSize = this.reader.getUsers().size();
		int userLimit = userSize * percentage / 100;
		List<Integer> randomIndices = Utilities.getRandomIndices(0, userSize - 1).subList(0, userLimit);
		int currentUser = -1;
		boolean takeUser = false;
		for (UserData data : this.reader.getUserLines()) {
			if (currentUser != data.getUserID())  { // new user
				currentUser = data.getUserID();
				takeUser = randomIndices.contains(currentUser);
			}
			if (takeUser) {
				lines.add(data);
			}
		}
		
		writeWikiSample(this.reader, lines, filename + "_" + percentage + "_perc", null);
	}
	
	public void leaveLastOutSplit(String filename) {
		List<UserData> trainLines = new ArrayList<UserData>();
		List<UserData> testLines = new ArrayList<UserData>();
		int currentUser = -1, userIndex = 1, userSize = -1;
		for (UserData data : this.reader.getUserLines()) {
			if (currentUser != data.getUserID())  { // new user
				currentUser = data.getUserID();
				userSize = this.reader.getUserCounts().get(currentUser);
				userIndex = 1;
			}
			if (userIndex++ == userSize) {
				testLines.add(data);
			} else {
				trainLines.add(data);
			}
		}
		
		writeWikiSample(this.reader, trainLines, filename + "_train", null);
		writeWikiSample(this.reader, testLines, filename + "_test", null);
		trainLines.addAll(testLines);
		writeWikiSample(this.reader, trainLines, filename, null);
	}
	
	public void leaveOneRandOutSplit(String filename) {
		List<UserData> trainLines = new ArrayList<UserData>();
		List<UserData> testLines = new ArrayList<UserData>();
		int currentUser = -1, userIndex = -1, index = -1, userSize = -1;
		for (UserData data : this.reader.getUserLines()) {
			if (currentUser != data.getUserID())  { // new user
				currentUser = data.getUserID();
				userSize = this.reader.getUserCounts().get(currentUser);
				userIndex = 1;
				index = 1 + (int)(Math.random() * ((userSize - 1) + 1));
			}
			if (userIndex++ == index) {
				testLines.add(data);
			} else {
				trainLines.add(data);
			}
		}
		
		writeWikiSample(this.reader, trainLines, filename + "_train", null);
		writeWikiSample(this.reader, testLines, filename + "_test", null);
		trainLines.addAll(testLines);
		writeWikiSample(this.reader, trainLines, filename, null);
	}
	
	/**
	 * split bookmarks at given index 
	 * @param index
	 * @param filename
	 */
	public void leaveSomeOutSplit(int index, String filename) {
		List<UserData> trainLines = new ArrayList<UserData>();
		List<UserData> testLines = new ArrayList<UserData>();
		int currentUser = -1;
		int userIndex = 0;
		
		Collections.sort(this.reader.getUserLines());

		int cntUsers=0;
		
		for (UserData data : this.reader.getUserLines()) {
			if (currentUser != data.getUserID())  { // new user
				
				cntUsers++;
				/*if (cntUsers == 500) {
					break;
				}*/
				
				if (userIndex <= index && currentUser != -1) {
					     SSLogU.info(userIndex + " " + index + "not enough bookmarks from user -> no bookmarks in train-set, userid: " + reader.getUsers().get(currentUser));
				}
				currentUser = data.getUserID();
				userIndex = 0;
			}
			if (++userIndex > index) {
				trainLines.add(data);
			} else {
				testLines.add(data);
			}
		}
		
		writeWikiSample(this.reader, trainLines, filename + "_train", null);
		writeWikiSample(this.reader, testLines, filename + "_test", null);
		trainLines.addAll(testLines);
		writeWikiSample(this.reader, trainLines, filename, null);
		SSLogU.info("total users: " + cntUsers);
	}
	
	public void leavePercentageOutSplit(String filename, int percentage, boolean random) {
		List<UserData> trainLines = new ArrayList<UserData>();
		List<UserData> testLines = new ArrayList<UserData>();
		Set<Integer> indices = new HashSet<Integer>();
		int currentUser = -1, userIndex = -1, userSize = -1;
		for (int i = 0; i < this.reader.getUserLines().size(); i++) {
			UserData data = this.reader.getUserLines().get(i);
			if (currentUser != data.getUserID())  { // new user
				currentUser = data.getUserID();
				userSize = this.reader.getUserCounts().get(currentUser);
				userIndex = 1;
				indices.clear();
				int limit = (userSize - 1 < percentage ? userSize - 1 : percentage);
				if (random) {
					while (indices.size() < limit) {
						indices.add(1 + (int)(Math.random() * ((userSize - 1) + 1)));
					}
				} else {
					for (int index : getBestIndices(this.reader.getUserLines().subList(i, i + userSize), false)) {
						if (indices.size() < limit) {
							indices.add(index);
						} else {
							break;
						}
					}
				}
			}
			if (indices.contains(userIndex++)) {
				testLines.add(data);
			} else {
				trainLines.add(data);
			}
		}
		
		writeWikiSample(this.reader, trainLines, filename + "_train", null);
		writeWikiSample(this.reader, testLines, filename + "_test", null);
		trainLines.addAll(testLines);
		writeWikiSample(this.reader, trainLines, filename, null);
	}
	
	private Set<Integer> getBestIndices(List<UserData> lines, boolean rating) {
		Map<Integer, Integer> countMap = new LinkedHashMap<Integer, Integer>();
		for (int i = 0; i < lines.size(); i++) {
			UserData data = lines.get(i);
			countMap.put(i + 1, rating ? (int)data.getRating() : this.reader.getResourceCounts().get(data.getWikiID()));
		}
		Map<Integer, Integer> sortedCountMap = new TreeMap<Integer, Integer>(new IntMapComparator(countMap));
		sortedCountMap.putAll(countMap);
		return sortedCountMap.keySet();
	}
	
	// Statics -------------------------------------------------------------------------------------------------------------------------------------------
	
	public static boolean writeWikiSample(WikipediaReader reader, List<UserData> userSample, String filename, List<int[]> catPredictions) {
		try {
			FileWriter writer = new FileWriter(new File("./data/csv/" + filename + ".txt"));
			BufferedWriter bw = new BufferedWriter(writer);
			int userCount = 0;
			
			for (UserData userData : userSample) {
				bw.write("\"" + reader.getUsers().get(userData.getUserID()) + "\";");
				bw.write("\"" + reader.getResources().get(userData.getWikiID()) + "\";");
				bw.write("\"" + userData.getTimestamp() + "\";\"");
				int i = 0;
				for (int tag : userData.getTags()) {
					bw.write(URLEncoder.encode(reader.getTags().get(tag), "UTF-8"));
					if (++i < userData.getTags().size()) {
						bw.write(',');
					}					
				}
				bw.write("\";\"");
				
				List<Integer> userCats = (catPredictions == null ? 
						userData.getCategories() : Ints.asList(catPredictions.get(userCount++)));
				i = 0;
				for (int cat : userCats) {
					bw.write(URLEncoder.encode((catPredictions == null ? reader.getCategories().get(cat) : reader.getTags().get(cat)), "UTF-8"));
					if (++i < userCats.size()) {
						bw.write(',');
					}					
				}
				bw.write("\"");
				if (userData.getRating() != -2) {
					bw.write(";\"" + userData.getRating() + "\"");
				}
				bw.write("\n");
			}
			
			bw.flush();
			bw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void splitSample(String filename, String sampleName, int count, int userLevel, int resLevel, int tagLevel) throws Exception {
		
		String resultfile = sampleName + "_core_u" + userLevel + "_r" + resLevel + "_t" + tagLevel;
		WikipediaReader reader = new WikipediaReader(0, false);
		reader.readFile(filename);		
		
		System.out.println("Unique users before filtering: " + reader.getUsers().size());
		System.out.println("Unique resources before filtering: " + reader.getResources().size());
		System.out.println("Unique tags before filtering: " + reader.getTags().size());
		System.out.println("Lines before filtering: " + reader.getUserLines().size());
		if (userLevel > 0 || resLevel > 0 || tagLevel > 0) {		
			int i = 0;
			while (true) {
				System.out.println("Core iteration: " + i);
				int size = reader.getUserLines().size();
				CoreFiltering filtering = new CoreFiltering(reader);
				reader = filtering.filterOrphansIterative(userLevel, resLevel, tagLevel);
				String coreResultfile = resultfile + "_c" + ++i;
				writeWikiSample(reader, reader.getUserLines(), coreResultfile, null);
				if (reader.getUserLines().size() >= size) {
					break;
				}
				
				// re-read the filtered dataset			
				reader = new WikipediaReader(0, false);
				reader.readFile(coreResultfile);
				File file = new File("./data/csv/" + coreResultfile + ".txt");
				file.delete();
			}			
		} else {		
			WikipediaSplitter splitter = new WikipediaSplitter(reader);
			// dkowald
			Collections.sort(reader.getUserLines());
			for (int i = 1; i <= count; i++) {
				//splitter.splitFile(sampleName + "_" + i + "_res", 10);
				//splitter.leavePercentageOutSplit(sampleName + "_" + i + "_res", 10, false);
				//splitter.leaveOneRandOutSplit(sampleName + "_" + i + "_res");
				splitter.leaveLastOutSplit(sampleName + "_" + i + "_res");
				//splitter.splitUserPercentage(filename, 3);
			}
		}
	}
}
