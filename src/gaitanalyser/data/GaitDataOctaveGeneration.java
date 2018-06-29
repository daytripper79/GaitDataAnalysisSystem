package gaitanalyser.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.parser.ParseException;

import gaitanalyser.GaitAnalyserTests;

public class GaitDataOctaveGeneration{

	String octaveCmd;	
	String octaveScript;
	String octaveImage;
	String octaveFoot;

	
	public GaitDataOctaveGeneration(String trial, String foot) throws FileNotFoundException, IOException{
		
		System.out.println("OctaveDataGeneration()");
		
		String path = System.getProperty("user.dir");
		String configFile = path +  "\\" + "gaitAnalyser.properties";		    
		Properties properties = new Properties();
		properties.load(new FileInputStream(configFile));
		   
		octaveCmd =  properties.getProperty("OCTAVE_CMD");
		
		if(trial.equals("TRIAL_1")){
			octaveScript =  properties.getProperty("OCTAVE_SCRIPT_1");			
		}else if((trial.equals("TRIAL_2"))) {
			octaveScript =  properties.getProperty("OCTAVE_SCRIPT_2");			
		}
		
		System.out.println("Octave script: " + octaveScript);
				
		octaveFoot = foot;
				
	}

	public void generateOctavePlots(String inputFile) throws IOException, InterruptedException{
						
		System.out.println("generateOctavePlots()");
		
		String fileName = inputFile.substring(inputFile.lastIndexOf("\\")+1,inputFile.length());
		int fileNameIndex = fileName.indexOf(".");
		if(fileNameIndex != -1) fileName = fileName.substring(0, fileNameIndex);
		
		int filePathIndex = inputFile.lastIndexOf(fileName);
		String filePath = inputFile.substring(0,filePathIndex);
		
		String octaveOutputPath = filePath + fileName + "_FILES\\" + fileName;
		
		if(octaveOutputPath.contains(" ")){		
			octaveOutputPath = "\"" + octaveOutputPath + "\"";
		}
		
		if(octaveImage.contains(" ")){		
			octaveImage = "\"" + octaveImage + "\"";
		}
	
		String octaveExecution = octaveCmd + " " + octaveScript +  " " + octaveOutputPath + " " + octaveFoot;
		
		System.out.println("Octave command: " + octaveExecution);
		
		Process proccess = Runtime.getRuntime().exec(octaveExecution);
		proccess.waitFor();
		
	}		
	
	
	public static void main(String args[]) throws IOException, InvalidFormatException, ParseException, Exception {
		   
		   //String inputFile = "D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\MAK_HAM_TRIAL_2_PATIENS_DATA\\MAK-HAM-P2-Paciente1-SG-M4";
		   String inputFile = "D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\MAK-HAM-TRIAL-4-DATA_SELECTION_F0\\1SG_mbd-20180516-094254_fixed.txt.without_errors.txt";
		   
		   System.out.println("Start Octave plots generation...");
		   GaitDataOctaveGeneration octavePLots = new GaitDataOctaveGeneration("TRIAL_4", "L");
		   //OctaveDataGeneration octavePLots = new OctaveDataGeneration("TRIAL_4");
		   octavePLots.generateOctavePlots(inputFile);		   
		   System.out.println("Octave plots generation done.");
		   
	}     
	
	
}