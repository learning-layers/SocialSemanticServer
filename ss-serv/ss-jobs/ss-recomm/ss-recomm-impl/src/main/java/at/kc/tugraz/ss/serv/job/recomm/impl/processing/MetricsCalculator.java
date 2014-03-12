package at.kc.tugraz.ss.serv.job.recomm.impl.processing;

import at.kc.tugraz.ss.serv.job.recomm.impl.common.PredictionData;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import at.kc.tugraz.ss.serv.job.recomm.impl.file.PredictionFileReader;

public class MetricsCalculator {

	private PredictionFileReader reader;	
	private double recall;
	private double precision;
	private double fMeasure;
	private double mrr;
	private double map;
	
	// used for averages
	public static double precisionSum = 0.0;
	public static double recallSum = 0.0;
	public static double fMeasureSum = 0.0;
	public static double mrrSum = 0.0;
	public static double mapSum = 0.0;
	
	public MetricsCalculator(PredictionFileReader reader, String outputFile, int k) {
		this.reader = reader;
		BufferedWriter bw = null;
		//if (k == 10) {
			try {
				FileWriter writer = new FileWriter(new File(outputFile), true);
				bw = new BufferedWriter(writer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		//}
		
		int count = this.reader.getPredictionCount();//this.reader.getPredictionData().size();
		double recall = 0.0, precision = 0.0, mrr = 0.0, fMeasure = 0.0, map = 0.0;
		for (PredictionData data : this.reader.getPredictionData()) {
			recall += data.getRecall();
			precision += data.getPrecision();
			fMeasure += data.getFMeasure();
			mrr += data.getMRR();
			map += data.getMAP();
			
			if (bw != null) {
				try {
					bw.write(Double.toString(data.getRecall()).replace('.', ',') + ";");
					bw.write(Double.toString(data.getPrecision()).replace('.', ',') + ";");
					bw.write(Double.toString(data.getFMeasure()).replace('.', ',') + ";");
					bw.write(Double.toString(data.getMRR()).replace('.', ',') + ";");
					bw.write(Double.toString(data.getMAP()).replace('.', ',') + "\n");			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		this.recall = recall / count;
		this.precision = precision / count;
		this.fMeasure = fMeasure / count;
		this.mrr = mrr / count;
		this.map = map / count;
		if (bw != null) {
			try {
				bw.flush();
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public double getRecall() {
		return this.recall;
	}
	
	public double getPrecision() {
		return this.precision;
	}
	
	public double getFMeasure() {
		return this.fMeasure;
	}
	
	public double getMRR() {
		return this.mrr;
	}
	
	public double getMAP() {
		return this.map;
	}
	
	// Statics ----------------------------------------------------------------------------------------------------------------------
	
	public static void calculateMetrics(String filename, int k, String outputFile, boolean endline) {
		PredictionFileReader reader = new PredictionFileReader();
		reader.readFile(filename, k);
		
		MetricsCalculator calc = new MetricsCalculator(reader, "./data/metrics/" + outputFile + "_all.txt", k);
		recallSum += calc.getRecall();
		precisionSum += calc.getPrecision();
		fMeasureSum += calc.getFMeasure();
		mrrSum += calc.getMRR();
		mapSum += calc.getMAP();
		/*
		if (outputFile != null) {
			try {
				FileWriter writer = new FileWriter(new File("./data/metrics/" + outputFile + "_all.txt"), true);
				BufferedWriter bw = new BufferedWriter(writer);
				bw.write(Double.toString(calc.getRecall()).replace('.', ',') + ";");
				bw.write(Double.toString(calc.getPrecision()).replace('.', ',') + ";");
				bw.write(Double.toString(calc.getFMeasure()).replace('.', ',') + ";");
				bw.write(Double.toString(calc.getMRR()).replace('.', ',') + ";");
				bw.write(Double.toString(calc.getMAP()).replace('.', ',') + (endline ? "\n" : ";"));			
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		*/
	}
	
	public static void writeAverageMetrics(String outputFile, int k, double size) {
		try {
			FileWriter writer = new FileWriter(new File("./data/metrics/" + outputFile + "_avg.txt"), true);
			BufferedWriter bw = new BufferedWriter(writer);		
			bw.write(Double.toString((recallSum / size)).replace('.', ',') + ";");		
			bw.write(Double.toString((precisionSum / size)).replace('.', ',') + ";");		
			bw.write(Double.toString((fMeasureSum / size)).replace('.', ',') + ";");		
			bw.write(Double.toString((mrrSum / size)).replace('.', ',') + ";");		
			bw.write(Double.toString((mapSum / size)).replace('.', ',') + "\n");		
			bw.close();
			
			resetMetrics();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void resetMetrics() {
		recallSum = 0.0;
		precisionSum = 0.0;
		fMeasureSum = 0.0;
		mrrSum = 0.0;
		mapSum = 0.0;
	}
}
