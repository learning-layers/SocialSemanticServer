package at.kc.tugraz.ss.recomm.impl.processing;

import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ThreeLayersCalculator {

	public static void predictSample(String filename, String rPath, boolean predictTags) {
		
    filename += "_res";

		try {
			String scriptName;
			if (predictTags) {
				scriptName = "3Layers_inMemory.R";
			} else {
				scriptName = "3Layers_resources.R";
			}
//   filePath:   "/home/dkowald/LinkPredictionFramework/data/"; //"C:/Users/dkowald/Projekte/WikiAnalyzer/LinkPredictionFramework/data/";
			//String[] commandArgs = new String[4];
			//commandArgs[0] = rPath + "Rscript ./src/processing/" + scriptName;
			//commandArgs[1] = filename;
			//commandArgs[2] = filePath;
			//commandArgs[3] = prefix; // DEP: some prefix for temp-files
			String commandLine = rPath + "Rscript" +  SSFileU.dirWorkingScriptR() + scriptName + SSStrU.blank + filename + SSStrU.blank + SSFileU.dirWorkingData() + SSStrU.blank + "layers";
			//System.out.println(commandLine);
			Process proc = Runtime.getRuntime().exec(commandLine);
			
		    BufferedReader bro = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		    String line;
		    while ((line = bro.readLine()) != null) {
		    	System.out.println(line);
		    }
		    BufferedReader bre = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		    String oline;
		    while ((oline = bre.readLine()) != null) {
		    	System.out.println(oline);
		    }
		    
		    bro.close();
		    bre.close();
			System.out.println(proc.waitFor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*
		DEP: Version with temp-files
		File f1 = new File("CatComb_" + prefix + ".txt");
		if (!f1.delete()) {
			System.out.println("Error while deleting CatComb");
		}
		File f2 = new File("Level3_" + prefix + ".txt");
		if (!f2.delete()) {
			System.out.println("Error while deleting Level3");
		}
		File f3 = new File("TagDist_" + prefix + ".txt");
		if (!f3.delete()) {
			System.out.println("Error while deleting TagDist");
		}
		File f4 = new File("weights_" + prefix + ".txt");
		if (!f4.delete()) {
			System.out.println("Error while deleting weights");
		}
		*/
	}
}
