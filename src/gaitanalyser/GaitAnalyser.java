package gaitanalyser;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import gaitanalyser.data.GaitDataGenerationTrial1;
import gaitanalyser.data.GaitDataGenerationTrial2;
import gaitanalyser.data.GaitDataReception;
import gaitanalyser.data.GaitDataOctaveGeneration;
import gaitanalyser.data.GaitDataReportGeneration;


/**
	The  GaitAlalyser class constitute the entry point to the application. GaitAnalyser.java bases its
	functionality on providing the appropriate inputs and the sequential execution of
	each of the classes involved. 
*/
public class GaitAnalyser {

	/**
	 * Performs gait analysis execution proccess.
	 *
	 * @param absolutePath The input file absolute path 
	 * @param trial the trial number (TRIAL_1|TRIAL_2)
	 * @param makLeg the mak leg (L|R)
	 * @param footSize the foot size (S|M|L|XL)
	 * @throws Exception the exception
	 */
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
			System.out.println("Gait Data Generation Done.");


			System.out.println("Plot Generation proccess");
			GaitDataOctaveGeneration octavePLots = new GaitDataOctaveGeneration(trial, makLeg);
			octavePLots.generateOctavePlots(absolutePath);		   

		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Generate report.
	 *
	 * @param absolutePath The input file absolute path 
	 * @throws InvalidFormatException the invalid format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateReport(String absolutePath) throws InvalidFormatException, IOException {
		GaitDataReportGeneration report = new GaitDataReportGeneration(absolutePath);
		report.generateReport();		
	}
	
}

