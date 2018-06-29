package gaitanalyser;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.parser.ParseException;

import gaitanalyser.data.GaitDataGenerationTrial1;
import gaitanalyser.data.GaitDataGenerationTrial2;
import gaitanalyser.data.GaitDataReception;
import gaitanalyser.data.GaitDataOctaveGeneration;
import gaitanalyser.data.GaitDataReportGeneration;


public class GaitAnalyser {

	public static void performGaitAnalysis(String absolutePath, String trial, String makLeg, String footSize) throws Exception{

		try {

			System.out.println("Starting Gait Data Generation...");

			if(trial.equals("TRIAL_1")){

				GaitDataGenerationTrial1 gaitData = new GaitDataGenerationTrial1(absolutePath, footSize, makLeg, 50);
				gaitData.generateOutputFiles();

			}else {

				GaitDataReception receptionDatas = new GaitDataReception(absolutePath, true, 17);
				receptionDatas.generateTxtFile();	 				  
				GaitDataGenerationTrial2 gaitData = new GaitDataGenerationTrial2(absolutePath + ".without_errors.txt", footSize, makLeg, 1000);
				gaitData.generateOutputFiles();						
			}
			System.out.println("Trial Generation Done.");


			System.out.println("Starting Plot Generation...");
			GaitDataOctaveGeneration octavePLots = new GaitDataOctaveGeneration(trial, makLeg);
			octavePLots.generateOctavePlots(absolutePath);		   
			System.out.println("Plot Generation Done.");
			GaitDataReportGeneration report = new GaitDataReportGeneration(absolutePath);
			report.generateReport();

		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String args[]) throws IOException, InvalidFormatException, ParseException, Exception {
		
		
		String absolutePath = "D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\MAK-HAM-P2-Paciente1-SG-M4";
		
		String trial = "TRIAL_1"; 
		String makLeg = "L"; 
		String footSize = "L";
		
		GaitAnalyser.performGaitAnalysis(absolutePath, trial, makLeg, footSize);
		
	}
	
	
	
	
	
	

}

