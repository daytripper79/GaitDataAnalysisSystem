package gaitanalyser.data;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.charts.AxisCrosses;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.ss.usermodel.charts.DataSources;
import org.apache.poi.ss.usermodel.charts.LegendPosition;
import org.apache.poi.ss.usermodel.charts.LineChartData;
import org.apache.poi.ss.usermodel.charts.LineChartSeries;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.charts.XSSFChartAxis;
import org.apache.poi.xssf.usermodel.charts.XSSFChartLegend;
import org.apache.poi.xssf.usermodel.charts.XSSFValueAxis;

/**
The class GaitDataGeneration.java is responsible for the conversion of the raw data
in the parameters of the analysis of the gait. This class is specialized
by the GaitDataGenerationTrial1.java and GaitDataGenerationTrial2.java classes.

The class GaitDataGeneration.java uses the Java library Apache POI that enables
the handling and generation of Office files. This class bases its functionality on the
utilization of an Excel file. Thus, the class will generate an excel file fileName.xlsx
as output.

The excel file will contain the following sheets:
<ol>
	<li> RAW table
	<li> CALCULATED_DATA table
	<li> GAIT_DATA_[MAK|FREE] table
	<li> GAIT_DATA_PARAM[MAK|FREE] table
	<li> PRESSURE_DATA[MAK|FREE] table	
</ol>	
	
From these tables, for a later use by the class GaitDataOctaveGeneration.java
in a next execution stage, files fileName.CSV.CALCULATED_DATA[MAK|FREE].csv,
fileName.CSV.GAIT_DATA[MAK|FREE].csv and fileName.CSV.PRESSURE_DATA[MAK|FREE].csv
will be generated.

*/

public abstract class GaitDataGeneration {

	
	/**   Sensors activated in the insole during the stance for each step. */
	private ArrayList<String> footprints = new ArrayList<String>();

	/** The footprints file out. */
	protected PrintStream footprintsFileOut;
	
	/** The footprints file out MAK. */
	protected PrintStream footprintsFileOutMAK;
	
	/** The footprints file out FREE. */
	protected PrintStream footprintsFileOutFREE;

	/** The last leg ground. */
	public boolean lastLegGround = false;
	
	/**   Input file name. */
	public String fileName;
	
	/**   Input file route. */
	public String filePath;
	
	/**  PAth where original file is. */
	public String originalPath;
	
	/**   Output path for generated files. */
	public String outputPath;  


	/** The excel column by letter. */
	public  final String[] columnByLetter = {  "A",  "B",  "C",  "D",  "E",  "F",  "G",  "H",  "I",  "J",  "K",  "L",  "M",  
											   "N",  "O",  "P",  "Q",  "R",  "S",  "T",  "U",  "V",  "W",  "X",  "Y",  "Z",
											   "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", 
											   "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ",};
	
	// RAW VALUES
	// Parameter to assign to a number of channel
	/**   Sample time. */
	public int RAW_TIME = 0;
	
	/**  Sample rate. */
	int SAMPLE_RATE = 4;
	
	/**   Knee angle position. */
	public int RAW_POSITION;
	
	/**   Torque. */
	public int RAW_TORQUE;
	
	/**   Pressure value for the insole sensor placed in exterior heel foot area in MAK leg. */
	public int RAW_HEEL_EXT_1;
	
	/**  Pressure value for the insole sensor placed in internal heel foot area  in MAK leg. */
	public int RAW_HEEL_INT_1;    
	
	/**  Pressure value for the insole sensor placed in toes foot area  in MAK leg. */
	public int RAW_TOES_1;
	
	/**  Pressure value for the insole sensor placed in intermediate metatarsus foot area in MAK leg. */
	public int RAW_META_INTERM_1;
	
	/**  Pressure value for the insole sensor placed in internal metatarsus foot area in MAK leg. */
	public int RAW_META_INT_1;    
	
	/**  Pressure value for the insole sensor placed in hallux foot area in MAK leg. */
	public int RAW_HALLUX_1;
	
	/**  Pressure value for the insole sensor placed in exterior metatarsus foot area. */
	public int RAW_META_EXT_1;
	
	/**  Pressure value for the insole sensor placed in arch foot area in MAK leg. */
	public int RAW_ARCH_1;
	
	/**   Pressure value for the insole sensor placed in exterior heel foot area in healthy leg. */
	public int RAW_HEEL_EXT_2;
	
	/**  Pressure value for the insole sensor placed in internal heel foot area in healthy leg. */
	public int RAW_HEEL_INT_2;    
	
	/**  Pressure value for the insole sensor placed in toes foot area in healthy leg. */
	public int RAW_TOES_2;
	
	/**  Pressure value for the insole sensor placed in intermediate metatarsus foot area in healthy leg. */
	public int RAW_META_INTERM_2;
	
	/**  Pressure value for the insole sensor placed in internal metatarsus foot area in healthy leg. */
	public int RAW_META_INT_2;    
	
	/**  Pressure value for the insole sensor placed in hallux foot area in healthy leg. */
	public int RAW_HALLUX_2;
	
	/**  Pressure value for the insole sensor placed in exterior metatarsus foot area in healthy leg. */
	public int RAW_META_EXT_2;
	
	/**  Pressure value for the insole sensor placed in arch foot area in MAK leg. */
	public int RAW_ARCH_2;
		
	/**   Quaternion X. */
	public int RAW_QUATERNION_X;
	
	/**   Quaternion Y. */
	public int RAW_QUATERNION_Y;
	
	/**   Quaternion Z. */
	public int RAW_QUATERNION_Z;
	
	/**   Quaternion W. */
	public int RAW_QUATERNION_W;
	
	/**   Euler X. */
	public int RAW_EULER_X;
	
	/**   Euler Y. */
	public int RAW_EULER_Y;
	
	/**   Euler X. */
	public int RAW_EULER_Z;
	
	/**   Total Raw Columns. */
	public int RAW_COLUMNS;
	

	// Calculated values from RAW parameters
	/**   Sample time. */
	public int TIME;	
	
	/**   Knee angle position in degrees. */
	public int POSITION_ANGLE;
	
	/** The euler x. */
	public int EULER_X;
	
	/** The euler y. */
	public int EULER_Y;
	
	/** The euler z. */
	public int EULER_Z;
	
	/**  Generic index for both possibles insoles in the sampling. */
	public int RAW_HALLUX; 			
	
	/** The raw toes. */
	public int RAW_TOES; 				
	
	/** The raw meta ext. */
	public int RAW_META_EXT;			
	
	/** The raw meta interm. */
	public int RAW_META_INTERM;			
	
	/** The raw meta int. */
	public int RAW_META_INT;			
	
	/** The raw arch. */
	public int RAW_ARCH;			
	
	/** The raw heel ext. */
	public int RAW_HEEL_EXT;    			
	
	/** The raw heel int. */
	public int RAW_HEEL_INT;
	
	/** The hallux. */
	public int HALLUX; 			
	
	/** The toes. */
	public int TOES; 				
	
	/** The meta ext. */
	public int META_EXT;			
	
	/** The meta interm. */
	public int META_INTERM;			
	
	/** The meta int. */
	public int META_INT;			
	
	/** The arch. */
	public int ARCH;			
	
	/** The heel ext. */
	public int HEEL_EXT;    			
	
	/** The heel int. */
	public int HEEL_INT;
	
	/** The tread. */
	public int TREAD;
	
	/** The cop x. */
	public int COP_X;			
	
	/** The cop y. */
	public int COP_Y;
	
	/** The relative cop x. */
	public int RELATIVE_COP_X;
	
	/** The relative cop y. */
	public int RELATIVE_COP_Y;
	
	/** The relative mean cop x. */
	public int RELATIVE_MEAN_COP_X;
	
	/** The relative mean cop y. */
	public int RELATIVE_MEAN_COP_Y;
	
	/** The step in time. */
	public int STEP_IN_TIME;
	
	/** The double support. */
	public int DOUBLE_SUPPORT;
	
	/**   Pressure value in millibars for the insole sensor placed in hallux foot area in MAK leg. */
	public int HALLUX_1; 
	
	/**   Pressure value in millibars for the insole sensor placed in toes foot area in MAK leg. */
	public int TOES_1; 
	
	/**   Pressure value in millibars for the insole sensor placed in external metatarsus foot area in MAK leg. */	
	public int META_EXT_1;
	
	/**   Pressure value in millibars for the insole sensor placed in intermediate metatarsus foot area in MAK leg. */
	public int META_INTERM_1;
	
	/**   Pressure value in millibars for the insole sensor placed in internal metatarsus foot area in MAK leg. */
	public int META_INT_1;
	
	/**   Pressure value in millibars for the insole sensor placed in arch foot area in MAK leg. */
	public int ARCH_1;
	
	/**   Pressure value in millibars for the insole sensor placed in exterior heel foot area in MAK leg. */
	public int HEEL_EXT_1;    
	
	/**   Pressure value in millibars for the insole sensor placed in internal heel foot area in MAK leg. */
	public int HEEL_INT_1;
	
	/**   Pressure value in millibars for the insole sensor placed in hallux foot area in healthy leg. */
	public int HALLUX_2; 
	
	/**   Pressure value in millibars for the insole sensor placed in toes foot area in healthy leg. */
	public int TOES_2; 
	
	/**   Pressure value in millibars for the insole sensor placed in external metatarsus foot area in healthy leg. */	
	public int META_EXT_2;
	
	/**   Pressure value in millibars for the insole sensor placed in intermediate metatarsus foot area in healthy leg. */
	public int META_INTERM_2;
	
	/**   Pressure value in millibars for the insole sensor placed in internal metatarsus foot area in healthy leg. */
	public int META_INT_2;
	
	/**   Pressure value in millibars for the insole sensor placed in arch foot area in healthy leg. */
	public int ARCH_2;
	
	/**   Pressure value in millibars for the insole sensor placed in exterior heel foot area in healthy leg. */
	public int HEEL_EXT_2;    
	
	/**   Pressure value in millibars for the insole sensor placed in internal heel foot area in healthy leg. */
	public int HEEL_INT_2;
	
	/**   Number of treads of the trial. */
	public int TREAD_1;
	
	/** The tread 2. */
	public int TREAD_2;
	
	/**   Centre of pressure in X. */
	public int COP_X_1;
	
	/** The cop x 2. */
	public int COP_X_2;
	
	/**   Centre of pressure in Y. */
	public int COP_Y_1;
	
	/** The cop y 2. */
	public int COP_Y_2;
	
	/**  Relative centre of pressure in X. */
	public int RELATIVE_COP_X_1;
	
	/** The relative cop x 2. */
	public int RELATIVE_COP_X_2;
	
	/**  Relative centre of pressure in Y. */
	public int RELATIVE_COP_Y_1;
	
	/** The relative cop y 2. */
	public int RELATIVE_COP_Y_2;
	
	/**  Mean relative centre of pressure in X. */
	public int RELATIVE_MEAN_COP_X_1;
	
	/** The relative mean cop x 2. */
	public int RELATIVE_MEAN_COP_X_2;
	
	/**  Mean relative centre of pressure in Y. */
	public int RELATIVE_MEAN_COP_Y_1;
	
	/** The relative mean cop y 2. */
	public int RELATIVE_MEAN_COP_Y_2;
	
	/**  Steps by sample. */
	public int STEP_IN_TIME_1;
	
	/** The step in time 2. */
	public int STEP_IN_TIME_2;
	
	/**  Total of calculated columns. */
	public int CALC_COLUMNS;

	/**  Array that stores the name of the calculated columns. */
	public String[] calc;

	/** The step. */
	// SERIES FOR GAIT and PRESSURE SHEETS
	protected final int STEP = 0;

	/** The gait phase. */
	// GAIT SHEET COLUMNS
	protected final int GAIT_PHASE = 1;
	
	/** The gait initial time. */
	protected final int GAIT_INITIAL_TIME = 2;
	
	/** The gait final time. */
	protected final int GAIT_FINAL_TIME = 3;
	
	/** The gait total phase time. */
	protected final int GAIT_TOTAL_PHASE_TIME = 4;
	
	/** The gait stride time. */
	protected final int GAIT_STRIDE_TIME = 5;
	
	/** The gait percent cycle. */
	protected final int GAIT_PERCENT_CYCLE = 6;
	
	/** The gait cop velocity. */
	protected final int GAIT_COP_VELOCITY = 7;
	
	/** The gait first double support. */
	protected final int GAIT_FIRST_DOUBLE_SUPPORT = 8;
	
	/** The gait first double support cycle. */
	protected final int GAIT_FIRST_DOUBLE_SUPPORT_CYCLE = 9;
	
	/** The gait second double support. */
	protected final int GAIT_SECOND_DOUBLE_SUPPORT = 10;
	
	/** The gait second double support cycle. */
	protected final int GAIT_SECOND_DOUBLE_SUPPORT_CYCLE = 11;
	
	/** The gait values. */
	public final String[] gaitValues= {"STEP", "GAIT_PHASE(s)", "INITIAL_TIME(s)", "FINAL_TIME(s)", "TOTAL_PHASE_TIME(s)", 
									   "GAIT_STRIDE_TIME(s)", "GAIT_PERCENT_CYCLE(%)", "GAIT_COP_VELOCITY (mm/s)",
			                           "GAIT_FIRST_DOUBLE_SUPPORT(s)", "GAIT_FIRST_DOUBLE_SUPPORT_CYCLE(%)", 
			                           "GAIT_SECOND_DOUBLE_SUPPORT(s)", "GAIT_SECOND_DOUBLE_SUPPORT_CYCLE(%)"};
	
	/** The gait param cadence. */
	// GAIT PARAMETERS	
	protected final int GAIT_PARAM_CADENCE =                     0;
	
	/** The gait param cop velocity. */
	protected final int GAIT_PARAM_COP_VELOCITY =                1;
	
	/** The gait param total stride time. */
	protected final int GAIT_PARAM_TOTAL_STRIDE_TIME =           2;
	
	/** The gait param total stride cycle. */
	protected final int GAIT_PARAM_TOTAL_STRIDE_CYCLE =          3;
	
	/** The gait param stance time. */
	protected final int GAIT_PARAM_STANCE_TIME =                 4;
	
	/** The gait param stance cycle. */
	protected final int GAIT_PARAM_STANCE_CYCLE =                5;
	
	/** The gait param swing time. */
	protected final int GAIT_PARAM_SWING_TIME =                  6;
	
	/** The gait param swing cycle. */
	protected final int GAIT_PARAM_SWING_CYCLE =                 7;
	
	/** The gait param first double support time. */
	protected final int GAIT_PARAM_FIRST_DOUBLE_SUPPORT_TIME =   8;
	
	/** The gait param first double support cycle. */
	protected final int GAIT_PARAM_FIRST_DOUBLE_SUPPORT_CYCLE =  9;
	
	/** The gait param single support time. */
	protected final int GAIT_PARAM_SINGLE_SUPPORT_TIME       =   10;
	
	/** The gait param single support cycle. */
	protected final int GAIT_PARAM_SINGLE_SUPPORT_CYCLE      =   11;
	
	/** The gait param second double support time. */
	protected final int GAIT_PARAM_SECOND_DOUBLE_SUPPORT_TIME =  12;
	
	/** The gait param second double support cycle. */
	protected final int GAIT_PARAM_SECOND_DOUBLE_SUPPORT_CYCLE = 13;
	
	/** The gait param total double support time. */
	protected final int GAIT_PARAM_TOTAL_DOUBLE_SUPPORT_TIME =   14;
	
	/** The gait param total double support cycle. */
	protected final int GAIT_PARAM_TOTAL_DOUBLE_SUPPORT_CYCLE =  15;
	
	/** The gait params. */
	public final String[] gaitParams= {"CADENCE (steps/min)", "COP_VELOCITY (mm/s)", 
										"TOTAL_STRIDE(s)", "TOTAL_STRIDE(%)", 
									   "STANCE(s)", "STANCE(%)","SWING(s)", "SWING(%)",
									   "FIRST_DOUBLE_SUPPORT(s)", "FIRST_DOUBLE_SUPPORT(%)", 
									   "SINGLE_SUPPORT(s)", "SINGLE_SUPPORT(%)",
									   "SECOND_DOUBLE_SUPPORT(s)", "SECOND_DOUBLE_SUPPORT(%)",
									   "TOTAL_DOUBLE_SUPPORT(s)","TOTAL_DOUBLE_SUPPORT(%)"};

	/** The pressure max. */
	// PRESSURE SHEET COLUMNS	
	protected final int PRESSURE_MAX = 1;
	
	/** The pressure total. */
	protected final int PRESSURE_TOTAL = 2;    
	
	/** The pressure relative. */
	protected final int PRESSURE_RELATIVE = 3;
	
	/** The pressure data. */
	protected final int PRESSURE_DATA = 3;    
	
	/** The pressure data. */
	public final String[] pressureData= {"", "MAX", "TOTAL", "RELATIVE"};

	/** The pressure hallux. */
	protected final int PRESSURE_HALLUX = 0;     
	
	/** The pressure toes. */
	protected final int PRESSURE_TOES = 1; 
	
	/** The pressure meta ext. */
	protected final int PRESSURE_META_EXT = 2;
	
	/** The pressure meta interm. */
	protected final int PRESSURE_META_INTERM = 3;
	
	/** The pressure meta int. */
	protected final int PRESSURE_META_INT = 4;
	
	/** The pressure arch. */
	protected final int PRESSURE_ARCH = 5;
	
	/** The pressure heel ext. */
	protected final int PRESSURE_HEEL_EXT = 6;    
	
	/** The pressure heel int. */
	protected final int PRESSURE_HEEL_INT = 7;
	
	/** The pressure sensors. */
	protected final int PRESSURE_SENSORS = 8;    
	
	/** The pressure sensor. */
	public final String[] pressureSensor= {"HALLUX", "TOES", "META_EXT", "META_INTERM", "META_INT", "ARCH", "HEEL_INT", "HEEL_EXT"};

	/** The pressure cop x. */
	protected final int PRESSURE_COP_X = PRESSURE_DATA*PRESSURE_SENSORS + 1;
	
	/** The pressure cop y. */
	protected final int PRESSURE_COP_Y = PRESSURE_DATA*PRESSURE_SENSORS + 2;


	/**  Patient's weight. */
	double weight;
	
	/**  Number of insole's sizes. */
	public int INSOLE_SIZES = 4;

	
	/**
	 *  Coordinates class.
	 */
	protected class Coords {

		/** The x. */
		double x;
		
		/** The y. */
		double y;

		/**
		 * Instantiates a new coords.
		 *
		 * @param x the x
		 * @param y the y
		 */
		public Coords(double x, double y) {
			super();
			this.x = x;
			this.y = y;
		}

		/**
		 * Gets the x.
		 *
		 * @return the x
		 */
		public double getX() {
			return x;
		}

		/**
		 * Sets the x.
		 *
		 * @param x the new x
		 */
		public void setX(double x) {
			this.x = x;
		}

		/**
		 * Gets the y.
		 *
		 * @return the y
		 */
		public double getY() {
			return y;
		}

		/**
		 * Sets the y.
		 *
		 * @param y the new y
		 */
		public void setY(double y) {
			this.y = y;
		}
	}

	/**  Coordinates of the sensors in the insole for each one of the available sizes. */
	Coords[][] pressureCoordinates = 
		       {{new Coords(54.37, 188.9),  new Coords(21.18, 178.55), new Coords(10.05, 127.27), new Coords(33.62, 136.2),  new Coords(58.12, 139.31), new Coords(11.33, 79.29),  new Coords(19.69, 16.89), new Coords(45.52, 16.89)},
				{new Coords(62.31, 203.5),  new Coords(26.91, 193.75), new Coords(10.14, 138.43), new Coords(40.28, 149.36), new Coords(69.13, 156.74), new Coords(21.17, 86.93),  new Coords(29.4,  16.89),  new Coords(55.68, 16.89)},
				{new Coords(64.73, 218.78), new Coords(28.37, 209.04), new Coords(10.92, 154.83), new Coords(40.94, 165.25), new Coords(71.71, 172.5),  new Coords(19.03, 94.35),  new Coords(26.07, 16.89), new Coords(54.4,  16.89)},
				{new Coords(64.72, 242.26), new Coords(31.37, 232.91), new Coords(11.2, 178.77),  new Coords(41.1, 188.75),  new Coords(72.52, 195.65), new Coords(18.53, 110.78), new Coords(27.43, 16.89), new Coords(54.47, 16.89)}};

	/**  Max value for the reference system for each one of the insole's size. */ 
	double[] pressureXY = {217.57, 231.85, 247.49, 269.91};

	
	/** The hallux current. */
	// Auxiliary variables for pressure calculations
	protected double halluxCurrent;
	
	/** The hallux max. */
	protected double halluxMax;
	
	/** The hallux total. */
	protected double halluxTotal;
	
	/** The toes current. */
	protected double toesCurrent;
	
	/** The toes max. */
	protected double toesMax;
	
	/** The toes total. */
	protected double toesTotal;
	
	/** The meta ext current. */
	protected double metaExtCurrent;
	
	/** The meta ext max. */
	protected double metaExtMax;
	
	/** The meta ext total. */
	protected double metaExtTotal;
	
	/** The meta interm current. */
	protected double metaIntermCurrent;
	
	/** The meta interm max. */
	protected double metaIntermMax;
	
	/** The meta interm total. */
	protected double metaIntermTotal;
	
	/** The meta int current. */
	protected double metaIntCurrent;
	
	/** The meta int max. */
	protected double metaIntMax;
	
	/** The meta int total. */
	protected double metaIntTotal;
	
	/** The arch current. */
	protected double archCurrent;
	
	/** The arch max. */
	protected double archMax;
	
	/** The arch total. */
	protected double archTotal;
	
	/** The heel int current. */
	protected double heelIntCurrent;
	
	/** The heel int max. */
	protected double heelIntMax;
	
	/** The heel int total. */
	protected double heelIntTotal;
	
	/** The heel ext current. */
	protected double heelExtCurrent;
	
	/** The heel ext max. */
	protected double heelExtMax;
	
	/** The heel ext total. */
	protected double heelExtTotal;

	/**  Threshold value for insole sensor. */
	protected double INSOLE_THRESHOLD = 50;
	
	/**  State for insole sensor activation. */
	protected final int INSOLE_STANCE = 1;
	
	/**  State for insole sensor deactivation. */
	protected final int INSOLE_SWING = 0;

	/**  Threshold for error correction. */
	protected double ERROR_CORRECTION = 200;

	/** The work book. */
	// Excel variables
	protected XSSFWorkbook workBook;
	
	/** The number of rows. */
	protected int numberOfRows;
	
	/** The row max. */
	protected int rowMax;
	
	/** The row min. */
	protected int rowMin;
	
	/** The gait isolated step. */
	protected  int GAIT_ISOLATED_STEP = 0;;
	
	/** The gait isolated step displacement. */
	protected  int GAIT_ISOLATED_STEP_DISPLACEMENT = 0;
	
	/** The one insole. */
	boolean oneInsole = true;
	
	/**  Size of the patient's insole. */
	public int insoleSize;
	
	/** The insole size s. */
	protected final int INSOLE_SIZE_S = 0;
	
	/** The insole size m. */
	protected final int INSOLE_SIZE_M = 1;
	
	/** The insole size l. */
	protected final int INSOLE_SIZE_L = 2;
	
	/** The insole size xl. */
	protected final int INSOLE_SIZE_XL= 3;
	
	/** The mak leg. */
	public int makLeg = 0;
	
	/** The mak leg right. */
	protected final int MAK_LEG_RIGHT = 0;
	
	/** The mak leg left. */
	protected final int MAK_LEG_LEFT = 1;
		
	/**
	 * Instantiates a new gait data generation.
	 *
	 * @param filePath the file path
	 * @param footSize the foot size
	 * @param makLeg the mak leg
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public GaitDataGeneration(String filePath, String footSize, String makLeg) throws IOException{
		
		this.filePath = filePath;
		fileName = filePath.substring(filePath.lastIndexOf("\\")+1,filePath.length());
		int index = fileName.indexOf(".");
		if(index != -1) fileName = fileName.substring(0, index);
		
		System.out.println("File name: " + fileName);
		
		filePath = filePath.substring(0, filePath.lastIndexOf("\\")) + "\\"+ fileName;
		System.out.println("File path: " +  filePath);
		FileUtils.forceMkdir(new File(filePath + "_FILES"));

		outputPath = filePath + "_FILES" + "\\" + fileName;

		footprintsFileOutMAK = new PrintStream(new FileOutputStream(outputPath + ".FOOTPRINTS.MAK.txt"));
		footprintsFileOutFREE = new PrintStream(new FileOutputStream(outputPath + ".FOOTPRINTS.FREE.txt"));
		
		setInsoleSize(footSize);
		setMakLeg(makLeg);
		
		channelsInitialization();
		setCalculatedDataValuesColumns();
	}
		
	/**
	 * Sets the insole size.
	 *
	 * @param footSize the new insole size
	 */
	void setInsoleSize(String footSize){

		if(footSize.equals("S"))
			insoleSize = INSOLE_SIZE_S;
		else if(footSize.equals("M"))
			insoleSize = INSOLE_SIZE_M;
		else if(footSize.equals("L"))
			insoleSize = INSOLE_SIZE_L;
		else if(footSize.equals("XL"))
			insoleSize = INSOLE_SIZE_XL;
	}
	

	/**
	 * Sets the mak leg.
	 *
	 * @param makLeg the new mak leg
	 */
	void setMakLeg(String makLeg) {
	
		if(makLeg.equals("RIGHT"))
			this.makLeg = MAK_LEG_RIGHT;
		else if(makLeg.equals("LEFT"))
			this.makLeg = MAK_LEG_LEFT;
	}


	/**
	 *  Gets the number of rows of the trial.
	 *
	 * @return the rows number
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	int getRowsNumber() throws FileNotFoundException, IOException {

		int counter = 0;
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		while (br.readLine() != null) {
			counter++;
		}

		System.out.println("Number of entries: " + counter);

		br.close();

		return counter;
	}
	
	/**
	 *  Conversion of input data file in .csv format into an excel sheet
	 *
	 * @throws Exception the exception
	 */
	public void csvToXLSX() throws Exception {

		try {

			this.numberOfRows = getRowsNumber();

			workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("RAW");
			String currentLine = null;
			int RowNum = 0;

			BufferedReader br = new BufferedReader(new FileReader(filePath));
			boolean timeColumn = false;
			while (RowNum < numberOfRows) {
				
				currentLine = br.readLine();

				if (RowNum == 0) {
					if(currentLine.startsWith("Time")) {
						timeColumn = true;						
					}else {
						currentLine = "Time," + currentLine;
					}
					
				} else {
					
					if(!timeColumn) {
						currentLine = (RowNum - 1)*SAMPLE_RATE  + "," + currentLine;
					}
				}

				String str[] = currentLine.split(",");
				XSSFRow currentRow = sheet.createRow(RowNum);

				if (RowNum == 0) {
					for (int i = 0; i < str.length; i++) {
						currentRow.createCell(i).setCellValue(str[i]);
					}
				} else {
					for (int i = 0; i < str.length; i++) {
							
							if(str[i].equals("LOW_0") || str[i].equals("HIGH_0") ||  str[i].equals("HIGH_LOW_0")) {
								
								if((RowNum-1) == 0) {
									currentRow.createCell(i).setCellValue(0);
								}else {
									double value = sheet.getRow(RowNum-1).getCell(i).getNumericCellValue();
									currentRow.createCell(i).setCellValue(value);								
								}
								
							}else {
								double value = Double.parseDouble(str[i]);
								currentRow.createCell(i).setCellValue(value);
							}
							
					}
				}

				RowNum++;
			}

			br.close();


		} catch (Exception ex) {
			System.out.println(ex.getMessage() + "Exception in try");
			throw ex;
		}
	}
	
	/** Deletion of errors. First round. Comparison between three values */
	public void errorDeletion(){

		System.out.println("Delete errors");

		XSSFSheet sheet = workBook.getSheet("RAW");

		int deletedRows = 0;
		for (int i = 2; i < numberOfRows - 1; i++) {

			for (int j = 1; j < RAW_COLUMNS; j++) {

				double beforeValue = sheet.getRow(i-1).getCell(j).getNumericCellValue();
				double currentValue = sheet.getRow(i).getCell(j).getNumericCellValue();
				double afterValue = sheet.getRow(i+1).getCell(j).getNumericCellValue();

				// Delete jumps from negative to positive
				if(((currentValue > 0) && (beforeValue < 0) && (afterValue < 0)) ||
						((currentValue < 0) && (beforeValue > 0) && (afterValue > 0))){
					sheet.removeRow(sheet.getRow(i));
					sheet.shiftRows(i+1, numberOfRows, -1);
					deletedRows++;
					numberOfRows--;
					i++;
					j = RAW_COLUMNS;
					
				// Delete big jumps 
				}else if(((Math.abs(currentValue) - Math.abs(beforeValue)) > ERROR_CORRECTION) && 
						((Math.abs(currentValue) - Math.abs(afterValue)) > ERROR_CORRECTION) && 
						((Math.abs(afterValue) - Math.abs(beforeValue) < ERROR_CORRECTION))){

					sheet.removeRow(sheet.getRow(i));
					sheet.shiftRows(i+1, numberOfRows, -1);
					deletedRows++;
					numberOfRows--;
					i++;
					j = RAW_COLUMNS;
				}
			}    	
		}

		System.out.println("Deleted rows: " + deletedRows); 

	}

	

	/**
	 * Sets the min max raw values.
	 */
	public void setMinMaxRawValues() {

		System.out.println("Set ranges");

		XSSFSheet sheet = workBook.getSheet("RAW");

		sheet.createRow(numberOfRows);

		rowMin = numberOfRows;
		sheet.createRow(rowMin);
		sheet.getRow(rowMin).createCell(0).setCellValue("MIN");                       

		String formulaMin = "IF((COUNTIF(Col1:Col2,\">0\"))>0, SMALL(Col1:Col2,COUNTIF(Col1:Col2,0)+1), 0)";
		for(int i = 1; i <= RAW_COLUMNS; i++) {			
			sheet.getRow(rowMin).createCell(i).setCellFormula(formulaMin.replace("Col1", columnByLetter[i] + "1").replace("Col2", columnByLetter[i] + "" + numberOfRows));
		}
		
		
		rowMax = numberOfRows+1;
		sheet.createRow(rowMax);
		sheet.getRow(rowMax).createCell(0).setCellValue("MAX");
		for(int i = 1; i <= RAW_COLUMNS; i++) {			
			sheet.getRow(rowMax).createCell(i).setCellFormula("MAX(" + columnByLetter[i] + "1:" + columnByLetter[i] + numberOfRows + ")");
		}			
	}


	/**
	 *  Gets the minimum value of a column of datas.
	 *
	 * @param column the column
	 * @return the min value
	 */
	public double getMinValue(int column) {
		FormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();
		return evaluator.evaluate(workBook.getSheet("RAW").getRow(rowMin).getCell(column)).getNumberValue();
	}

	/**
	 *  Gets the maximum value of a column of datas.
	 *
	 * @param column the column
	 * @return the max value
	 */
	public double getMaxValue(int column) {
		FormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();
		return evaluator.evaluate(workBook.getSheet("RAW").getRow(rowMax).getCell(column)).getNumberValue();
	}
	
	/**
	 *  Main function for the generation of calculated general values from RAW Datas.
	 */
	void generateCalculatedData() {

		System.out.println("Generate Calculated Values");

		XSSFSheet sheet = workBook.createSheet("CALCULATED_DATA");

		sheet.createRow(0);        

		setCalculatedDataValuesColumns();
		
		calculateTimeData();

		calculateExtraData();
		

	}
	
	/**
	 * Sets the insoles.
	 *
	 * @param mak the new insoles
	 */
	// TODO: Use this to specify with insole we are calculating
	protected void setInsoles(boolean mak) {
		
		if(mak) {
			
			RAW_HALLUX = RAW_HALLUX_1; 			
			RAW_TOES = RAW_TOES_1; 				
			RAW_META_EXT = RAW_META_EXT_1;			
			RAW_META_INTERM = RAW_META_INTERM_1;			
			RAW_META_INT = RAW_META_INT_1;			
			RAW_ARCH = RAW_ARCH_1;			
			RAW_HEEL_EXT = RAW_HEEL_EXT_1;    			
			RAW_HEEL_INT = RAW_HEEL_INT_1;
			
			HALLUX = HALLUX_1; 			
			TOES = TOES_1; 				
			META_EXT = META_EXT_1;			
			META_INTERM = META_INTERM_1;			
			META_INT = META_INT_1;			
			ARCH = ARCH_1;			
			HEEL_EXT = HEEL_EXT_1;    			
			HEEL_INT = HEEL_INT_1;
			TREAD = TREAD_1;					
			COP_X = COP_X_1;			
			COP_Y = COP_Y_1;
			RELATIVE_COP_X = RELATIVE_COP_X_1;
			RELATIVE_COP_Y = RELATIVE_COP_Y_1;
			RELATIVE_MEAN_COP_X = RELATIVE_MEAN_COP_X_1;
			RELATIVE_MEAN_COP_Y = RELATIVE_MEAN_COP_Y_1;
			STEP_IN_TIME = STEP_IN_TIME_1;
			
			
		}else {
			
			RAW_HALLUX = RAW_HALLUX_2; 			
			RAW_TOES = RAW_TOES_2; 				
			RAW_META_EXT = RAW_META_EXT_2;			
			RAW_META_INTERM = RAW_META_INTERM_2;			
			RAW_META_INT = RAW_META_INT_2;			
			RAW_ARCH = RAW_ARCH_2;			
			RAW_HEEL_EXT = RAW_HEEL_EXT_2;    			
			RAW_HEEL_INT = RAW_HEEL_INT_2;
						
			HALLUX = HALLUX_2; 			
			TOES = TOES_2; 				
			META_EXT = META_EXT_2;			
			META_INTERM = META_INTERM_2;			
			META_INT = META_INT_2;			
			ARCH = ARCH_2;			
			HEEL_EXT = HEEL_EXT_2;    			
			HEEL_INT = HEEL_INT_2;
			TREAD = TREAD_2;					
			COP_X = COP_X_2;			
			COP_Y = COP_Y_2;
			RELATIVE_COP_X = RELATIVE_COP_X_2;
			RELATIVE_COP_Y = RELATIVE_COP_Y_2;
			RELATIVE_MEAN_COP_X = RELATIVE_MEAN_COP_X_2;
			RELATIVE_MEAN_COP_Y = RELATIVE_MEAN_COP_Y_2;
			STEP_IN_TIME = STEP_IN_TIME_2;
		}
				
	}
	
	

	/**
	 *  Gets time from raw data and makes a conversion in seconds.
	 */
	void calculateTimeData() {

		System.out.println("Calculate Time");

		XSSFSheet rawSheet = workBook.getSheet("RAW");
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");

		sheet.getRow(0).createCell(TIME).setCellValue(calc[TIME]);
		sheet.autoSizeColumn(TIME);

		for (int i = 1; i < numberOfRows; i++) {

			double time = rawSheet.getRow(i).getCell(RAW_TIME).getNumericCellValue()/1000;
			sheet.createRow(i).createCell(TIME).setCellValue(time);

		}
	}

	/**
	 *  Gets position value from raw data and makes a conversion in degrees.
	 */
	void calculatePositionAngleData() {

		System.out.println("Calculate Position Angle");

		XSSFSheet rawSheet = workBook.getSheet("RAW");
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");

		double angle;
		XSSFRow header = sheet.getRow(0);
		header.createCell(POSITION_ANGLE).setCellValue(calc[POSITION_ANGLE]);
		sheet.autoSizeColumn(POSITION_ANGLE);
		for (int i = 1; i < numberOfRows; i++) {

			angle = conversionPositionAngleValue(rawSheet.getRow(i).getCell(RAW_POSITION).getNumericCellValue()); 
			sheet.getRow(i).createCell(POSITION_ANGLE).setCellValue(angle);

		}
	}
	
	/**
	 * Conversion position angle value.
	 *
	 * @param value the value
	 * @return the double
	 */
	abstract double conversionPositionAngleValue(double value);
	

	/**
	 *  Gets pressure insole values from raw data and makes a conversion in millibars.
	 *
	 * @param mak the mak
	 */
	void calculateInsolesData(boolean mak){
		
		System.out.println("Calculate Insole sensor values");

		setInsoles(mak);		

		XSSFSheet rawSheet = workBook.getSheet("RAW");
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");

		sheet.getRow(0).createCell(HALLUX).setCellValue(calc[HALLUX]);
		sheet.autoSizeColumn(HALLUX);
		sheet.getRow(0).createCell(TOES).setCellValue(calc[TOES]);
		sheet.autoSizeColumn(TOES);
		sheet.getRow(0).createCell(META_EXT).setCellValue(calc[META_EXT]);
		sheet.autoSizeColumn(META_EXT);
		sheet.getRow(0).createCell(META_INTERM).setCellValue(calc[META_INTERM]);
		sheet.autoSizeColumn(META_INTERM);
		sheet.getRow(0).createCell(META_INT).setCellValue(calc[META_INT]);
		sheet.autoSizeColumn(META_INT);
		sheet.getRow(0).createCell(ARCH).setCellValue(calc[ARCH]);
		sheet.autoSizeColumn(ARCH);
		sheet.getRow(0).createCell(HEEL_INT).setCellValue(calc[HEEL_INT]);
		sheet.autoSizeColumn(HEEL_INT);        
		sheet.getRow(0).createCell(HEEL_EXT).setCellValue(calc[HEEL_EXT]);
		sheet.autoSizeColumn(HEEL_EXT);
		
		setEmptyInsole();

		for(int i = 1; i < numberOfRows ; i++){

			for (int j = RAW_HEEL_EXT; j <= RAW_ARCH; j++){

				double pressureValue = rawSheet.getRow(i).getCell(j).getNumericCellValue();

				pressureValue = conversionPressureValue(pressureValue);
				
				if(j == RAW_HEEL_EXT)
					sheet.getRow(i).createCell(HEEL_EXT).setCellValue(pressureValue);
				if(j == RAW_HEEL_INT)
					sheet.getRow(i).createCell(HEEL_INT).setCellValue(pressureValue);
				if(j == RAW_TOES)
					sheet.getRow(i).createCell(TOES).setCellValue(pressureValue);
				if(j == RAW_META_INTERM)
					sheet.getRow(i).createCell(META_INTERM).setCellValue(pressureValue);
				if(j == RAW_META_INT)    
					sheet.getRow(i).createCell(META_INT).setCellValue(pressureValue);
				if(j == RAW_HALLUX)
					sheet.getRow(i).createCell(HALLUX).setCellValue(pressureValue);
				if(j == RAW_META_EXT)
					sheet.getRow(i).createCell(META_EXT).setCellValue(pressureValue);
				if(j == RAW_ARCH)
					sheet.getRow(i).createCell(ARCH).setCellValue(pressureValue);    				    			    			    		    			
			}    	    	
		}    	
	}
	
	/**
	 * Conversion pressure value.
	 *
	 * @param value the value
	 * @return the double
	 */
	abstract double conversionPressureValue(double value);

	
	/**
	 * Sets the empty insole.
	 */
	void setEmptyInsole() {
		
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");
		
		if(RAW_HEEL_EXT == 0)
			for(int i = 1; i < numberOfRows; i++) {
				sheet.getRow(i).createCell(HEEL_EXT).setCellValue(0);    				    			    			    		    			
			}
				
			
		if(RAW_HEEL_INT == 0)
			for(int i = 1; i < numberOfRows; i++) {
				sheet.getRow(i).createCell(HEEL_INT).setCellValue(0);    				    			    			    		    			
			}
		
		if(RAW_TOES == 0)
			for(int i = 1; i < numberOfRows; i++) {
				sheet.getRow(i).createCell(TOES).setCellValue(0);    				    			    			    		    			
			}
		
		if(RAW_META_INTERM == 0)
			for(int i = 1; i < numberOfRows; i++) {
				sheet.getRow(i).createCell(META_INTERM).setCellValue(0);    				    			    			    		    			
			}
		
		if(RAW_META_INT == 0)    
			for(int i = 1; i < numberOfRows; i++) {
				sheet.getRow(i).createCell(META_INT).setCellValue(0);    				    			    			    		    			
			}
		
		if(RAW_HALLUX == 0)
			for(int i = 1; i < numberOfRows; i++) {
				sheet.getRow(i).createCell(HALLUX).setCellValue(0);    				    			    			    		    			
			}
		
		if(RAW_META_EXT == 0)
			for(int i = 1; i < numberOfRows; i++) {
				sheet.getRow(i).createCell(META_EXT).setCellValue(0);    				    			    			    		    			
			}
		
		if(RAW_ARCH == 0)
			for(int i = 1; i < numberOfRows; i++) {
				sheet.getRow(i).createCell(ARCH).setCellValue(0);    				    			    			    		    			
			}    
	
	}
	
	
	
	
	/**
	 *  
	 * Gets pressure of the insole sensors from the raw data and 
	 * defines the threshold that define if the the foot is in 
	 * a stance phase in the ground of if is in the air.
	 *
	 * @param mak the mak
	 */
	void calculateTreadThreshold(boolean mak) {

		System.out.println("Calculate Tread");

		setInsoles(mak);

		XSSFSheet rawSheet = workBook.getSheet("RAW");
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");
		
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
		//INSOLE_THRESHOLD = 50;

		double tread;
		for (int i = 1; i < numberOfRows; i++) {

			double insole0 = rawSheet.getRow(i).getCell(RAW_HEEL_EXT).getNumericCellValue();
			double insole1 = rawSheet.getRow(i).getCell(RAW_HEEL_INT).getNumericCellValue();
			double insole2 = rawSheet.getRow(i).getCell(RAW_TOES).getNumericCellValue();
			double insole3 = rawSheet.getRow(i).getCell(RAW_META_INTERM).getNumericCellValue();
			double insole4 = rawSheet.getRow(i).getCell(RAW_META_INT).getNumericCellValue();
			double insole5 = rawSheet.getRow(i).getCell(RAW_HALLUX).getNumericCellValue();
			double insole6 = rawSheet.getRow(i).getCell(RAW_META_EXT).getNumericCellValue();
			double insole7 = rawSheet.getRow(i).getCell(RAW_ARCH).getNumericCellValue();

			int activations = 0;
			if(insole0 > INSOLE_THRESHOLD)activations++;
			if(insole1 > INSOLE_THRESHOLD)activations++;
			if(insole2 > INSOLE_THRESHOLD)activations++;
			if(insole3 > INSOLE_THRESHOLD)activations++;
			if(insole4 > INSOLE_THRESHOLD)activations++;
			if(insole5 > INSOLE_THRESHOLD)activations++;
			if(insole6 > INSOLE_THRESHOLD)activations++;
			if(insole7 > INSOLE_THRESHOLD)activations++;

			if(activations >=1) {
				tread = INSOLE_STANCE;        		
			}else {        		
				tread = INSOLE_SWING;
			}

			sheet.getRow(i).createCell(TREAD).setCellValue(tread);   

		}

	}

	/**
	 *  Gets the activation subvalues from the sensors and its respective pressure values (in millibars) 
	 *  and calculates the centre of pressure on time.
	 *
	 * @param mak the mak
	 */
	void calculateCentreOfPressureData(boolean mak){

		System.out.println("Calculate Centre of Pressure");

		setInsoles(mak);
		
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");
		
		XSSFRow header = sheet.getRow(0);
		header.createCell(COP_X).setCellValue(calc[COP_X]);
		sheet.autoSizeColumn(COP_X);
		header.createCell(COP_Y).setCellValue(calc[COP_Y]);
		sheet.autoSizeColumn(COP_Y);

		for(int i = 1; i < numberOfRows ; i++){

			double copx = 0;
			double copy = 0;
			if(sheet.getRow(i).getCell(TREAD).getNumericCellValue() == INSOLE_STANCE) {

				double totalPressures = 0;
				for (int j = HALLUX; j <= HEEL_INT; j++){
					double x = (pressureCoordinates[insoleSize][j-HALLUX]).getX();
					double y = (pressureCoordinates[insoleSize][j-HALLUX]).getY();
					double pressureCoord = sheet.getRow(i).getCell(j).getNumericCellValue();
					totalPressures+=pressureCoord;
					copx+= x*pressureCoord;
					copy+= y*pressureCoord;    			
				}    	

				if(totalPressures != 0){    			
					copx = copx/totalPressures;
					copy = copy/totalPressures;    			
				}

			}

			sheet.getRow(i).createCell(COP_X).setCellValue(copx);
			sheet.getRow(i).createCell(COP_Y).setCellValue(copy);
		}    	
	}

	/**
	 *  Gets the calculated centre of pressure and scales it in percentage 
	 *  and makes mean calculations to smooth the progression of the CoP.
	 *
	 * @param mak the mak
	 */
	void calculateRelativeCentreOfPressureData(boolean mak){

		System.out.println("Calculate Relative Centre of Pressure");

		setInsoles(mak);

		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");
		
		XSSFRow header = sheet.getRow(0);

		header.createCell(RELATIVE_COP_X).setCellValue(calc[RELATIVE_COP_X]);
		sheet.autoSizeColumn(RELATIVE_COP_X);
		header.createCell(RELATIVE_COP_Y).setCellValue(calc[RELATIVE_COP_Y]);
		sheet.autoSizeColumn(RELATIVE_COP_Y);

		for(int i = 1; i < numberOfRows ; i++){

			double copx = sheet.getRow(i).getCell(COP_X).getNumericCellValue();
			double copy = sheet.getRow(i).getCell(COP_Y).getNumericCellValue();

			double x = pressureXY[insoleSize];
			double relativeCopx = (100*copx)/x;
			double y = pressureXY[insoleSize];
			double relativeCopy = (100*copy)/y;

			sheet.getRow(i).createCell(RELATIVE_COP_X).setCellValue(relativeCopx);
			sheet.getRow(i).createCell(RELATIVE_COP_Y).setCellValue(relativeCopy);

		}


		header.createCell(RELATIVE_MEAN_COP_X).setCellValue(calc[RELATIVE_MEAN_COP_X]);
		sheet.autoSizeColumn(RELATIVE_MEAN_COP_X);
		header.createCell(RELATIVE_MEAN_COP_Y).setCellValue(calc[RELATIVE_MEAN_COP_Y]);
		sheet.autoSizeColumn(RELATIVE_MEAN_COP_Y);

		int meanRange = 10;
		for(int i = 1; i < numberOfRows - meanRange; i+=meanRange){

			double meanValueCopX = 0;
			int counterX = 0;
			double meanValueCopY = 0;
			int counterY = 0;
			for(int j = 0; j < meanRange ; j++) {
				double copX = sheet.getRow(i+j).getCell(RELATIVE_COP_X).getNumericCellValue();
				double copY = sheet.getRow(i+j).getCell(RELATIVE_COP_Y).getNumericCellValue();
				if(copX != 0) {
					meanValueCopX += copX;
					counterX++;
				}
				if(copY != 0) {
					meanValueCopY += copY;
					counterY++;
				}
			}

			if(counterX!=0) meanValueCopX/=counterX;
			if(counterY!=0)  meanValueCopY/=counterY;

			for(int j = 0; j < meanRange ; j++) {
				double copX = sheet.getRow(i+j).getCell(RELATIVE_COP_X).getNumericCellValue();
				double copY = sheet.getRow(i+j).getCell(RELATIVE_COP_Y).getNumericCellValue();
				if(copX != 0) copX = meanValueCopX;
				if(copY != 0) copY = meanValueCopY;
				sheet.getRow(i+j).createCell(RELATIVE_MEAN_COP_X).setCellValue(copX);
				sheet.getRow(i+j).createCell(RELATIVE_MEAN_COP_Y).setCellValue(copY);
			}    		
		}      	
	}

	
	/**
	 * Calculate double support data.
	 */
	public void calculateDoubleSupportData() {
		
		System.out.println("Create Double Support Column");
		
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");
		
		XSSFRow header = sheet.getRow(0);

		header.createCell(DOUBLE_SUPPORT).setCellValue(calc[DOUBLE_SUPPORT]);
		sheet.autoSizeColumn(DOUBLE_SUPPORT);
				
		for(int i = 1; i < numberOfRows; i++) {
		
			double treadMak = sheet.getRow(i).getCell(TREAD_1).getNumericCellValue();
			double treadFree = sheet.getRow(i).getCell(TREAD_2).getNumericCellValue();

			if((treadMak == INSOLE_STANCE) && (treadFree == INSOLE_STANCE)) {
				sheet.getRow(i).createCell(DOUBLE_SUPPORT).setCellValue(1);
			}else {
				sheet.getRow(i).createCell(DOUBLE_SUPPORT).setCellValue(0);
			}

		}
							
	}
	
	

	/**
	 *   Calculates the sensors that are activated in the insole during the stance for each step.
	 *
	 * @param footprintArray the footprint array
	 */
	private void calculateFootprint(ArrayList <double[]> footprintArray){

		ArrayList <double[]> finalFootprintArray = new ArrayList<double[]>();

		finalFootprintArray.add(footprintArray.get(0));
		for (int i = 1; i < footprintArray.size(); i++){   
			double[] currentFootprintTimestamp = footprintArray.get(i);
			double[] lastFootprint = Arrays.copyOfRange(footprintArray.get(i-1), HALLUX, HEEL_INT);
			double[] currentFootPrint = Arrays.copyOfRange(footprintArray.get(i), HALLUX, HEEL_INT);    	
			if(!Arrays.equals(lastFootprint, currentFootPrint))
				finalFootprintArray.add(currentFootprintTimestamp);    		
		}

		for (int i = 0; i < finalFootprintArray.size(); i++){
			double[] footprint =  finalFootprintArray.get(i);
			String insoleSensor = footprint[0] + " ";
			for(int j = HALLUX; j <= HEEL_INT ; j++) {
				if(footprint[j] == 1){
					insoleSensor = insoleSensor + calc[j] + " ";    				    				
				}
			}
			
			footprints.add(insoleSensor);
			footprintsFileOut.println(insoleSensor);
		}    	
	}


	/**
	 *   Calculates all the relative values related with the pressure of the sensors.
	 *
	 * @param sheetPressure the sheet pressure
	 * @param pressureDataRow the pressure data row
	 * @param numberOfStep the number of step
	 * @param footprintArray the footprint array
	 */
	private void generatePressureData(XSSFSheet sheetPressure, int pressureDataRow, int numberOfStep, ArrayList<double[]> footprintArray) {

		double totalPressures = halluxTotal + toesTotal + metaExtTotal + metaIntermTotal + metaIntTotal + archTotal + heelIntTotal + heelExtTotal;
		sheetPressure.getRow(pressureDataRow).createCell(STEP).setCellValue(numberOfStep);

		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_HALLUX) + PRESSURE_MAX).setCellValue(halluxMax);
		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_HALLUX) + PRESSURE_TOTAL).setCellValue(halluxTotal); 
		if(totalPressures != 0)
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_HALLUX) + PRESSURE_RELATIVE).setCellValue((100*halluxTotal)/totalPressures);
		else
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_HALLUX) + PRESSURE_RELATIVE).setCellValue(0);
		
		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_TOES) + PRESSURE_MAX).setCellValue(toesMax);
		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_TOES) + PRESSURE_TOTAL).setCellValue(toesTotal);
		if(totalPressures != 0)
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_TOES) + PRESSURE_RELATIVE).setCellValue((100*toesTotal)/totalPressures);
		else
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_TOES) + PRESSURE_RELATIVE).setCellValue(0);
		
		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_META_EXT) + PRESSURE_MAX).setCellValue(metaExtMax);
		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_META_EXT) + PRESSURE_TOTAL).setCellValue(metaExtTotal);    			
		if(totalPressures != 0)
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_META_EXT) + PRESSURE_RELATIVE).setCellValue((100*metaExtTotal)/totalPressures);
		else
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_META_EXT) + PRESSURE_RELATIVE).setCellValue(0);
		

		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_META_INTERM) + PRESSURE_MAX).setCellValue(metaIntermMax);
		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_META_INTERM) + PRESSURE_TOTAL).setCellValue(metaIntermTotal);		
		if(totalPressures != 0)
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_META_INTERM) + PRESSURE_RELATIVE).setCellValue((100*metaIntermTotal)/totalPressures);
		else
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_META_INTERM) + PRESSURE_RELATIVE).setCellValue(0);

		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_META_INT) + PRESSURE_MAX).setCellValue(metaIntMax);
		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_META_INT) + PRESSURE_TOTAL).setCellValue(metaIntTotal);		
		if(totalPressures != 0)
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_META_INT) + PRESSURE_RELATIVE).setCellValue((100*metaIntTotal)/totalPressures);
		else
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_META_INT) + PRESSURE_RELATIVE).setCellValue(0);
		

		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_ARCH) + PRESSURE_MAX).setCellValue(archMax);
		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_ARCH) + PRESSURE_TOTAL).setCellValue(archTotal);		
		if(totalPressures != 0)
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_ARCH) + PRESSURE_RELATIVE).setCellValue((100*archTotal)/totalPressures);
		else
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_ARCH) + PRESSURE_RELATIVE).setCellValue(0);
		

		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_HEEL_INT) + PRESSURE_MAX).setCellValue(heelIntMax);
		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_HEEL_INT) + PRESSURE_TOTAL).setCellValue(heelIntTotal);		
		if(totalPressures != 0)
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_HEEL_INT) + PRESSURE_RELATIVE).setCellValue((100*heelIntTotal)/totalPressures);
		else
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA*PRESSURE_HEEL_INT) + PRESSURE_RELATIVE).setCellValue(0);
		
		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA* PRESSURE_HEEL_EXT) + PRESSURE_MAX).setCellValue(heelExtMax);
		sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA* PRESSURE_HEEL_EXT) + PRESSURE_TOTAL).setCellValue(heelExtTotal);		
		if(totalPressures != 0)
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA* PRESSURE_HEEL_EXT) + PRESSURE_RELATIVE).setCellValue((100*heelExtTotal)/totalPressures);
		else
			sheetPressure.getRow(pressureDataRow).createCell((PRESSURE_DATA* PRESSURE_HEEL_EXT) + PRESSURE_RELATIVE).setCellValue(0);
		

		halluxCurrent = 0;
		halluxMax = 0;
		halluxTotal = 0;
		toesCurrent = 0;
		toesMax = 0;
		toesTotal = 0;
		metaExtCurrent = 0;
		metaExtMax = 0;
		metaExtTotal = 0;
		metaIntermCurrent = 0;
		metaIntermMax = 0;
		metaIntermTotal = 0;
		metaIntCurrent = 0;
		metaIntMax = 0;
		metaIntTotal = 0;
		archCurrent = 0;
		archMax = 0;
		archTotal = 0;
		heelIntCurrent = 0;
		heelIntMax = 0;
		heelIntTotal = 0;
		heelExtCurrent = 0;
		heelExtMax = 0;
		heelExtTotal = 0;

	}

	/**
	 *  Generation of datas related with the march.
	 *
	 * @param mak the mak
	 */
	void generateGaitData(boolean mak){

		System.out.println("Generate Gait Values");
				
		int numberOfSteps = 0;
		setInsoles(mak);
		
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");
		
		XSSFSheet sheetGait;
		XSSFSheet sheetPressure;
		if(mak) {
		
			sheetGait = workBook.createSheet("GAIT_DATA_MAK");		
			sheetPressure = workBook.createSheet("PRESSURE_DATA_MAK");
			footprintsFileOut =  footprintsFileOutMAK;
			
		}else {
			
			sheetGait = workBook.createSheet("GAIT_DATA_FREE");		
			sheetPressure = workBook.createSheet("PRESSURE_DATA_FREE");
			footprintsFileOut =  footprintsFileOutFREE;
		}
		
		boolean ground = true;
		double timestamp = 0;
		double timestampFinal = 0;
		double initialTimeInGround = 0;
		double initialTimeInAir = 0;
		double timeInGround = 0;
		double timeInAir = 0;     

		sheet.getRow(0).createCell(STEP_IN_TIME).setCellValue(calc[STEP_IN_TIME]);
		sheet.autoSizeColumn(STEP_IN_TIME);

		int gaitDatasRow = 0;        
		sheetGait.createRow(gaitDatasRow);                    
		sheetGait.getRow(gaitDatasRow).createCell(STEP).setCellValue(gaitValues[STEP]);
		sheetGait.autoSizeColumn(STEP);
		sheetGait.getRow(gaitDatasRow).createCell(GAIT_PHASE).setCellValue(gaitValues[GAIT_PHASE]);
		sheetGait.autoSizeColumn(GAIT_PHASE);
		sheetGait.getRow(gaitDatasRow).createCell(GAIT_INITIAL_TIME).setCellValue(gaitValues[GAIT_INITIAL_TIME]);
		sheetGait.autoSizeColumn(GAIT_INITIAL_TIME);
		sheetGait.getRow(gaitDatasRow).createCell(GAIT_FINAL_TIME).setCellValue(gaitValues[GAIT_FINAL_TIME]);
		sheetGait.autoSizeColumn(GAIT_FINAL_TIME);
		sheetGait.getRow(gaitDatasRow).createCell(GAIT_TOTAL_PHASE_TIME).setCellValue(gaitValues[GAIT_TOTAL_PHASE_TIME]);
		sheetGait.autoSizeColumn(GAIT_TOTAL_PHASE_TIME);
		sheetGait.getRow(gaitDatasRow).createCell(GAIT_STRIDE_TIME).setCellValue(gaitValues[GAIT_STRIDE_TIME]);
		sheetGait.autoSizeColumn(GAIT_STRIDE_TIME);
		sheetGait.getRow(gaitDatasRow).createCell(GAIT_PERCENT_CYCLE).setCellValue(gaitValues[GAIT_PERCENT_CYCLE]);
		sheetGait.autoSizeColumn(GAIT_PERCENT_CYCLE);

		int pressureDatasRow = 0;
		sheetPressure.createRow(pressureDatasRow);
		sheetPressure.getRow(pressureDatasRow).createCell(STEP).setCellValue("STEP");
		int j = 0;
		for (int i = 0; i < PRESSURE_SENSORS*PRESSURE_DATA; i++) {

			sheetPressure.getRow(pressureDatasRow).createCell(PRESSURE_MAX + i).setCellValue(pressureData[PRESSURE_MAX] + "_" + pressureSensor[j] + "(mbar)");
			sheetPressure.autoSizeColumn(PRESSURE_MAX + i);
			sheetPressure.getRow(pressureDatasRow).createCell(PRESSURE_TOTAL + i).setCellValue(pressureData[PRESSURE_TOTAL] + "_" + pressureSensor[j] + "(mbar)");
			sheetPressure.autoSizeColumn(PRESSURE_TOTAL + i);
			sheetPressure.getRow(pressureDatasRow).createCell(PRESSURE_RELATIVE + i).setCellValue(pressureData[PRESSURE_RELATIVE] + "_" + pressureSensor[j]  + "(%)");
			sheetPressure.autoSizeColumn(PRESSURE_RELATIVE + i);
		
			i += PRESSURE_DATA-1;
			j++;
		}    

		if(sheet.getRow(1).getCell(TREAD).getNumericCellValue() == INSOLE_STANCE){
			ground = true;
		}else{
			ground = false;
		}
			
		numberOfSteps++;
						
		ArrayList <double[]> footprintArray =  new ArrayList<double[]>();               

		for (int i = 1; i < numberOfRows; i++) {
			
			timestamp =  sheet.getRow(i).getCell(TIME).getNumericCellValue();

			// Transition from air to ground meanwhile the gait
			if(ground && sheet.getRow(i).getCell(TREAD).getNumericCellValue() == INSOLE_SWING){

				gaitDatasRow++;
				sheetGait.createRow(gaitDatasRow);
				sheetGait.getRow(gaitDatasRow).createCell(STEP).setCellValue(numberOfSteps);

				sheetGait.getRow(gaitDatasRow).createCell(GAIT_PHASE).setCellValue("STANCE");

				// Pressures
				pressureDatasRow++;
				sheetPressure.createRow(pressureDatasRow);
				generatePressureData(sheetPressure, pressureDatasRow, numberOfSteps, footprintArray);

				// From step, the activated sensors related with time
				footprintsFileOut.println("Step [" + numberOfSteps + "]");
				if(footprintArray.size() > 0){
					calculateFootprint(footprintArray);
					footprintsFileOut.println();
					footprintArray =  new ArrayList<double[]>();
				}    

				// Updating data related with transition
				ground = false;
				numberOfSteps++;
				sheet.getRow(i).createCell(STEP_IN_TIME).setCellValue(numberOfSteps);

				// Initial time for the transition from ground to air
				initialTimeInAir = timestamp;

				timeInGround = timestampFinal - initialTimeInGround;        		
				sheetGait.getRow(gaitDatasRow).createCell(GAIT_INITIAL_TIME).setCellValue(initialTimeInGround);
				sheetGait.getRow(gaitDatasRow).createCell(GAIT_FINAL_TIME).setCellValue(timestampFinal);
				sheetGait.getRow(gaitDatasRow).createCell(GAIT_TOTAL_PHASE_TIME).setCellValue(timeInGround);


			// Transition from ground to air meanwhile the gait
			}else if (!ground && sheet.getRow(i).getCell(TREAD).getNumericCellValue() == INSOLE_STANCE){

				gaitDatasRow++;
				sheetGait.createRow(gaitDatasRow);
				sheetGait.getRow(gaitDatasRow).createCell(STEP).setCellValue(numberOfSteps);

				sheetGait.getRow(gaitDatasRow).createCell(GAIT_PHASE).setCellValue("SWING");            	            

				// Updating data related with transition
				ground = true;
				initialTimeInGround = timestamp;
				numberOfSteps++;
				sheet.getRow(i).createCell(STEP_IN_TIME).setCellValue(numberOfSteps);

				timeInAir = timestampFinal - initialTimeInAir;        		
				sheetGait.getRow(gaitDatasRow).createCell(GAIT_INITIAL_TIME).setCellValue(initialTimeInAir);
				sheetGait.getRow(gaitDatasRow).createCell(GAIT_FINAL_TIME).setCellValue(timestampFinal);
				sheetGait.getRow(gaitDatasRow).createCell(GAIT_TOTAL_PHASE_TIME).setCellValue(timeInAir);				

				// Step (time of the foot in the ground meanwhile the gait)
			} else if(ground && sheet.getRow(i).getCell(TREAD).getNumericCellValue() == INSOLE_STANCE){

				sheet.getRow(i).createCell(STEP_IN_TIME).setCellValue(numberOfSteps);

				double[] footprint = new double[29];
				Arrays.fill(footprint,0);

				footprint[0] = timestamp;

				halluxCurrent = sheet.getRow(i).getCell(HALLUX).getNumericCellValue();
				if(halluxCurrent >= INSOLE_THRESHOLD){        			
					footprint[HALLUX]++;        			
					if(halluxMax < halluxCurrent) halluxMax = halluxCurrent;
					halluxTotal+=halluxCurrent;

				}

				toesCurrent = sheet.getRow(i).getCell(TOES).getNumericCellValue();
				if(toesCurrent >= INSOLE_THRESHOLD){
					footprint[TOES]++;        			
					if(toesMax < toesCurrent) toesMax = toesCurrent;
					toesTotal+=toesCurrent;
				}

				metaExtCurrent = sheet.getRow(i).getCell(META_EXT).getNumericCellValue();
				if(metaExtCurrent >= INSOLE_THRESHOLD){
					footprint[META_EXT]++;        			
					if(metaExtMax < metaExtCurrent) metaExtMax = metaExtCurrent;
					metaExtTotal+=metaExtCurrent;
				}

				metaIntermCurrent = sheet.getRow(i).getCell(META_INTERM).getNumericCellValue();
				if(metaIntermCurrent >= INSOLE_THRESHOLD){
					footprint[META_INTERM]++;        			
					if(metaIntermMax < metaIntermCurrent) metaIntermMax = metaIntermCurrent;
					metaIntermTotal+=metaIntermCurrent;
				}

				metaIntCurrent = sheet.getRow(i).getCell(META_INT).getNumericCellValue();
				if(metaIntCurrent >= INSOLE_THRESHOLD){
					footprint[META_INT]++;        			
					if(metaIntMax < metaIntCurrent) metaIntMax = metaIntCurrent;
					metaIntTotal+=metaIntCurrent;
				}

				archCurrent = sheet.getRow(i).getCell(ARCH).getNumericCellValue();
				if(archCurrent >= INSOLE_THRESHOLD){
					footprint[ARCH]++;        			
					if(archMax < archCurrent) archMax = archCurrent;
					archTotal+=archCurrent;
				}

				heelIntCurrent = sheet.getRow(i).getCell(HEEL_INT).getNumericCellValue();
				if(heelIntCurrent >= INSOLE_THRESHOLD){
					footprint[HEEL_INT]++;        			
					if(heelIntMax < heelIntCurrent) heelIntMax = heelIntCurrent;
					heelIntTotal+=heelIntCurrent;
				}

				heelExtCurrent = sheet.getRow(i).getCell(HEEL_EXT).getNumericCellValue();
				if(heelExtCurrent >= INSOLE_THRESHOLD){
					footprint[HEEL_EXT]++;        			
					if(heelExtMax < heelExtCurrent) heelExtMax = heelExtCurrent;
					heelExtTotal+=heelExtCurrent;
				}

				footprintArray.add(footprint);

			}else {

				sheet.getRow(i).createCell(STEP_IN_TIME).setCellValue(numberOfSteps);

			}    
			
			timestampFinal =  sheet.getRow(i).getCell(TIME).getNumericCellValue();

		}     

		// Gait extra calculations
		if(numberOfSteps > 2) {
				
			System.out.println("Number of steps: " + numberOfSteps);
			
			removeInvalidSteps(numberOfSteps, mak);	
			
			// Calculations about gait cycle
			int lastRow = (int) sheetGait.getLastRowNum();
			for(int i = 1; i < lastRow; i++){
				
				if(sheetGait.getRow(i).getCell(GAIT_PHASE).getStringCellValue().equals("STANCE")){
				
					double groundTime = sheetGait.getRow(i).getCell(GAIT_TOTAL_PHASE_TIME).getNumericCellValue();
					double airTime = sheetGait.getRow(i+1).getCell(GAIT_TOTAL_PHASE_TIME).getNumericCellValue();

					double totalTime = groundTime+airTime;
					sheetGait.getRow(i).createCell(GAIT_STRIDE_TIME);
					sheetGait.getRow(i+1).createCell(GAIT_STRIDE_TIME).setCellValue(totalTime);

					double groundPercent = 0;
					double airPercent = 0;
					if(totalTime != 0){
						groundPercent = (groundTime*100)/totalTime;
						airPercent = 100 - groundPercent;
					}
					sheetGait.getRow(i).createCell(GAIT_PERCENT_CYCLE).setCellValue(groundPercent);
					sheetGait.getRow(i+1).createCell(GAIT_PERCENT_CYCLE).setCellValue(airPercent);
					
				}

			}	 

		}
		
	}
	
	/**
	 *  Deletion of invalid steps. 
	 *  For the trial only are taked into account complete cycles of the march
	 *  staring by the contact phase.
	 *
	 * @param numberOfSteps the number of steps
	 * @param mak the mak
	 */
	private void removeInvalidSteps(int  numberOfSteps, boolean mak) {

		System.out.println("Remove Invalid Steps");
		
		setInsoles(mak);
		
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");
		XSSFSheet sheetGait;
		if(mak) {			
			sheetGait = workBook.getSheet("GAIT_DATA_MAK");								
		}else {			
			sheetGait = workBook.getSheet("GAIT_DATA_FREE");					
		}

		int firstStep = 0;
		if(sheetGait.getRow(1).getCell(GAIT_PHASE).getStringCellValue().equals("SWING")){

			firstStep = 2;
			sheetGait.removeRow(sheetGait.getRow(1));
			sheetGait.shiftRows(2, numberOfSteps, -1);            	
			numberOfSteps--;           
		}
		else{

			firstStep = 3;
			
			sheetGait.removeRow(sheetGait.getRow(1));
			sheetGait.shiftRows(2, numberOfSteps, -1);            	
			numberOfSteps--;   

			sheetGait.removeRow(sheetGait.getRow(1));
			sheetGait.shiftRows(2, numberOfSteps, -1);            	
			numberOfSteps--;

		}

		int lastRow = (int) sheetGait.getLastRowNum();
		
		int lastStep = 0;
		if(sheetGait.getRow(sheetGait.getLastRowNum()).getCell(GAIT_PHASE).getStringCellValue().equals("STANCE")){

			lastStep = (int) sheetGait.getRow(lastRow).getCell(STEP).getNumericCellValue();
			sheetGait.removeRow(sheetGait.getRow(lastRow));
			numberOfSteps--;

		}
		else{
						
			sheetGait.removeRow(sheetGait.getRow(lastRow));
			numberOfSteps--;
			
			lastRow = (int) sheetGait.getLastRowNum();
			lastStep =  (int) sheetGait.getRow(lastRow).getCell(STEP).getNumericCellValue();
			sheetGait.removeRow(sheetGait.getRow(lastRow));
			numberOfSteps--;
			
		}

		if(mak){
			
			for (int i = 1; i < numberOfRows; i++) {
						
				double currentStep =  sheet.getRow(i).getCell(STEP_IN_TIME).getNumericCellValue();			  			  

				if(currentStep < firstStep) {
									
					sheet.removeRow(sheet.getRow(i));
					sheet.shiftRows(i+1, numberOfRows, -1);
					numberOfRows--;
					i--;
				}else if(currentStep >= lastStep) {
					
					sheet.removeRow(sheet.getRow(i));
					sheet.shiftRows(i+1, numberOfRows, -1);
					numberOfRows--;
					i--;
				}
			}  

		}
		System.out.println("Number of rows deleted: " + numberOfRows);
	}
	


	/**
	 * Generate cop velocity.
	 *
	 * @param mak the mak
	 */
	void generateCopVelocity(boolean mak){
		
		System.out.println("Calculate Centre of Pressure Velocity");
		
		setInsoles(mak);
		
		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");
				
		XSSFSheet sheetGait;
		if(mak) {		
			sheetGait = workBook.getSheet("GAIT_DATA_MAK");		
			footprintsFileOut =  footprintsFileOutMAK;			
		}else {			
			sheetGait = workBook.getSheet("GAIT_DATA_FREE");		
			footprintsFileOut =  footprintsFileOutFREE;
		}
		
		sheetGait.getRow(0).createCell(GAIT_COP_VELOCITY).setCellValue(gaitValues[GAIT_COP_VELOCITY]);
		sheetGait.autoSizeColumn(GAIT_COP_VELOCITY);		
				
		int numberOfRowsGait =sheetGait.getLastRowNum();
		for(int i = 1; i <= numberOfRowsGait ; i++) {
						
			double initialTime = sheetGait.getRow(i).getCell(GAIT_INITIAL_TIME).getNumericCellValue();
			double finalTime = sheetGait.getRow(i).getCell(GAIT_FINAL_TIME).getNumericCellValue();
						
			int j = 1;
			while(sheet.getRow(j).getCell(TIME).getNumericCellValue() < initialTime) {
				j++;
			}
			int initialIndex = j;
			// TODO - Recuperarlo si falla
			//while(sheet.getRow(j).getCell(TIME).getNumericCellValue() < (finalTime - 0.01)) {				
			while(sheet.getRow(j).getCell(TIME).getNumericCellValue() < finalTime) {
				j++;
			}
			int finalIndex = j;
		
			
			double trajectory = 0;
			for(int k = initialIndex; k < finalIndex -1 ; k++) {
				double x1 = sheet.getRow(k).getCell(RELATIVE_COP_X).getNumericCellValue();
				double x2 = sheet.getRow(k+1).getCell(RELATIVE_COP_X).getNumericCellValue();
				double y1 = sheet.getRow(k).getCell(RELATIVE_COP_Y).getNumericCellValue();
				double y2 = sheet.getRow(k+1).getCell(RELATIVE_COP_Y).getNumericCellValue();
				trajectory+= Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2 -y1,2));

			}
			
			double totalTime = finalTime-initialTime;
			double velocity = 0;
			if(totalTime != 0){
				velocity = trajectory/(totalTime);
			}
			sheetGait.getRow(i).createCell(GAIT_COP_VELOCITY).setCellValue(velocity);						
		}

		
	} 
	
	/**
	 * Generate double stance gait data.
	 *
	 * @param mak the mak
	 */
	protected void generateDoubleStanceGaitData(boolean mak) {
		
		System.out.println("Generate double stance gait values");
		
		setInsoles(mak);

		XSSFSheet sheet = workBook.getSheet("CALCULATED_DATA");
		
		XSSFSheet sheetGait;		
		if(mak) {		
			sheetGait = workBook.getSheet("GAIT_DATA_MAK");
		}else {			
			sheetGait = workBook.getSheet("GAIT_DATA_FREE");
		}
					
		sheetGait.getRow(0).createCell(GAIT_FIRST_DOUBLE_SUPPORT).setCellValue(gaitValues[GAIT_FIRST_DOUBLE_SUPPORT]);
		sheetGait.autoSizeColumn(GAIT_FIRST_DOUBLE_SUPPORT);
		sheetGait.getRow(0).createCell(GAIT_FIRST_DOUBLE_SUPPORT_CYCLE).setCellValue(gaitValues[GAIT_FIRST_DOUBLE_SUPPORT_CYCLE]);
		sheetGait.autoSizeColumn(GAIT_FIRST_DOUBLE_SUPPORT_CYCLE);
		sheetGait.getRow(0).createCell(GAIT_SECOND_DOUBLE_SUPPORT).setCellValue(gaitValues[GAIT_SECOND_DOUBLE_SUPPORT]);
		sheetGait.autoSizeColumn(GAIT_SECOND_DOUBLE_SUPPORT);
		sheetGait.getRow(0).createCell(GAIT_SECOND_DOUBLE_SUPPORT_CYCLE).setCellValue(gaitValues[GAIT_SECOND_DOUBLE_SUPPORT_CYCLE]);
		sheetGait.autoSizeColumn(GAIT_SECOND_DOUBLE_SUPPORT_CYCLE);
		
		int numberOfRowsGait =sheetGait.getLastRowNum();
		for(int i = 1; i < numberOfRowsGait ; i=i+2) {
				
			double initialTime = sheetGait.getRow(i).getCell(GAIT_INITIAL_TIME).getNumericCellValue();
			double finalTime = sheetGait.getRow(i+1).getCell(GAIT_FINAL_TIME).getNumericCellValue();
						
			int j = 1;
			while(sheet.getRow(j).getCell(TIME).getNumericCellValue() < initialTime) {
				j++;
			}
			int initialIndex = j;
			while(sheet.getRow(j).getCell(TIME).getNumericCellValue() < (finalTime - 0.01)) {				
				j++;
			}
			int finalIndex = j;
		
			double initialFirstSupport = 0;
			double finalFirstSupport = 0;		
			boolean found = false;
			int k;
			for(k = initialIndex; (k < finalIndex) && !found ; k++) {
				if(sheet.getRow(k).getCell(DOUBLE_SUPPORT).getNumericCellValue() == 1) {
					initialFirstSupport = sheet.getRow(k).getCell(TIME).getNumericCellValue();
					initialIndex = k;
					found = true;					
				}				
			}
			
			if(found != false) {
				found = false;
				for(k = initialIndex; (k < finalIndex) && !found ; k++) {
					if(sheet.getRow(k).getCell(DOUBLE_SUPPORT).getNumericCellValue() == 0) {
						finalFirstSupport = sheet.getRow(k).getCell(TIME).getNumericCellValue();
						initialIndex = k;
						found = true;					
					}				
				}
			}
			
			
			double initialSecondSupport = 0;
			double finalSecondSupport = 0;					
			found = false;
			for(k = initialIndex; (k < finalIndex) && !found ; k++) {
				if(sheet.getRow(k).getCell(DOUBLE_SUPPORT).getNumericCellValue() == 1) {
					initialSecondSupport = sheet.getRow(k).getCell(TIME).getNumericCellValue();
					initialIndex = k;
					found = true;
					
				}				
			}
			
			if(found != false) {
				found = false;
				for(k = initialIndex; (k < finalIndex) && !found ; k++) {
					if(sheet.getRow(k).getCell(DOUBLE_SUPPORT).getNumericCellValue() == 0) {
						finalSecondSupport = sheet.getRow(k).getCell(TIME).getNumericCellValue();
						initialIndex = k;
						found = true;					
					}				
				}
			}
			
			
			double fistSupportTime = finalFirstSupport - initialFirstSupport;
			if (fistSupportTime < 0) fistSupportTime = 0;
			double secondSupportTime = finalSecondSupport - initialSecondSupport;
			if (secondSupportTime < 0) secondSupportTime = 0;

			sheetGait.getRow(i).createCell(GAIT_FIRST_DOUBLE_SUPPORT);
			sheetGait.getRow(i).createCell(GAIT_SECOND_DOUBLE_SUPPORT);
			sheetGait.getRow(i+1).createCell(GAIT_FIRST_DOUBLE_SUPPORT).setCellValue(fistSupportTime);
			sheetGait.getRow(i+1).createCell(GAIT_SECOND_DOUBLE_SUPPORT).setCellValue(secondSupportTime);
			
			
			double totalStride = sheetGait.getRow(i+1).getCell(GAIT_STRIDE_TIME).getNumericCellValue();
			sheetGait.getRow(i).createCell(GAIT_FIRST_DOUBLE_SUPPORT_CYCLE);
			sheetGait.getRow(i).createCell(GAIT_SECOND_DOUBLE_SUPPORT_CYCLE);
			sheetGait.getRow(i+1).createCell(GAIT_FIRST_DOUBLE_SUPPORT_CYCLE).setCellValue((fistSupportTime*100)/totalStride);
			sheetGait.getRow(i+1).createCell(GAIT_SECOND_DOUBLE_SUPPORT_CYCLE).setCellValue((secondSupportTime*100)/totalStride);
			
		}

	}

	
	/**
	 * Generate gait params.
	 *
	 * @param mak the mak
	 */
	protected void generateGaitParams(boolean mak) {
		
		System.out.println("Generate Gait Parameters");
		
		setInsoles(mak);

		XSSFSheet sheetGait;
		XSSFSheet sheetGaitParameters;
		if(mak) {		
			sheetGait = workBook.getSheet("GAIT_DATA_MAK");
			sheetGaitParameters = workBook.createSheet("GAIT_DATA_PARAM_MAK");
		}else {			
			sheetGait = workBook.getSheet("GAIT_DATA_FREE");
			sheetGaitParameters = workBook.createSheet("GAIT_DATA_PARAM_FREE");
		}
				
		int numberOfRows = sheetGait.getLastRowNum();
	
		// STRIDE
		double totalStrideTime = 0;
		double numberOfStrides = 0;
		for(int i = 1; i<= numberOfRows; i++) {	
			if(sheetGait.getRow(i).getCell(GAIT_PHASE).getStringCellValue().equals("SWING")) {
				double strideTime = sheetGait.getRow(i).getCell(GAIT_STRIDE_TIME).getNumericCellValue();
				if( strideTime != 0){
					totalStrideTime += strideTime;
					numberOfStrides++;
				}
			}
		}
		double strideTime = totalStrideTime/numberOfStrides;
		sheetGaitParameters.createRow(GAIT_PARAM_TOTAL_STRIDE_TIME);                    
		sheetGaitParameters.getRow(GAIT_PARAM_TOTAL_STRIDE_TIME).createCell(0).setCellValue(gaitParams[GAIT_PARAM_TOTAL_STRIDE_TIME]);
		sheetGaitParameters.getRow(GAIT_PARAM_TOTAL_STRIDE_TIME).createCell(1).setCellValue(strideTime);
		sheetGaitParameters.createRow(GAIT_PARAM_TOTAL_STRIDE_CYCLE);
		sheetGaitParameters.getRow(GAIT_PARAM_TOTAL_STRIDE_CYCLE).createCell(0).setCellValue(gaitParams[GAIT_PARAM_TOTAL_STRIDE_CYCLE]);		
		sheetGaitParameters.getRow(GAIT_PARAM_TOTAL_STRIDE_CYCLE).createCell(1).setCellValue(100);

		
		// CADENCE
		double initialTime = sheetGait.getRow(1).getCell(GAIT_INITIAL_TIME).getNumericCellValue();
		double finalTime = sheetGait.getRow(numberOfRows).getCell(GAIT_FINAL_TIME).getNumericCellValue();
		sheetGaitParameters.createRow(GAIT_PARAM_CADENCE);                    
		sheetGaitParameters.getRow(GAIT_PARAM_CADENCE).createCell(0).setCellValue(gaitParams[GAIT_PARAM_CADENCE]);		
		sheetGaitParameters.getRow(GAIT_PARAM_CADENCE).createCell(1).setCellValue((((numberOfStrides*2)/(finalTime-initialTime))*60));
		
		
		// COP
		double validCops = 0;
		double totalCopVelocity = 0;
		for(int i = 1; i<= numberOfRows; i++) {			
			if(sheetGait.getRow(i).getCell(GAIT_PHASE).getStringCellValue().equals("STANCE")) {				
				double copVelocity = sheetGait.getRow(i).getCell(GAIT_COP_VELOCITY).getNumericCellValue();
				if(copVelocity != 0){
					totalCopVelocity+=copVelocity;
					validCops++;					
				}								
			}
		}
		
		double finalCopVelocity = totalCopVelocity/validCops;
		sheetGaitParameters.createRow(GAIT_PARAM_COP_VELOCITY);                    
		sheetGaitParameters.getRow(GAIT_PARAM_COP_VELOCITY).createCell(0).setCellValue(gaitParams[GAIT_PARAM_COP_VELOCITY]);		
		sheetGaitParameters.getRow(GAIT_PARAM_COP_VELOCITY).createCell(1).setCellValue(finalCopVelocity);		
		
		// STANCE
		double totalTimeStance = 0;
		for(int i = 1; i<= numberOfRows; i++) {			
			if(sheetGait.getRow(i).getCell(GAIT_PHASE).getStringCellValue().equals("STANCE")) {
				double stanceTime = sheetGait.getRow(i).getCell(GAIT_TOTAL_PHASE_TIME).getNumericCellValue();
				totalTimeStance += stanceTime;									
			}
		}		
		double stanceTime = totalTimeStance/numberOfStrides;
		sheetGaitParameters.createRow(GAIT_PARAM_STANCE_TIME);                    
		sheetGaitParameters.getRow(GAIT_PARAM_STANCE_TIME).createCell(0).setCellValue(gaitParams[GAIT_PARAM_STANCE_TIME]);		
		sheetGaitParameters.getRow(GAIT_PARAM_STANCE_TIME).createCell(1).setCellValue(stanceTime);		
		sheetGaitParameters.createRow(GAIT_PARAM_STANCE_CYCLE);                    
		sheetGaitParameters.getRow(GAIT_PARAM_STANCE_CYCLE).createCell(0).setCellValue(gaitParams[GAIT_PARAM_STANCE_CYCLE]);
		sheetGaitParameters.getRow(GAIT_PARAM_STANCE_CYCLE).createCell(1).setCellValue((100*stanceTime)/strideTime);

		
		// SWING		
		double totalTimeSwing = 0;
		for(int i = 1; i<= numberOfRows; i++) {			
			if(sheetGait.getRow(i).getCell(GAIT_PHASE).getStringCellValue().equals("SWING")) {
				double swingTime = sheetGait.getRow(i).getCell(GAIT_TOTAL_PHASE_TIME).getNumericCellValue();
				totalTimeSwing += swingTime;				
			}
		}
		double swingTime = totalTimeSwing/numberOfStrides;		
		sheetGaitParameters.createRow(GAIT_PARAM_SWING_TIME);                    
		sheetGaitParameters.getRow(GAIT_PARAM_SWING_TIME).createCell(0).setCellValue(gaitParams[GAIT_PARAM_SWING_TIME]);				
		sheetGaitParameters.getRow(GAIT_PARAM_SWING_TIME).createCell(1).setCellValue(swingTime);	
		sheetGaitParameters.createRow(GAIT_PARAM_SWING_CYCLE);                    
		sheetGaitParameters.getRow(GAIT_PARAM_SWING_CYCLE).createCell(0).setCellValue(gaitParams[GAIT_PARAM_SWING_CYCLE]);				
		sheetGaitParameters.getRow(GAIT_PARAM_SWING_CYCLE).createCell(1).setCellValue((100*swingTime)/strideTime);
				
		sheetGaitParameters.autoSizeColumn(0);

	}
	
	
	/**
	 * Generate double stance gait params.
	 *
	 * @param mak the mak
	 */
	protected void generateDoubleStanceGaitParams(boolean mak) {
		
		System.out.println("Generate Double Stance Gait Parameters");
			
		setInsoles(mak);

		XSSFSheet sheetGait;
		XSSFSheet sheetGaitParameters;
		if(mak) {		
			sheetGait = workBook.getSheet("GAIT_DATA_MAK");
			sheetGaitParameters = workBook.getSheet("GAIT_DATA_PARAM_MAK");
		}else {			
			sheetGait = workBook.getSheet("GAIT_DATA_FREE");
			sheetGaitParameters = workBook.getSheet("GAIT_DATA_PARAM_FREE");
		}
				
		int numberOfRows = sheetGait.getLastRowNum();
		
		// STRIDE
		double totalStrideTime = 0;
		double numberOfStrides = 0;
		for(int i = 1; i< numberOfRows; i++) {	
			if(sheetGait.getRow(i).getCell(GAIT_PHASE).getStringCellValue().equals("SWING")) {
				totalStrideTime += sheetGait.getRow(i).getCell(GAIT_STRIDE_TIME).getNumericCellValue();
				numberOfStrides++;
			}
		}
		double strideTime = totalStrideTime/numberOfStrides;
		
		// FIRST DOUBLE SUPPORT
		double totalTimeFirstDouble = 0;
		for(int i = 1; i< numberOfRows; i++) {			
			if(sheetGait.getRow(i).getCell(GAIT_PHASE).getStringCellValue().equals("SWING")) {
				totalTimeFirstDouble+= sheetGait.getRow(i).getCell(GAIT_FIRST_DOUBLE_SUPPORT).getNumericCellValue();								
			}
		}
		double firstStanceTime = totalTimeFirstDouble/numberOfStrides;	
		double firstStanceCycle = (100*firstStanceTime)/strideTime;
		sheetGaitParameters.createRow(GAIT_PARAM_FIRST_DOUBLE_SUPPORT_TIME);
		sheetGaitParameters.getRow(GAIT_PARAM_FIRST_DOUBLE_SUPPORT_TIME).createCell(0).setCellValue(gaitParams[GAIT_PARAM_FIRST_DOUBLE_SUPPORT_TIME]);
		sheetGaitParameters.getRow(GAIT_PARAM_FIRST_DOUBLE_SUPPORT_TIME).createCell(1).setCellValue(firstStanceTime);
		sheetGaitParameters.createRow(GAIT_PARAM_FIRST_DOUBLE_SUPPORT_CYCLE);                    
		sheetGaitParameters.getRow(GAIT_PARAM_FIRST_DOUBLE_SUPPORT_CYCLE).createCell(0).setCellValue(gaitParams[GAIT_PARAM_FIRST_DOUBLE_SUPPORT_CYCLE]);
		sheetGaitParameters.getRow(GAIT_PARAM_FIRST_DOUBLE_SUPPORT_CYCLE).createCell(1).setCellValue(firstStanceCycle);
				
		
		// SECOND DOUBLE SUPPORT
		double totalTimeSecondDouble = 0;
		for(int i = 1; i< numberOfRows; i++) {			
			if(sheetGait.getRow(i).getCell(GAIT_PHASE).getStringCellValue().equals("SWING")) {				
				totalTimeSecondDouble+= sheetGait.getRow(i).getCell(GAIT_SECOND_DOUBLE_SUPPORT).getNumericCellValue();					
			}
		}
		double secondStanceTime = totalTimeSecondDouble/numberOfStrides;
		double secondStanceCycle = (100*secondStanceTime)/strideTime;	
		sheetGaitParameters.createRow(GAIT_PARAM_SECOND_DOUBLE_SUPPORT_TIME);
		sheetGaitParameters.getRow(GAIT_PARAM_SECOND_DOUBLE_SUPPORT_TIME).createCell(0).setCellValue(gaitParams[GAIT_PARAM_SECOND_DOUBLE_SUPPORT_TIME]);
		sheetGaitParameters.getRow(GAIT_PARAM_SECOND_DOUBLE_SUPPORT_TIME).createCell(1).setCellValue(secondStanceTime);
		sheetGaitParameters.createRow(GAIT_PARAM_SECOND_DOUBLE_SUPPORT_CYCLE);                    
		sheetGaitParameters.getRow(GAIT_PARAM_SECOND_DOUBLE_SUPPORT_CYCLE).createCell(0).setCellValue(gaitParams[GAIT_PARAM_SECOND_DOUBLE_SUPPORT_CYCLE]);
		sheetGaitParameters.getRow(GAIT_PARAM_SECOND_DOUBLE_SUPPORT_CYCLE).createCell(1).setCellValue(secondStanceCycle);

		
		// TOTALS DOUBLE SUPPPORT
		double totalDoubleSupportTime = firstStanceTime + secondStanceTime;
		double totalDoubleSupportCycle = firstStanceCycle + secondStanceCycle;
		sheetGaitParameters.createRow(GAIT_PARAM_TOTAL_DOUBLE_SUPPORT_TIME);                    
		sheetGaitParameters.getRow(GAIT_PARAM_TOTAL_DOUBLE_SUPPORT_TIME).createCell(0).setCellValue(gaitParams[GAIT_PARAM_TOTAL_DOUBLE_SUPPORT_TIME]);
		sheetGaitParameters.getRow(GAIT_PARAM_TOTAL_DOUBLE_SUPPORT_TIME).createCell(1).setCellValue(totalDoubleSupportTime);
		
		sheetGaitParameters.createRow(GAIT_PARAM_TOTAL_DOUBLE_SUPPORT_CYCLE);                    
		sheetGaitParameters.getRow(GAIT_PARAM_TOTAL_DOUBLE_SUPPORT_CYCLE).createCell(0).setCellValue(gaitParams[GAIT_PARAM_TOTAL_DOUBLE_SUPPORT_CYCLE]);		
		sheetGaitParameters.getRow(GAIT_PARAM_TOTAL_DOUBLE_SUPPORT_CYCLE).createCell(1).setCellValue(totalDoubleSupportCycle);

		
		// SINGLE SUPPPORT
		double stanceTime = sheetGaitParameters.getRow(GAIT_PARAM_STANCE_TIME).getCell(1).getNumericCellValue();
		double singleSupportTime = stanceTime - totalDoubleSupportTime;	
		double singleSupportCycle = (100*singleSupportTime)/strideTime;	
		sheetGaitParameters.createRow(GAIT_PARAM_SINGLE_SUPPORT_TIME);                    
		sheetGaitParameters.getRow(GAIT_PARAM_SINGLE_SUPPORT_TIME).createCell(0).setCellValue(gaitParams[GAIT_PARAM_SINGLE_SUPPORT_TIME]);
		sheetGaitParameters.getRow(GAIT_PARAM_SINGLE_SUPPORT_TIME).createCell(1).setCellValue(singleSupportTime);
		
		sheetGaitParameters.createRow(GAIT_PARAM_SINGLE_SUPPORT_CYCLE);                    
		sheetGaitParameters.getRow(GAIT_PARAM_SINGLE_SUPPORT_CYCLE).createCell(0).setCellValue(gaitParams[GAIT_PARAM_SINGLE_SUPPORT_CYCLE]);		
		sheetGaitParameters.getRow(GAIT_PARAM_SINGLE_SUPPORT_CYCLE).createCell(1).setCellValue(singleSupportCycle);
				
		
		sheetGaitParameters.autoSizeColumn(0);
		

	}


	

	/**
	 *  Creation of charts in Excel.
	 *
	 * @param channel the channel
	 * @throws Exception the exception
	 */
	public void createChart(int channel) throws Exception {

		System.out.println("Generate chart " + channel);

		XSSFSheet sheet = workBook.getSheet("RAW");

		XSSFSheet plotSheet = workBook.getSheet("Channel" + channel);
		if (plotSheet != null) {
			int index = workBook.getSheetIndex(sheet);
			workBook.removeSheetAt(index);
		}
		plotSheet = workBook.createSheet("Channel" + channel);

		XSSFDrawing drawing = plotSheet.createDrawingPatriarch();
		XSSFClientAnchor anchor = drawing.createAnchor(5, 5, 5, 5, 2, 6, 15, 20);

		XSSFChart chart = drawing.createChart(anchor);
		XSSFChartLegend legend = chart.getOrCreateLegend();
		legend.setPosition(LegendPosition.TOP_RIGHT);

		// Use a category axis for the bottom axis.
		XSSFChartAxis bottomAxis
		= chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
		XSSFValueAxis leftAxis
		= chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
		leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

		ChartDataSource<Number> xs = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(1, numberOfRows - 1, RAW_TIME, RAW_TIME));
		ChartDataSource<Number> ys = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(1, numberOfRows - 1, channel, channel));

		LineChartData data = chart.getChartDataFactory().createLineChartData();
		LineChartSeries series1 = data.addSeries(xs, ys);
		series1.setTitle("Channel" + channel);

		chart.plot(data, bottomAxis, leftAxis);

	}
	
	/**
	 *  Generation of .csv  files for each generated sheet
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void generateSheetCSV() throws IOException{

		generateCSV("RAW");
		generateCSV("CALCULATED_DATA");
		
		generateCSV("GAIT_DATA_MAK");
		if(workBook.getSheet("GAIT_DATA_FREE") != null) {
			generateCSV("GAIT_DATA_FREE");
		}		
		generateCSV("PRESSURE_DATA_MAK");
		if(workBook.getSheet("PRESSURE_DATA_FREE") != null) {
			generateCSV("PRESSURE_DATA_FREE");
		}
	}
	
	/**
	 *  CSV generation.
	 *
	 * @param sheetName the sheet name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void generateCSV(String sheetName) throws IOException{

		FormulaEvaluator fe =  workBook.getCreationHelper().createFormulaEvaluator();

		XSSFSheet sheet = workBook.getSheet(sheetName);

		DataFormatter formatter = new DataFormatter();
		PrintStream out;

		out = new PrintStream(new FileOutputStream(outputPath + ".CSV." + sheetName + ".csv"), true, "UTF-8");

		byte[] bom = {(byte)0xEF, (byte)0xBB, (byte)0xBF};
		out.write(bom);

		for (int r = 0, rn = sheet.getLastRowNum() ; r <= rn ; r++) {
			XSSFRow row = sheet.getRow(r);
			if ( row == null ) { out.println(','); continue; }
			boolean firstCell = true;
			for (int c = 0, cn = row.getLastCellNum() ; c < cn ; c++) {
				Cell cell = row.getCell(c, XSSFRow.MissingCellPolicy.RETURN_BLANK_AS_NULL);
				if ( ! firstCell ) out.print(',');
				if ( cell != null ) {
					if ( fe != null ) cell = fe.evaluateInCell(cell);
					String value = formatter.formatCellValue(cell);
					if ( cell.getCellTypeEnum() == CellType.FORMULA ) {
						value = "=" + value;
					}
					out.print(encodeValue(value));
				}
				firstCell = false;
			}
			out.println();
		}    	
	}

	/**
	 *  CSV generation.
	 *
	 * @param value the value
	 * @return the string
	 */
	private String encodeValue(String value) {
		Pattern rxquote = Pattern.compile("\"");
		boolean needQuotes = false;
		if(value.indexOf(',') != -1) value = value.replace(',', '.');
		if (value.indexOf('"') != -1 || value.indexOf('\n') != -1 || value.indexOf('\r') != -1 )
			needQuotes = true;
		Matcher m = rxquote.matcher(value);
		if ( m.find() ) needQuotes = true; value = m.replaceAll("\"\"");
		if ( needQuotes ) return "\"" + value + "\"";
		else return value;
	}
	
	
	/**
	 *  Excel file generation.
	 *
	 * @param file the file
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void createFile(String file) throws FileNotFoundException, IOException {

		System.out.println("Create File");

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		workBook.write(fileOutputStream);
		fileOutputStream.close();

	}
	
	/**
	 *  Definition of the information that is provided in each one of the channels.
	 */
	public abstract void channelsInitialization();
	
	/**
	 *  Definition of the calculated data that is going to be generated.
	 */
	public abstract void setCalculatedDataValuesColumns();	
	
	/**
	 *  Processing of raw data for the obtaining of meaningful values.
	 */
	protected abstract void calculateExtraData();
	
	/**
	 * Generate output files.
	 *
	 * @throws Exception the exception
	 */
	public abstract void generateOutputFiles() throws Exception;
	
	/**
	 * Calculate euler data.
	 */
	public abstract void calculateEulerData();

}