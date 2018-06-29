package gaitanalyser.data;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.*;
import org.json.simple.parser.ParseException;

import gaitanalyser.GaitAnalyser;

public class GaitDataGenerationTrial2 extends GaitDataGeneration{
	
	
	public GaitDataGenerationTrial2(String filePath, String footSize, String makLeg, int threshold) throws IOException{

		super(filePath, footSize, makLeg);
		INSOLE_THRESHOLD = threshold;				
		SAMPLE_RATE = 10;		
	}
	

	public void channelsInitialization(){
		
		RAW_TIME = 0;		
		RAW_POSITION = 1;		
		RAW_HEEL_EXT_1 = 2;		
		RAW_HEEL_INT_1 = 3;    			
		RAW_META_INT_1 = 4;    		
		RAW_HALLUX_1 = 5;		
		RAW_META_EXT_1 = 6;		
		RAW_ARCH_1 = 7;				
		RAW_HEEL_EXT_2 = 8;		
		RAW_HEEL_INT_2 = 9;    			
		RAW_META_INT_2 = 10;    		
		RAW_HALLUX_2 = 11;		
		RAW_META_EXT_2 = 12;		
		RAW_ARCH_2 = 13;
		RAW_EULER_X = 14;		
		RAW_EULER_Y = 15;		
		RAW_EULER_Z = 16;	
		RAW_COLUMNS = 17;
		
	}
	

	public void setCalculatedDataValuesColumns(){

		TIME = 0;
		
		POSITION_ANGLE = 1;
		
		EULER_X = 2;
		EULER_Y = 3;
		EULER_Z = 4;

		HALLUX_1 = 5;  
		TOES_1 = 6;     			
		META_EXT_1 = 7;
		META_INTERM_1 = 8;
		META_INT_1 = 9;
		ARCH_1 = 10;
		HEEL_EXT_1 = 11;    
		HEEL_INT_1 = 12;
		TREAD_1 = 13;
		STEP_IN_TIME_1 = 14;
		COP_X_1 = 15;
		COP_Y_1 = 16;
		RELATIVE_COP_X_1 = 17;
		RELATIVE_COP_Y_1 = 18;
		RELATIVE_MEAN_COP_X_1 = 19;
		RELATIVE_MEAN_COP_Y_1 = 20;
		
		HALLUX_2 = 21;
		TOES_2 = 22;
		META_EXT_2 = 23;
		META_INTERM_2 = 24;
		META_INT_2 = 25;
		ARCH_2 = 26;
		HEEL_EXT_2 = 27;    
		HEEL_INT_2 = 28;
		TREAD_2 = 29;
		STEP_IN_TIME_2 = 30;	
		COP_X_2 = 31;
		COP_Y_2 = 32;
		RELATIVE_COP_X_2 = 33;
		RELATIVE_COP_Y_2 = 34;
		RELATIVE_MEAN_COP_X_2 = 35;
		RELATIVE_MEAN_COP_Y_2 = 36;
		
		DOUBLE_SUPPORT = 37;
		
		CALC_COLUMNS = 38;
		
		calc = new String[CALC_COLUMNS];
		
		calc[TIME] = "TIMESTAMP(s)";
		
		calc[POSITION_ANGLE] = "POSITION_ANGLE(°)";
		
		calc[EULER_X] = "EULER_X(°)";
		calc[EULER_Y] = "EULER_Y(°)";
		calc[EULER_Z] = "EULER_Z(°)";

		calc[HALLUX_1] = "HALLUX_MAK(millibar)";   
		calc[TOES_1] = "TOES_MAK(millibar)";     
		calc[META_EXT_1] = "META_EXT_MAK(millibar)";
		calc[META_INTERM_1] = "META_INTERM_MAK(millibar)";
		calc[META_INT_1] = "META_INT_MAK(millibar)";
		calc[ARCH_1] = "ARCH_MAK(millibar)";
		calc[HEEL_EXT_1] = "HEEL_EXT_MAK(millibar)";    
		calc[HEEL_INT_1] = "HEEL_INT_MAK(millibar)";
		calc[TREAD_1] = "TREAD_MAK";
		calc[COP_X_1] = "COP_X_MAK(mm)";
		calc[COP_Y_1] = "COP_Y_MAK(mm)";
		calc[RELATIVE_COP_X_1] = "RELATIVE_COP_X_MAK(%)";
		calc[RELATIVE_COP_Y_1] = "RELATIVE_COP_Y_MAK(%)";
		calc[RELATIVE_MEAN_COP_X_1] = "RELATIVE_MEAN_COP_X_MAK(%)";
		calc[RELATIVE_MEAN_COP_Y_1] = "RELATIVE_MEAN_COP_Y_MAK(%)";
		calc[STEP_IN_TIME_1] = "STEP_IN_TIME_MAK";
		
		calc[HALLUX_2] = "HALLUX_FREE(millibar)"; 
		calc[TOES_2] = "TOES_FREE(millibar)";     
		calc[META_EXT_2] = "META_EXT_FREE(millibar)";
		calc[META_INTERM_2] = "META_INTERM_FREE(millibar)";
		calc[META_INT_2] = "META_INT_FREE(millibar)";
		calc[ARCH_2] = "ARCH_FREE(millibar)";
		calc[HEEL_EXT_2] = "HEEL_EXT_FREE(millibar)";    
		calc[HEEL_INT_2] = "HEEL_INT_FREE(millibar)";
		calc[TREAD_2] = "TREAD_FREE";
		calc[COP_X_2] = "COP_X_FREE(mm)";
		calc[COP_Y_2] = "COP_Y_FREE(mm)";
		calc[RELATIVE_COP_X_2] = "RELATIVE_COP_X_FREE(%)";
		calc[RELATIVE_COP_Y_2] = "RELATIVE_COP_Y_FREE(%)";
		calc[RELATIVE_MEAN_COP_X_2] = "RELATIVE_MEAN_COP_X_FREE(%)";
		calc[RELATIVE_MEAN_COP_Y_2] = "RELATIVE_MEAN_COP_Y_FREE(%)";
		calc[STEP_IN_TIME_2] = "STEP_IN_TIME_FREE";	
		calc[DOUBLE_SUPPORT] = "STEP_DOUBLE_SUPPORT";
		
	}


	protected  void calculateExtraData(){

		calculatePositionAngleData();        
		calculateEulerData();

		calculateTreadThreshold(true);
		calculateInsolesData(true);		
		calculateCentreOfPressureData(true);
		calculateRelativeCentreOfPressureData(true);
		
		calculateTreadThreshold(false);
		calculateInsolesData(false);		
		calculateCentreOfPressureData(false);
		calculateRelativeCentreOfPressureData(false);
		
		calculateDoubleSupportData();

	}

	double conversionPositionAngleValue(double value) {		
		return value;	
	}
	
	public void calculateEulerData() {
		
		XSSFSheet rawSheet = workBook.getSheet("RAW");
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");
				
		XSSFRow header = sheet.getRow(0);
		header.createCell(EULER_X).setCellValue(calc[EULER_X]);
		header.createCell(EULER_Y).setCellValue(calc[EULER_Y]);
		header.createCell(EULER_Z).setCellValue(calc[EULER_Z]);

		
		for(int i = 1; i < numberOfRows; i++) {
			
			double eulerX = (rawSheet.getRow(i).getCell(RAW_EULER_X).getNumericCellValue()/10)-180;
			double eulerY = (rawSheet.getRow(i).getCell(RAW_EULER_Y).getNumericCellValue()/10)-180;
			double eulerZ = (rawSheet.getRow(i).getCell(RAW_EULER_Z).getNumericCellValue()/10)-180;
			
			sheet.getRow(i).createCell(EULER_X).setCellValue(eulerX);                       
			sheet.getRow(i).createCell(EULER_Y).setCellValue(eulerY);                       
			sheet.getRow(i).createCell(EULER_Z).setCellValue(eulerZ);                       
						
		}
		
		sheet.autoSizeColumn(EULER_X);
		sheet.autoSizeColumn(EULER_Y);
		sheet.autoSizeColumn(EULER_Z);
		
	}

	
	double conversionPressureValue(double pressureValue) {		

		if(pressureValue > INSOLE_THRESHOLD) 
			pressureValue = (pressureValue - INSOLE_THRESHOLD);
		else pressureValue = 0;
		
		return pressureValue;
	}
		
	void calculateTreadThreshold(boolean mak) {

		System.out.println("Calculate Tread Threshold");

		XSSFSheet rawSheet = workBook.getSheet("RAW");
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");
		
		setInsoles(mak);

		XSSFRow header = sheet.getRow(0);
		header.createCell(TREAD).setCellValue(calc[TREAD]);
		sheet.autoSizeColumn(TREAD);

		// Calculate threshold
		double max = 0;
		final FormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();
		for (int i = RAW_HEEL_EXT; i <= RAW_ARCH; i++) {

			CellValue cellValue = evaluator.evaluate(rawSheet.getRow(rowMin).getCell(i));    	    
			double currentValue = cellValue.getNumberValue();
			if(currentValue > max) {
				max = currentValue;
			}    		    		
		}

		System.out.println("Insole Threshold: " + max);

		//INSOLE_THRESHOLD = max;
		//INSOLE_THRESHOLD = 1000;

		double tread;
		for (int i = 1; i < numberOfRows; i++) {

			double insole0 = rawSheet.getRow(i).getCell(RAW_HEEL_EXT).getNumericCellValue();
			double insole1 = rawSheet.getRow(i).getCell(RAW_HEEL_INT).getNumericCellValue();
			double insole2 = rawSheet.getRow(i).getCell(RAW_META_INT).getNumericCellValue();
			double insole3 = rawSheet.getRow(i).getCell(RAW_HALLUX).getNumericCellValue();
			double insole4 = rawSheet.getRow(i).getCell(RAW_META_EXT).getNumericCellValue();
			double insole5 = rawSheet.getRow(i).getCell(RAW_ARCH).getNumericCellValue();
			
			int activations = 0;
			if(insole0 > INSOLE_THRESHOLD)activations++;
			if(insole1 > INSOLE_THRESHOLD)activations++;
			if(insole2 > INSOLE_THRESHOLD)activations++;
			if(insole3 > INSOLE_THRESHOLD)activations++;
			if(insole4 > INSOLE_THRESHOLD)activations++;
			if(insole5 > INSOLE_THRESHOLD)activations++;

			if(activations > 1) {
				tread = INSOLE_STANCE;        		
			}else {        		
				tread = INSOLE_SWING;
			}

			sheet.getRow(i).createCell(TREAD).setCellValue(tread);   

		}

	}
	

	
	void errorHandling(){
		
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");

		int deletedRows = 0;
		// Delete initial invalid samples
		for(int i = 1; i < numberOfRows; i++) {
			
			double tread1 = sheet.getRow(i).getCell(TREAD_1).getNumericCellValue();
			double tread2 = sheet.getRow(i).getCell(TREAD_2).getNumericCellValue();
			
			if(tread1 == INSOLE_SWING && tread2 == INSOLE_SWING) {
				
				sheet.removeRow(sheet.getRow(i));
				sheet.shiftRows(i+1, numberOfRows, -1);
				deletedRows++;
				numberOfRows--;
				i++;				
			}			
		}
		
		// Delete initial invalid samples
		for(int i = 1; i < numberOfRows; i++) {
			
			double positionAngle = sheet.getRow(i).getCell(POSITION_ANGLE).getNumericCellValue();
			if(positionAngle > 360) {			
				sheet.getRow(i).getCell(POSITION_ANGLE).setCellValue(-1);				
			}									
		}
		
		System.out.println("Rows deleted for erros in Trial 4: " + deletedRows);		
	}
	
	public void generateOutputFiles() throws Exception {

		System.out.println("Excel generation...");

		csvToXLSX();

		if(numberOfRows == 1){
			createFile(filePath + ".xlsx");
		}

		errorDeletion();
		
		setMinMaxRawValues();
	
		generateCalculatedData();

		errorHandling();

		generateGaitData(true);
		generateCopVelocity(true);
		generateDoubleStanceGaitData(true);
		generateGaitParams(true);
		generateDoubleStanceGaitParams(true);
		
		generateGaitData(false);
		generateCopVelocity(false);
		generateDoubleStanceGaitData(false);
		generateGaitParams(false);
		generateDoubleStanceGaitParams(false);
			
		createFile(outputPath + ".xlsx");        
		System.out.println("Excel Done.");

		generateSheetCSV();
		System.out.println("CSVs Done.");

	}
	
   public static void main(String args[]) throws IOException, InvalidFormatException, ParseException, Exception {
		   
	   
//	   GaitDataGenerationTrial2 gaitData0 = new GaitDataGenerationTrial2("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\0_mbd-20180605-161145.txt.without_errors.txt", "M", "LEFT", 1200);
//	   gaitData0.generateOutputFiles();
	   GaitDataGenerationTrial2 gaitData11 = new GaitDataGenerationTrial2("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\1SG_mbd-20180516-094254_fixed.txt.without_errors.txt", "M", "LEFT", 1200);
	   gaitData11.generateOutputFiles();
//	   GaitDataGenerationTrial2 gaitData12 = new GaitDataGenerationTrial2("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\1SG_mbd-20180516-094316_fixed.txt.without_errors.txt", "M", "LEFT", 1200);
//	   gaitData12.generateOutputFiles();
//	   GaitDataGenerationTrial2 gaitData2 = new GaitDataGenerationTrial2("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\2JLU_mbd-20180516-112135_fixed.txt.without_errors.txt", "L", "LEFT", 1200);
//	   gaitData2.generateOutputFiles();
//	   GaitDataGenerationTrial2 gaitData31 = new GaitDataGenerationTrial2("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\3FM_mbd-20180516-120957_fixed.txt.without_errors.txt", "M", "LEFT", 1200);
//	   gaitData31.generateOutputFiles();
//	   GaitDataGenerationTrial2 gaitData32 = new GaitDataGenerationTrial2("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\3FM_mbd-20180516-121028_fixed.txt.without_errors.txt", "M", "LEFT", 1200);
//	   gaitData32.generateOutputFiles();
//	   GaitDataGenerationTrial2 gaitData4 = new GaitDataGenerationTrial2("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\4EOU_mbd-20180516-130443_fixed.txt.without_errors.txt", "M", "RIGHT", 1200);
//	   gaitData4.generateOutputFiles();
//	   GaitDataGenerationTrial2 gaitData6 = new GaitDataGenerationTrial2("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\6NA_mbd-20180516-165857_fixed.txt.without_errors.txt", "S", "RIGHT", 1200);
//	   gaitData6.generateOutputFiles();
	   		   
   }          

	
	
	
}
