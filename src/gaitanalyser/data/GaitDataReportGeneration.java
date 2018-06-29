package gaitanalyser.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class GaitDataReportGeneration{

	/**
	 * @param args
	 * @throws IOException
	 * @throws InvalidFormatException 
	 */
	
	/**  Input file name  */
	public String fileName;
	/**  Path for input file  */
	public String filePath;
	public String outputPath;

	
	public GaitDataReportGeneration(String file) throws FileNotFoundException, IOException {
		
		this.filePath = file;
		fileName = filePath.substring(filePath.lastIndexOf("\\")+1,filePath.length());
		int index = fileName.indexOf(".");
		if(index != -1) fileName = fileName.substring(0, index);
		System.out.println("File name: " + fileName);
		
		outputPath = filePath.substring(0, filePath.lastIndexOf("\\"));
		filePath = filePath.substring(0, filePath.lastIndexOf("\\")) + "\\"+ fileName +  "_FILES" + "\\";
		 
		
		System.out.println("File path: " +  filePath);		
		
	}

	public void generateReport() throws InvalidFormatException, IOException {
		
		System.out.println("Generating report...");
		
		ArrayList listofImages = new ArrayList();
		
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".png")) {
				System.out.println("File " + listOfFiles[i].getName());
				listofImages.add(filePath + "\\"+ listOfFiles[i].getName());
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}

		
		 XWPFDocument doc = new XWPFDocument();

		 for(int i = 0; i < listofImages.size(); i++) {
			 
			String filePath = (String) listofImages.get(i);			
			String fileName = filePath.substring(filePath.lastIndexOf("\\")+1,filePath.length());
			int index = fileName.indexOf(".");
			if(index != -1) fileName = fileName.substring(0, index);
				
			System.out.println("Adding " + fileName);
				
			XWPFParagraph title = doc.createParagraph();
			title.setAlignment(ParagraphAlignment.LEFT);
			XWPFRun run = title.createRun();
			run.setText(fileName);
			run.setBold(true);
			run.setFontSize(15);
			//run.setFontFamily("Tahona");
			run.addBreak();
			run.addBreak();

			FileInputStream is = new FileInputStream(filePath);
			run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, fileName, Units.toEMU(468), Units.toEMU(276)); // 200x200 pixels
			is.close();

		 }
		 
		 String outputFile = outputPath + "\\" + fileName + ".docx";
		 System.out.println(outputFile);
		 FileOutputStream fos = new FileOutputStream(outputFile);

		 doc.write(fos);
		 fos.close();   
		 
		 System.out.println("Report done");
		     
	}
	
	
	public static void main(String[] args) throws InvalidFormatException, IOException{
		
		String inputFile = "D:\\Ven sube a mi nube\\BME\\TFM\\Datas\\FINAL\\MAK-HAM-P2-Paciente1-SG-M4";
		System.out.println("Input File: " + inputFile);
		   
		GaitDataReportGeneration report = new GaitDataReportGeneration(inputFile);
		report.generateReport();
		
	
	}
	
	
}