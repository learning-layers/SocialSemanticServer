package at.kc.tugraz.ss.recomm.impl.file.filtering;

import at.kc.tugraz.ss.recomm.impl.file.WikipediaReader;

public class TagFiltering {
	
	private WikipediaReader reader;
	
	public TagFiltering(WikipediaReader reader) {
		this.reader = reader;
	}
	
	// dkowald: solve
	/*
	public WikipediaReader filterOrphans(int level) {
		List<Integer> removeIDs = new ArrayList<Integer>();
		for (int tagID = 0; tagID < this.reader.getTagCounts().size(); tagID++) {
			if (this.reader.getTagCounts().get(tagID) < level) {
				removeIDs.add(tagID);
			}
		}
		
		List<UserData> removeData = new ArrayList<UserData>();
		for (UserData data : this.reader.getUserLines()) {
			for (int id : removeIDs) {
				data.getTags().remove(id);
			}
			if (data.getTagSequence().size() == 0) {
				removeData.add(data);
			}
		}
		this.reader.getUserLines().removeAll(removeData);		
		//removeTags(removeIDs);
		
		return this.reader;
	}
	
	// dkowald: check
	private void removeTags(List<Integer> removeIDs) {
		Collections.sort(removeIDs);
		for (int i = removeIDs.size() - 1; i >= 0; i--) {
			this.reader.getTags().remove(removeIDs.get(i));
			this.reader.getTagCounts().remove(removeIDs.get(i));
		}
	}
	*/
}
