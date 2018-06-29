package gaitanalyser.data;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.*;
import org.json.simple.parser.ParseException;

public class GaitDataGenerationTrial1 extends GaitDataGeneration{
	
	public GaitDataGenerationTrial1(String filePath, String footSize, String makLeg, int threshold) throws IOException{
		
		super(filePath, footSize, makLeg);
		INSOLE_THRESHOLD = threshold;				
		SAMPLE_RATE = 4;
	}
	

	public void channelsInitialization(){
		
		RAW_TIME = 0;		
		RAW_POSITION = 1;		
		RAW_TORQUE = 2;		
		RAW_HEEL_EXT_1 = 3;		
		RAW_HEEL_INT_1 = 4;    		
		RAW_TOES_1 = 5;		
		RAW_META_INTERM_1 = 6;		
		RAW_META_INT_1 = 7;    		
		RAW_HALLUX_1 = 8;		
		RAW_META_EXT_1 = 9;		
		RAW_ARCH_1 = 10;		
		RAW_QUATERNION_X = 11;		
		RAW_QUATERNION_Y = 12;		
		RAW_QUATERNION_Z = 13;		
		RAW_QUATERNION_W = 14;		
		RAW_COLUMNS = 14;
		
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
		CALC_COLUMNS = 21;
		
		calc = new String[CALC_COLUMNS];
		
		calc[TIME] = "TIMESTAMP(s)";
		calc[POSITION_ANGLE] = "POSITION_ANGLE(°)";
		calc[EULER_X] = "EULER_X(°)";
		calc[EULER_Y] = "EULER_Y(°)";
		calc[EULER_Z] = "EULER_Z(°)";
		calc[HALLUX_1] = "HALLUX(millibar)";     
		calc[TOES_1] = "TOES(millibar)"; 
		calc[META_EXT_1] = "META_EXT(millibar)";
		calc[META_INTERM_1] = "META_INTERM(millibar)";
		calc[META_INT_1] = "META_INT(millibar)";
		calc[ARCH_1] = "ARCH(millibar)";
		calc[HEEL_EXT_1] = "HEEL_EXT(millibar)";    
		calc[HEEL_INT_1] = "HEEL_INT(millibar)";
		calc[TREAD_1] = "TREAD(millibar)";
		calc[COP_X_1] = "COP_X(mm)";
		calc[COP_Y_1] = "COP_Y(mm)";
		calc[RELATIVE_COP_X_1] = "RELATIVE_COP_X(%)";
		calc[RELATIVE_COP_Y_1] = "RELATIVE_COP_Y(%)";
		calc[RELATIVE_MEAN_COP_X_1] = "RELATIVE_MEAN_COP_X(%)";
		calc[RELATIVE_MEAN_COP_Y_1] = "RELATIVE_MEAN_COP_Y(%)";
		calc[STEP_IN_TIME_1] = "STEP_IN_TIME";	
		
	}

	/** Deletion of errors. Second round. Comparison between four values */
	public void errorHandling(){

		System.out.println("Delete errors Trial 2");

		XSSFSheet sheet = workBook.getSheet("RAW");

		int deletedRows = 0;
		for (int i = 1; i < (numberOfRows - 4); i++) {

			boolean delete = false;
			for (int j = 1; j <= RAW_COLUMNS; j++) {

				double beforeValue   = sheet.getRow(i).getCell(j).getNumericCellValue();
				double currentValue1 = sheet.getRow(i+1).getCell(j).getNumericCellValue();
				double currentValue2 = sheet.getRow(i+2).getCell(j).getNumericCellValue();
				double afterValue    = sheet.getRow(i+3).getCell(j).getNumericCellValue();

				// Delete jumps from negative to positive
				if(((currentValue1 > 0) && (currentValue1 > 0) && (beforeValue < 0) && (afterValue < 0)) ||
						((currentValue1 < 0) && (currentValue2 < 0) && (beforeValue > 0) && (afterValue > 0))){

					delete = true;

			    // Delete big jumps 
				}else if(((Math.abs(currentValue1) - Math.abs(beforeValue)) > ERROR_CORRECTION) && 
						((Math.abs(currentValue2) - Math.abs(beforeValue)) > ERROR_CORRECTION) &&
						((Math.abs(currentValue1) - Math.abs(afterValue)) > ERROR_CORRECTION) && 
						((Math.abs(currentValue2) - Math.abs(afterValue)) > ERROR_CORRECTION) &&    					
						((Math.abs(afterValue) - Math.abs(beforeValue) < ERROR_CORRECTION))){

					delete = true;

				}
				
				// Deletion of values that doesn't follow a logical progresion
				else if(((currentValue1 == currentValue2) &&
						(currentValue1 > beforeValue) &&
						(currentValue1 > afterValue) &&
						(beforeValue > afterValue)) ||
						((currentValue1 == currentValue2) &&
								(currentValue1 < beforeValue) &&
								(currentValue1 < afterValue) &&
								(beforeValue < afterValue))){

					delete = true;

				}

				if(delete){

					sheet.removeRow(sheet.getRow(i));
					sheet.shiftRows(i+1, numberOfRows, -1);										
					deletedRows++;
					numberOfRows--;
					i++;
					
					sheet.removeRow(sheet.getRow(i));
					sheet.shiftRows(i+1, numberOfRows, -1);
					deletedRows++;
					numberOfRows--;
					i++;
					j = RAW_COLUMNS;
										
					delete = false;

				}

			}    	
		}


		System.out.println("Deleted rows: " + deletedRows); 
		System.out.println("Final number of rows: " + numberOfRows); 

	}

	/* Calculate Euler values from quaterniums */ 
	public void calculateEulerData() {

		System.out.println("Calculate Euler Values");

		XSSFSheet rawSheet = workBook.getSheet("RAW");
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");

		double x = 0;
		double y = 0;
		double z = 0;
		double q0 = 0;
		double q1 = 0;
		double q2 = 0;
		double q3 = 0;

		double temp0 = 0;
		double temp1 = 0;
		double temp2 = 0;
		double temp3 = 0;
		double temp4 = 0;

		XSSFRow header = sheet.getRow(0);
		header.createCell(EULER_X).setCellValue(calc[EULER_X]);
		sheet.autoSizeColumn(EULER_X);
		header.createCell(EULER_Y).setCellValue(calc[EULER_Y]);
		sheet.autoSizeColumn(EULER_Y);
		header.createCell(EULER_Z).setCellValue(calc[EULER_Z]);
		sheet.autoSizeColumn(EULER_Z);

		for (int i = 1; i < numberOfRows; i++) {

			q0 = rawSheet.getRow(i).getCell(RAW_QUATERNION_X).getNumericCellValue() / 10000;
			q1 = rawSheet.getRow(i).getCell(RAW_QUATERNION_Y).getNumericCellValue() / 10000;
			q2 = rawSheet.getRow(i).getCell(RAW_QUATERNION_Z).getNumericCellValue() / 10000;
			q3 = rawSheet.getRow(i).getCell(RAW_QUATERNION_W).getNumericCellValue() / 10000;

			temp0 = 2 * (q0 * q1 + q2 * q3);
			temp1 = 1 - 2 * (q1 * q1 + q2 * q2);
			temp2 = 2 * (q0 * q2 - q3 * q1);
			temp3 = 2 * (q0 * q3 + q1 * q2);
			temp4 = 1 - 2 * (q2 * q2 + q3 * q3);

			x = Math.atan2(temp1, temp0) * 180 / Math.PI;
			y = Math.asin(temp2) * 180 / Math.PI;;
			z = Math.atan2(temp4, temp3) * 180 / Math.PI;;

			sheet.getRow(i).createCell(EULER_X).setCellValue(Math.abs(x));
			sheet.getRow(i).createCell(EULER_Y).setCellValue(y);
			sheet.getRow(i).createCell(EULER_Z).setCellValue(z);
		}
	}

	protected  void calculateExtraData(){

		calculatePositionAngleData();        
		calculateEulerData();
		calculateTreadThreshold(true);
		calculateInsolesData(true);
		calculateCentreOfPressureData(true);
		calculateRelativeCentreOfPressureData(true);

	}
	
	double conversionPositionAngleValue(double value) {
	
		return Math.abs(((value - 2000) / 4096) * 360);
	}
	
	
	double conversionPressureValue(double pressureValue) {
		
		if(pressureValue > INSOLE_THRESHOLD) 
			pressureValue = (pressureValue - INSOLE_THRESHOLD)*6;
		else pressureValue = 0;
		
		return pressureValue;
	}
	
	public void generateOutputFiles() throws Exception {

		System.out.println("Excel generation...");

		csvToXLSX();

		if(numberOfRows == 1){
			createFile(filePath + ".xlsx");
		}

		errorDeletion();
		errorHandling();
		setMinMaxRawValues();
	
		generateCalculatedData();

		generateGaitData(true);
		generateCopVelocity(true);
		generateGaitParams(true);

		createFile(outputPath + ".xlsx");        
		System.out.println("Excel Done.");

		generateSheetCSV();
		System.out.println("CSVs Done.");

	}
	
	public void fixigErrors(ArrayList stepsToDelete){
		
		XSSFSheet sheetGait;
				
		sheetGait = workBook.getSheet("GAIT_DATA_MAK");
		
		
		for(int i = 1 ; i < stepsToDelete.size() ; i++){			
			sheetGait.removeRow(sheetGait.getRow(i));
			sheetGait.shiftRows(i+1, numberOfRows, -1);			
		}
				
	}
	
	public static void main(String args[]) throws IOException, InvalidFormatException, ParseException, Exception {
		  
		   
//		   GaitDataGenerationTrial1 gaitData0 = new GaitDataGenerationTrial1("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\MAK-HAM-P2-Control.txt", "M", "LEFT", 100);
//		   gaitData0.generateOutputFiles();
		   GaitDataGenerationTrial1 gaitData1 = new GaitDataGenerationTrial1("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\MAK-HAM-P2-Paciente1-SG-M4", "M", "LEFT", 50);
		   gaitData1.generateOutputFiles();
//		   GaitDataGenerationTrial1 gaitData2 = new GaitDataGenerationTrial1("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\MAK-HAM-P2-Paciente2-JLU-M4", "L", "LEFT", 50);
//		   gaitData2.generateOutputFiles();
//		   GaitDataGenerationTrial1 gaitData3 = new GaitDataGenerationTrial1("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\MAK-HAM-P2-Paciente3-FM-M5", "M", "LEFT", 50);
//		   gaitData3.generateOutputFiles();
//		   GaitDataGenerationTrial1 gaitData4 = new GaitDataGenerationTrial1("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\MAK-HAM-P2-Paciente4-EOU-M4", "M", "RIGHT", 50);
//		   gaitData4.generateOutputFiles();
		   //GaitDataGenerationTrial1 gaitData5 = new GaitDataGenerationTrial1("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\MAK-HAM-P2-Paciente5-EA-M3", "XL", "LEFT", 50);
		   //gaitData5.generateOutputFiles();
		   //GaitDataGenerationTrial1 gaitData6 = new GaitDataGenerationTrial1("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\MAK-HAM-P2-Paciente6-N-M3", "S", "RIGHT", 50);
		   //gaitData6.generateOutputFiles();
//		   GaitDataGenerationTrial1 gaitData7 = new GaitDataGenerationTrial1("D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\MAK-HAM-P2-Paciente7-JM-M7", "M", "LEFT", 50);		   
//		   gaitData7.generateOutputFiles();

		   
		   
	}	
	

}
