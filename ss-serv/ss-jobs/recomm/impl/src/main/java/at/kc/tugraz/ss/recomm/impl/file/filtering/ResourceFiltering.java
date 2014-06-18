package at.kc.tugraz.ss.recomm.impl.file.filtering;

import at.kc.tugraz.ss.recomm.impl.file.WikipediaReader;

public class ResourceFiltering {
	
	private WikipediaReader reader;
	
	public ResourceFiltering(WikipediaReader reader) {
		this.reader = reader;
	}
	
	// dkowald: solve
	/*
	public WikipediaReader filterOrphans(int level, boolean tagLevel) {
		List<Integer> resCountList = (tagLevel ? this.reader.getResourceTagCounts() : this.reader.getResourceCounts());
		List<Integer> removeIDs = new ArrayList<>();
		for (int resID = 0; resID < resCountList.size(); resID++) {
			if (resCountList.get(resID) < level) {
				removeIDs.add(resID);
			}
		}
		
		List<UserData> removeData = new ArrayList<UserData>();
		for (UserData data : this.reader.getUserLines()) {
			int resID = this.reader.getResources().indexOf(data.getWikiID());
			if (removeIDs.contains(resID)) {
				removeData.add(data);
			}
		}
		//removeTags(removeData);
		this.reader.getUserLines().removeAll(removeData);
		
		return this.reader;
	}
	
	// dkowald: check for bug
	// only used to output the remaining numbers of tags
	private void removeTags(List<UserData> removeData) {
		Set<String> tags = new HashSet<String>();
		for (UserData data : removeData) {
			for (int id : data.getTags().keySet()) {
				String tag = this.reader.getTags().get(id);
				tags.add(tag);
			}
		}
		this.reader.getTags().removeAll(tags);
	}
	*/
}
