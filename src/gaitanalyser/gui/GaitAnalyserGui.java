package gaitanalyser.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import gaitanalyser.GaitAnalyser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.FlowLayout;


/**
 * The Class GaitAnalyserGui constitute the entry point to the application providing a friendly environment for the inexperienced user.
 */
public class GaitAnalyserGui extends JFrame{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
		
	/** The insole size. */
	private String insoleSize;
	
	/** The input file with his absolute path. */
	private String absolutePath;
	
	/** The trial. */
	private String trial;
	
	/** The mak leg. */
	private String makLeg;	
	
	/**
	 * Create the frame.
	 */
	public GaitAnalyserGui() {
				
		setTitle("Gait Analyser"); 

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				 
		JLabel lblInsole = new JLabel("Insole Size");
		getContentPane().add(lblInsole);
		String[] insoles = { "S", "M", "L", "XL"};
		JComboBox  insoleBox = new JComboBox(insoles);
		insoleBox.setSelectedIndex(0);
		getContentPane().add(insoleBox);
		insoleBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
			}
		});
		
		JLabel lblTrial = new JLabel("Trial");
		getContentPane().add(lblTrial);
		String[] trialsCombo = { "TRIAL_1", "TRIAL_2"};
		JComboBox trialBox = new JComboBox(trialsCombo);
		trialBox.setSelectedIndex(0);
		getContentPane().add(trialBox);
		trialBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
			}
		});
		
		JLabel lblMakLeg = new JLabel("MAK Leg");
		getContentPane().add(lblMakLeg);
		String[] makLegCombo = { "LEFT", "RIGHT"};
		JComboBox makBox = new JComboBox(makLegCombo);
		makBox.setSelectedIndex(0);
		getContentPane().add(makBox);
		makBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
			}
		});		
		

        // Set default path
        String path = System.getProperty("user.dir");
        String configFile = path +  "\\" + "gaitanalyser.properties";		    
        Properties properties = new Properties();
        try {
        	properties.load(new FileInputStream(configFile));
        } catch (Exception e) { e.printStackTrace();}        
        // Create a file chooser
        String defaultDataPath =  properties.getProperty("DEFAULT_DATA_PATH");
        
		JButton chooserButton = new JButton("Select File");
		getContentPane().add(chooserButton);		
		chooserButton.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser(defaultDataPath);
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					absolutePath = selectedFile.getAbsolutePath();
				}
			}
		});
		
		JButton processingButton = new JButton("Start Proccessing");
		getContentPane().add(processingButton);		
		processingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				insoleSize =  (String) insoleBox.getSelectedItem();
				trial = (String) trialBox.getSelectedItem();
				makLeg =  (String) makBox.getSelectedItem();
				
						
				try {
					GaitAnalyser.performGaitAnalysis(absolutePath, trial, makLeg, insoleSize);
				} catch (Exception e) {					
					e.printStackTrace();
				}
				
			}
		});
		
		
		JButton reportButton = new JButton("Generate Report");
		getContentPane().add(reportButton);		
		reportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				try {
					GaitAnalyser.generateReport(absolutePath);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		});
		
		pack();		
		setVisible(true);
			
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {								   
					GaitAnalyserGui frame = new GaitAnalyserGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
