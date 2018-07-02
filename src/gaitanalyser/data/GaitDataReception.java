 package gaitanalyser.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;


/**
 * The Class GaitDataReception receives the file fileName.txt as input, as
	well as the number of data sent by sample. Then, the parsing of this file, taking
	into account the requirements explained previously is performed. The first output
	is the file fileName_raw.txt. Taking into account the possibility of problems of
	synchronization during the data reception, a second processing is applied to the file
	fileName_raw.txt, where the detected corrupted samples are deleted. The output
	file will be called fileName_raw.without_errors.txt. Both are text files with .csv
	format. In these files, each of the columns corresponds to a channel associated with
	a data sensor.
 */
public class GaitDataReception{
	
	/** The wifi mode. */
	private boolean WIFI_MODE = false;

	/** The eof. */
	private boolean EOF = false;

	/** The page size. */
	private static int PAGE_SIZE = 264;

	/** The type int. */
	final private int TYPE_INT	   = 1;
	
	/** The type long. */
	final private int TYPE_LONG    = 2;
	
	/** The type float. */
	final private int TYPE_FLOAT   = 3;
	
	/** The type double. */
	final private int TYPE_DOUBLE  = 4;
	
	/** The open parenthesis. */
	final private int OPEN_PARENTHESIS = 40; 
	
	/** The close parenthesis. */
	final private int CLOSE_PARENTHESIS = 41;
	
	/** The hash error. */
	final private int HASH_ERROR = 35;


	/** The timestamp. */
	final private int TIMESTAMP      	               =  0;
	
	/** The motor position. */
	final private int MOTOR_POSITION                   =  1;
	
	/** The motor torque. */
	final private int MOTOR_TORQUE                     =  2;
	
	/** The insole sensor 1. */
	final private int INSOLE_SENSOR_1                  =  3;
	
	/** The insole sensor 2. */
	final private int INSOLE_SENSOR_2                  =  4;
	
	/** The insole sensor 3. */
	final private int INSOLE_SENSOR_3                  =  5;
	
	/** The insole sensor 4. */
	final private int INSOLE_SENSOR_4                  =  6;
	
	/** The insole sensor 5. */
	final private int INSOLE_SENSOR_5                  =  7;
	
	/** The insole sensor 6. */
	final private int INSOLE_SENSOR_6                  =  8;
	
	/** The insole sensor 7. */
	final private int INSOLE_SENSOR_7                  =  9;
	
	/** The insole sensor 8. */
	final private int INSOLE_SENSOR_8                  = 10;
	
	/** The sensor orientation euler x. */
	final private int SENSOR_ORIENTATION_EULER_X       = 11;
	
	/** The sensor orientation euler y. */
	final private int SENSOR_ORIENTATION_EULER_Y       = 12;
	
	/** The sensor orientation euler z. */
	final private int SENSOR_ORIENTATION_EULER_Z       = 13;
	
	/** The sensor orientation quaternion x. */
	final private int SENSOR_ORIENTATION_QUATERNION_X  = 14;
	
	/** The sensor orientation quaternion y. */
	final private int SENSOR_ORIENTATION_QUATERNION_Y  = 15;
	
	/** The sensor orientation quaternion z. */
	final private int SENSOR_ORIENTATION_QUATERNION_Z  = 16;
	
	/** The sensor orientation quaternion w. */
	final private int SENSOR_ORIENTATION_QUATERNION_W  = 17;
	
	/** The motor current. */
	final private int MOTOR_CURRENT                    = 18;
	
	/** The motor overload. */
	final private int MOTOR_OVERLOAD                   = 19;
	
	/** The motor speed. */
	final private int MOTOR_SPEED                      = 20;
	
	/** The speed. */
	final private int SPEED            		   		   = 21;  
	
	/** The battery charge. */
	final private int BATTERY_CHARGE                   = 22;
	
	/** The temperature. */
	final private int TEMPERATURE                      = 23;
	
	/** The insole 2sensor 1. */
	final private int INSOLE_2SENSOR_1                 = 24;
	
	/** The insole 2sensor 2. */
	final private int INSOLE_2SENSOR_2                 = 25;
	
	/** The insole 2sensor 3. */
	final private int INSOLE_2SENSOR_3                 = 26;
	
	/** The insole 2sensor 4. */
	final private int INSOLE_2SENSOR_4                 = 27;
	
	/** The insole 2sensor 5. */
	final private int INSOLE_2SENSOR_5                 = 28;
	
	/** The insole 2sensor 6. */
	final private int INSOLE_2SENSOR_6                 = 29;
	
	/** The insole 2sensor 7. */
	final private int INSOLE_2SENSOR_7                 = 30;
	
	/** The insole 2sensor 8. */
	final private int INSOLE_2SENSOR_8                 = 31;


	// NEGATIVE_MASK and NUM_ELEMENTS must have same value
	/** The num elements. */
	// to assure that headers don't overlap.
	private int NUM_ELEMENTS = 32;
	
	/** The negative mask. */
	private int NEGATIVE_MASK = 32;
	
	/** The num of channels. */
	private int numOfChannels;

	/** The data collection. */
	private int[]  dataCollection;
	
	/** The file path. */
	private String filePath;
	
	/** The file name. */
	private String fileName;

	/** The buffer. */
	private BufferedReader buffer;
	
	/** The buffer bin. */
	private FileInputStream bufferBin;

    /** The file out. */
    private static PrintStream fileOut;    
    
    /** The file out final. */
    private static PrintStream fileOutFinal;

	
	/**
	 * Instantiates a new gait data reception.
	 *
	 * @param filePath the file path
	 * @param wifiMode the wifi mode
	 * @param numOfChannels the num of channels
	 * @throws Exception the exception
	 */
	public GaitDataReception(String filePath, boolean wifiMode, int numOfChannels) throws Exception {
		
		dataCollection = new int[NUM_ELEMENTS];

		dataCollection[MOTOR_POSITION] = TYPE_INT;
		dataCollection[MOTOR_TORQUE] = TYPE_INT;
		dataCollection[INSOLE_SENSOR_1] = TYPE_INT;
		dataCollection[INSOLE_SENSOR_2] = TYPE_INT;
		dataCollection[INSOLE_SENSOR_3] = TYPE_INT;
		dataCollection[INSOLE_SENSOR_4] = TYPE_INT;
		dataCollection[INSOLE_SENSOR_5] = TYPE_INT;
		dataCollection[INSOLE_SENSOR_6] = TYPE_INT;
		dataCollection[INSOLE_SENSOR_7] = TYPE_INT;
		dataCollection[INSOLE_SENSOR_8] = TYPE_INT;
		dataCollection[SENSOR_ORIENTATION_EULER_X] = TYPE_INT;
		dataCollection[SENSOR_ORIENTATION_EULER_Y] = TYPE_INT;
		dataCollection[SENSOR_ORIENTATION_EULER_Z] = TYPE_INT;
		dataCollection[SENSOR_ORIENTATION_QUATERNION_X] = TYPE_INT;
		dataCollection[SENSOR_ORIENTATION_QUATERNION_Y] = TYPE_INT;
		dataCollection[SENSOR_ORIENTATION_QUATERNION_Z] = TYPE_INT;
		dataCollection[SENSOR_ORIENTATION_QUATERNION_W] = TYPE_INT;
		dataCollection[MOTOR_CURRENT] = TYPE_INT;
		dataCollection[MOTOR_OVERLOAD] = TYPE_INT;
		dataCollection[MOTOR_CURRENT] = TYPE_INT;
		dataCollection[MOTOR_OVERLOAD] = TYPE_INT;
		dataCollection[MOTOR_SPEED] = TYPE_INT;
		dataCollection[SPEED] = TYPE_INT;  
		dataCollection[BATTERY_CHARGE]  = TYPE_INT;
		dataCollection[TEMPERATURE] = TYPE_INT;
		dataCollection[INSOLE_2SENSOR_1] = TYPE_INT;
		dataCollection[INSOLE_2SENSOR_2]  = TYPE_INT;
		dataCollection[INSOLE_2SENSOR_3] = TYPE_INT;
		dataCollection[INSOLE_2SENSOR_4] = TYPE_INT;
		dataCollection[INSOLE_2SENSOR_5] = TYPE_INT;
		dataCollection[INSOLE_2SENSOR_6] = TYPE_INT;
		dataCollection[INSOLE_2SENSOR_7] = TYPE_INT;
		dataCollection[INSOLE_2SENSOR_8] = TYPE_INT;
		
		WIFI_MODE = wifiMode;
		this.numOfChannels = numOfChannels;
						
        this.filePath = filePath;
        this.fileName = filePath.substring(filePath.lastIndexOf("\\")+1,filePath.length());
                
        System.out.println("File name: " + this.fileName);
        System.out.println("File path: " + this.filePath);
        
    	fileOut = new PrintStream(new FileOutputStream(filePath + ".raw.txt"));
    	fileOutFinal = new PrintStream(new FileOutputStream(filePath + ".without_errors.txt"));
    	
		buffer = new BufferedReader(new FileReader(filePath));
    	bufferBin = new FileInputStream(filePath);
		
	}


	/**
	 * Read txt flash.
	 *
	 * @throws Exception the exception
	 */
	public void readTxtFlash() throws Exception {

		System.out.println("readTxtFlash()");
		                       
		int header;
		int type;
		boolean negative;
				
		writeChannels();
		
		while (!EOF) {
			
			header = parseIntValue();
			negative = false;
			if (header == TIMESTAMP){
				fileOut.println();
				int timestamp =  (parseIntValue() <<24) + (parseIntValue() <<16) + (parseIntValue() <<8) + parseIntValue();				
				System.out.println("Timestamp [" + timestamp + "]");
				fileOut.print(timestamp);

			}else{

				fileOut.print(",");
				if(header > NUM_ELEMENTS){
					header = header - NEGATIVE_MASK;
					negative = true;
				}

				if(negative) {
					System.out.println("Detected a negative value");
					fileOut.print("ERROR");
					continue;					
				}
				
				System.out.println("Header: " + header);
				
				if(header < NUM_ELEMENTS && header >= 0){
					type = dataCollection[header];
				}else{
					System.out.println("Detected error in reception");
					fileOut.print("ERROR");
					continue;
				}

				switch(type){

					case TYPE_INT:
					{
						int integer_number = (parseIntValue() <<8) + parseIntValue();
						if(negative) integer_number = -integer_number;
						
						System.out.println("Integer value: " + integer_number);
						fileOut.print(integer_number);
						
						break;
					}
			
					case TYPE_LONG:
					{
						long long_number = (parseIntValue() <<24) + (parseIntValue() <<16) + (parseIntValue() <<8)  + parseIntValue();
						if(negative) long_number = -long_number;
			
						System.out.println(long_number);
						fileOut.print(long_number);
						
						break;
					}
			
					case TYPE_FLOAT:
					{
						int float_integer_part =  (parseIntValue() <<8) + parseIntValue();
						int float_floating_part = (parseIntValue() <<8) + parseIntValue();
						float float_number  = (float) (float_integer_part + float_floating_part*Math.pow(10,-4));
						if(negative) float_number = -float_number;
						
						System.out.println(float_number);
						fileOut.print(float_number);

						break;
					}
			
					case TYPE_DOUBLE:
					{
						int double_integer_part = (parseIntValue() <<24) + (parseIntValue() <<16) + (parseIntValue() <<8) + parseIntValue();
						int double_decimal_part = (parseIntValue() <<24) + (parseIntValue() <<16) + (parseIntValue() <<8) + parseIntValue();
						double double_number  = (double) (double_integer_part + double_decimal_part*Math.pow(10,-4));
						
						System.out.println(double_number);
						fileOut.print(double_number);

						break;
					}
			
					default:
					{					
						parseIntValue();
						System.out.println("ERROR");
						fileOut.print("Detected error in reception");
					}
				}

			}
		}
		
		buffer.close();
		
		cleanInvalidLines();
		
		System.out.println("Done.");

	}
	
	
	/**
	 * Read txt wifi.
	 *
	 * @throws Exception the exception
	 */
	public void readTxtWifi() throws Exception {

		System.out.println("readTxtWifi()");

		writeChannels();

		char[] data = new char[2];		
		int counter = 0; 

		while (!EOF) {

			int open = parseIntValue();

			if(open == OPEN_PARENTHESIS) {
				
				int header = parseIntValue();
								
				System.out.println("Header: " + header);

				int integerValue = 0;
				if (header == TIMESTAMP){
					fileOut.println();
					int timestamp =  (parseIntValue() <<8) + parseIntValue();				
					System.out.println("Timestamp [" + timestamp + "]");
					integerValue = counter*10;
					counter++;					
				}else{

					fileOut.print(",");		

					if(!(header < NUM_ELEMENTS && header >= 0)){		

						System.out.println("Detected error in reception");
						fileOut.print("ERROR");
						while((parseIntValue() != CLOSE_PARENTHESIS) && (!EOF));					
						continue;

					}

					int highValue = parseIntValue();
					int lowValue = parseIntValue();
							
//					if((highValue == HASH_ERROR) || (lowValue == HASH_ERROR)) {
//						
//						System.out.println("Detected error in reception");
//						
//						if(highValue == HASH_ERROR) {
//							highValue = 0;
//							fileOut.print("HIGH_");
//						}if(lowValue == HASH_ERROR) {
//							lowValue = 0;
//							fileOut.print("LOW_");
//						}								
//						integerValue = 0;
//						
//					}else {						
					integerValue = (highValue <<8) + lowValue;
					System.out.println("Integer value: " + integerValue);						
//					}
															
				}
				
				int close = parseIntValue();
				
				if(close == CLOSE_PARENTHESIS) {
					fileOut.print(integerValue);	
				}else {
					System.out.println("Detected error in reception");
					fileOut.print("ERROR");
					while((parseIntValue() != CLOSE_PARENTHESIS) && (!EOF));					
					continue;
				}
			}
		}

		buffer.close();

		cleanInvalidLines();

		System.out.println("Done.");

	}
	
	
	/**
	 * Write channels.
	 */
	private void writeChannels(){
		
		String fileHeader = "Time";
	
		for (int i = 1; i < numOfChannels; i++){			
			fileHeader+= ",Channel " + i;
		}
		
		fileOut.print(fileHeader);	
	}

	/**
	 * Clean invalid lines.
	 *
	 * @throws Exception the exception
	 */
	private void cleanInvalidLines() throws Exception {
		
		System.out.println("cleanInvalidLines()");
		
		int counter = 0;
		int lines = 0;
		
		BufferedReader br = new BufferedReader(new FileReader(filePath + ".raw.txt"));
		
		String currentLine = null;		
		while ((currentLine = br.readLine()) != null) {

			lines ++;

			String str[] = currentLine.split(",");
			if(currentLine.contains("ERROR") || str.length != numOfChannels) {		
				System.out.println("Deleting line " + lines + " [" + currentLine + "]");
				counter++;				 
			}else {
				fileOutFinal.println(currentLine);
			}			             
		}
		
		br.close();
		
		System.out.println("Deleted [" + counter + "] lines of [" +  lines  + "]");
		
	}	
	
	/**
	 * Parses the int value.
	 *
	 * @return the int
	 * @throws Exception the exception
	 */
	private int parseIntValue() throws Exception{
		
		byte[] data = new byte[1];
		int dataValue = 0;
		if ((bufferBin.read(data,0,1)) != -1){
								
			dataValue = data[0] & 0xFF;
								
		}else{
			EOF = true;
		}
		
		return dataValue;
	}
	
	/**
	 * Generate txt file.
	 *
	 * @throws Exception the exception
	 */
	public void generateTxtFile() throws Exception {
					
		System.out.println("Begin conversion from binary file...");
		if(WIFI_MODE)
			
			readTxtWifi();	
		else
			readTxtFlash();
		
		fileOut.close();
    	fileOutFinal.close();
    	
    	System.out.println("Conversion done.");
		
	}
	   
}     
	




