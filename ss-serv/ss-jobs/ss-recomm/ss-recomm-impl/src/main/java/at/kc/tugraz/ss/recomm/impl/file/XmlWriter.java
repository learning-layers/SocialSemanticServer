package at.kc.tugraz.ss.recomm.impl.file;

import at.kc.tugraz.ss.recomm.impl.common.Utilities;

public class XmlWriter {
	
	private int labelCount;
	private String labelString;
	
	public XmlWriter(int labelCount) {
		this.labelCount = labelCount;
		this.labelString = initializeLabels();
	}
	
	private String initializeLabels() {
		String labelString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<labels xmlns=\"http://mulan.sourceforge.net/labels\">\n";
		for (int i = 0; i < this.labelCount; i++) {
			labelString += ("<label name=\"label_" + i + "\"></label>\n");
		}
		return (labelString + "</labels>\n");
	}
	
	public boolean writeFile(String filename) {		
		return Utilities.writeStringToFile("./data/arff/" + filename + ".xml", this.labelString);
	}
 }
