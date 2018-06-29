package gaitanalyser;

import java.io.*;
import java.util.Properties;

import org.json.simple.parser.ParseException;

import gaitanalyser.data.GaitDataGenerationTrial1;
import gaitanalyser.data.GaitDataGenerationTrial2;
import gaitanalyser.data.GaitDataOctaveGeneration;
import gaitanalyser.data.GaitDataReception;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;


public class GaitAnalyserTests {
	
	   public static Properties properties;
	   
	   public static void initProperties() throws FileNotFoundException, IOException {
		   
		   String rootPath = System.getProperty("user.dir");
		   String configFile = rootPath +  "\\" + "gaitAnalyser.properties";
		   System.out.println(configFile);
		    
		   properties = new Properties();
		   properties.load(new FileInputStream(configFile));
				   
	   }
	   	   
	   public static void main(String args[]) throws IOException, InvalidFormatException, ParseException, Exception {
		   
		   String inputFile = "D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\MAK-HAM-TRIAL-4-DATA_SELECTION_F0\\1SG_mbd-20180516-094254_fixed.txt";
		   System.out.println("Input File: " + inputFile);
		   
		   //GaitDataReception receptionDatas = new GaitDataReception(inputFile, true, 17);
		   //receptionDatas.generateTxtFile();	 
		   GaitDataGenerationTrial1 gaitData = new GaitDataGenerationTrial1(inputFile, "L", "LEFT", 50);
		   //GaitDataGenerationTrial4 gaitData = new GaitDataGenerationTrial4(inputFile + ".without_errors.txt", "L", "LEFT", 60);
		   gaitData.generateOutputFiles();
		   //OctaveDataGeneration.generateOctavePlot(inputFile);
		   
	    }     
	   
}

