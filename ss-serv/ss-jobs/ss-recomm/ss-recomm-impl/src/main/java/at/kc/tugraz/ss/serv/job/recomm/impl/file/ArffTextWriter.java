package at.kc.tugraz.ss.serv.job.recomm.impl.file;

import at.kc.tugraz.ss.serv.job.recomm.impl.common.UserData;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ArffTextWriter {

	private WikipediaReader reader;
	private boolean learnLabels;
	private boolean learnFeatures;
	private boolean nominalOnly;
	
	public ArffTextWriter(WikipediaReader reader, boolean learnLabels, boolean learnFeatures, boolean nominalOnly) {
		this.reader = reader;
		this.learnLabels = learnLabels;
		this.learnFeatures = learnFeatures;
		this.nominalOnly = nominalOnly;
	}
	
	public boolean writeFile(String filename) {		
		try {
			if (this.nominalOnly) {
				filename += "_nom";
			}
			FileWriter writer = new FileWriter(new File("./data/arff/" + filename + ".arff"));
			BufferedWriter bw = new BufferedWriter(writer);
			writeArffHeader(bw);
			
			int i = 0;
			for (UserData userData : this.reader.getUserLines()) {
				String labels = "";
				for (int cat = 0; cat < reader.getTags().size(); cat++) {
					Integer count = userData.getTags().get(cat);
					labels += (count == null ? "0," : "1,");
				}
				
				if (this.nominalOnly) {
					bw.write(labels.substring(0, labels.length() - 1) + "\n");
				} else {
					String features = "";
					if (this.learnFeatures) {
						for (int tag = 0; tag < reader.getCategories().size(); tag++) {
							Integer count = userData.getCategories().get(tag);
							features += (count == null ? "0," : count + ",");
						}
					}
					if (this.learnLabels) {
						for (int cat = 0; cat < reader.getTags().size(); cat++) {
							Integer count = userData.getTags().get(cat);
							features += (count == null ? "0," : count + ",");
						}
					}
					bw.write(labels + features.substring(0, features.length() - 1) + "\n");
					if (++i % 100 == 0) {
						System.out.println("Wrote user: " + i);
					}
				}
			}
					
			bw.write("\n");
			bw.flush();
			bw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void writeArffHeader(BufferedWriter bw) throws IOException {
		bw.write("@relation Features\n\n");
		for (int i = 0; i < this.reader.getTags().size(); i++) {
			bw.write("@attribute label_" + i + " {1,0}\n");
		}
		if (!this.nominalOnly) {
			if (this.learnFeatures) {
				for (int i = 0; i < this.reader.getCategories().size(); i++) {
					bw.write("@attribute feature_" + i + " numeric\n");
				}
			}
			if (this.learnLabels) {
				for (int i = 0; i < this.reader.getTags().size(); i++) {
					bw.write("@attribute label_feature_" + i + " numeric\n");
				}
			}
		}
		bw.write("\n@data\n");
	}
	
	// Statics ---------------------------------------------------------------------------------------------------------------
	
	public static void writeArffFiles(String filename, int train, int test, boolean learnLabels, boolean learnFeatures, boolean nominalOnly) throws Exception  {
		filename += "_res";

		WikipediaReader readerTrain = new WikipediaReader(-1, false);
		readerTrain.readFile(filename);
		System.out.println("Train Reader size for " + filename + " : " + readerTrain.getUserLines().size());
		readerTrain.setUserLines(readerTrain.getUserLines().subList(0, train));
		WikipediaReader readerTest = new WikipediaReader(-1, false);
		readerTest.readFile(filename);
		readerTest.setUserLines(readerTest.getUserLines().subList(train, train + test));
	
		ArffTextWriter writerTrain = new ArffTextWriter(readerTrain, learnLabels, learnFeatures, nominalOnly);
		writerTrain.writeFile(filename + "_train");
		ArffTextWriter writerTest = new ArffTextWriter(readerTest, learnLabels, learnFeatures, nominalOnly);
		writerTest.writeFile(filename + "_test");
		
		if (!nominalOnly) {
			XmlWriter xmlWriterTrain = new XmlWriter(readerTrain.getTags().size());
			xmlWriterTrain.writeFile(filename + "_train");
			XmlWriter xmlWriterTest = new XmlWriter(readerTest.getTags().size());
			xmlWriterTest.writeFile(filename + "_test");
		}
	}
}
