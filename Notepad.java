
// Evan Bariquit
// Notepad Application


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Notepad extends JFrame {
    JMenuBar menuBar = new JMenuBar();
    JTextPane textPane = new JTextPane();
    
    // 'File' dropdown menu components
    JMenu file_menu = new JMenu("File");				
    JMenuItem newfile_button = new JMenuItem("New File");
    JMenuItem savefile_button = new JMenuItem("Save File");
    JMenuItem printfile_button = new JMenuItem("Print File");
    JMenuItem openFile_button = new JMenuItem("Open");
    JMenu recentFiles_submenu = new JMenu("Open Recent");
    ArrayList<File> recents = new ArrayList<File>();			
    
    // 'Edit' dropdown menu components
    JMenu edit_menu = new JMenu("Edit");
    JMenuItem copyText_button = new JMenuItem("Copy");
    JMenuItem pasteText_button = new JMenuItem("Paste");
    JMenuItem replaceText_button = new JMenuItem("Replace");		

    public static void main(String[] args) {
    	Notepad notepad = new Notepad();
    	notepad.launch();
    }
    
    public void launch() {
    	addOpenFeature();      
    	addOpenRecentFeature();    
    	addNewFileFeature();      
    	addSaveFileFeature();      
    	addPrintFileFeature();      
    	addCopyFeature();      
    	addPasteFeature();      
    	addReplaceFeature();
    	buildGUI();
    }
    
    public void buildGUI() {
    	menuBar.add(file_menu);
	   	menuBar.add(edit_menu);
	   	setJMenuBar(menuBar);
	   	add(new JScrollPane(textPane));
	   	setPreferredSize(new Dimension(600,600));
	   	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   	setVisible(true);
	   	pack();
    }
    
    public void addNewFileFeature() {
    	file_menu.add(newfile_button);
	   	newfile_button.addActionListener(new java.awt.event.ActionListener() {            
	   		public void actionPerformed(ActionEvent evt) {              
	   			textPane.setText("");            
	   		}
	   	});
	   	newfile_button.setActionCommand("new");
    }
        
    public void addSaveFileFeature() {   
    	file_menu.add(savefile_button);      
    	savefile_button.addActionListener(new java.awt.event.ActionListener() {        
    		public void actionPerformed(ActionEvent evt) {        
    			File fileToWrite = null;
    			JFileChooser fc = new JFileChooser();
    			int returnVal = fc.showSaveDialog(null);
    			if (returnVal == JFileChooser.APPROVE_OPTION)
    				fileToWrite = fc.getSelectedFile();
    			try {
    				PrintWriter out = new PrintWriter(new FileWriter(fileToWrite));
    				out.println(textPane.getText());
    				JOptionPane.showMessageDialog(null, "File is saved successfully...");
    				out.close();
    			} catch (IOException ex) {}
    		}
    	});
    	savefile_button.setActionCommand("save");
    }
        
    public void addPrintFileFeature() {
    	file_menu.add(printfile_button);        
    	printfile_button.addActionListener(new java.awt.event.ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			try{
    				PrinterJob pjob = PrinterJob.getPrinterJob();
    				pjob.setJobName("Sample Command Pattern");
    				pjob.setCopies(1);
    				pjob.setPrintable(new Printable() {          
    					public int print(Graphics pg, PageFormat printfile_button, int pageNum) { 
    						if (pageNum>0){
    							return Printable.NO_SUCH_PAGE;
    						}
    						pg.drawString(textPane.getText(), 500, 500);
    						paint(pg);
    						return Printable.PAGE_EXISTS;                 
    					}        	   
    				});
    				if (pjob.printDialog() == false){
    					return;
    				}
    				pjob.print();         
    			} catch (PrinterException pe) {
    				JOptionPane.showMessageDialog(null,
    						"Printer error" + pe, "Printing error",
    						JOptionPane.ERROR_MESSAGE);
    			}
    		}
    	});
    	printfile_button.setActionCommand("print");
    }
   
        
    public void addCopyFeature() {
    	edit_menu.add(copyText_button);
    	copyText_button.addActionListener(new java.awt.event.ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			textPane.copy();
    		}
    	});
    	copyText_button.setActionCommand("copy");
    }
    
    
    public void addPasteFeature() {
       edit_menu.add(pasteText_button);
       pasteText_button.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(ActionEvent evt) {
        	   textPane.paste(); 
           }
       });
       pasteText_button.setActionCommand("paste");
    }
   
    
    public void addOpenFeature() {
    	file_menu.add(openFile_button);
    	openFile_button.addActionListener(new java.awt.event.ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			JFileChooser jFileChooser = new JFileChooser();   	
    			int result = jFileChooser.showOpenDialog(getParent());    	
    			if(result == JFileChooser.APPROVE_OPTION) {
    				File file = jFileChooser.getSelectedFile();  
    				openFile(file);
    			} 
    		}
    	});
    	openFile_button.setActionCommand("open");
    }
   
       
    public void addOpenRecentFeature() {
    	// store recents in ArrayList - easier to add to for large amount of files
    	recents = findTextFiles("C:\\Users\\Owner\\Documents", recents);
    	// convert recents ArrayList to arrary - easier to sort
    	File[] recentArr = arrayListToSortedArray(recents);
    	// add 5 most recent files to 'openRecent' submenu
    	addRecentFiles(5, recentFiles_submenu, recentArr);
    	file_menu.add(recentFiles_submenu);
    }
   
    
    public void addReplaceFeature() {
    	edit_menu.add(replaceText_button);
    	replaceText_button.addActionListener(new java.awt.event.ActionListener() {        		
    		public void actionPerformed(ActionEvent evt) {
    			// Setup GUI
    			JFrame frame = new JFrame("replace");
    			Container pane = frame.getContentPane();
    			pane.setLayout(new GridBagLayout());
    		   	GridBagConstraints c = new GridBagConstraints();    	
    		   	Label label = new Label("Replace with");
    		   	c.gridy = 1;  
    		   	pane.add(label, c);
    		   	// User input field
    		   	JTextField replaceField = new JTextField(5);
    		   	c.gridy = 2; 
    		   	pane.add(replaceField, c);
    		   	JButton replace = new JButton("Replace");
    		   	replace.addActionListener(new java.awt.event.ActionListener() {
    		   		public void actionPerformed(ActionEvent evt) {
    		   			// Swaps selected text with user input
    		   			textPane.replaceSelection(replaceField.getText());
    		   		}
    		   	});
    		   	frame.add(replace, c);
    		   	frame.setVisible(true);
     	   	}        
    	});
    }	
   
    @SuppressWarnings("resource")
    public void openFile(File openThis) {
    	try {  
    		StringBuffer buffer = new StringBuffer();
    		FileReader reader = new FileReader(openThis);
    		int i = 1;
    		while(i != -1) {
    			i = reader.read();
    			buffer.append((char) i);
    		}
    		// Remove EOF character
    		buffer.deleteCharAt(buffer.length()-1);
    		textPane.setText(buffer.toString());
    	} 
		catch (FileNotFoundException ex) {} 
		catch (IOException ex) {}   
    }

    // Adds files to the 'Open Recent' submenu.
    public void addRecentFiles(int size, JMenu menu, File[] fileArr) {
    	for(int i = 0; i < size; i++) {	    
    		int j = i;
    		// Display file name without file path
    		String fullName = (i+1) + ". " + fileArr[i].toString();
    		String f = fullName.substring(fullName.lastIndexOf("\\") + 1, fullName.length());
    		JMenuItem recent = new JMenuItem(f);
    		// Add file to submenu
    		recentFiles_submenu.add(recent);
    		// File opens when clicked
    		recent.addActionListener(new java.awt.event.ActionListener() {
    			public void actionPerformed(ActionEvent evt) {
    				openFile(fileArr[j]);
    			}	        	
    		});	        
    	}
    }
   
    // Returns an ArrayList of .txt Files
    public ArrayList<File> findTextFiles(String directory, ArrayList<File> files) {
    	// Open a directory into an array
    	File dir = new File(directory);
    	File[] fileList = dir.listFiles();
    	if(fileList != null) {
    		for (File file: fileList ) {
    			// Only add .txt files our ArrayList
    			if(file.isFile() && file.toString().toLowerCase().endsWith(".txt")) {
    				files.add(file);
    			} 
    			// Repeat this process if the file is a directory
    			else if (file.isDirectory()) {
    				findTextFiles(file.getAbsolutePath(), files);
    			}
    		}
    	}	
    	return files;
    }

    // Converts an ArrayList to an Array.
    // Sorts and returns said Array
    public File[] arrayListToSortedArray(ArrayList<File> aL) {
    	File[] recentArr = new File[aL.size()];
    	for(int i = 0; i < aL.size(); i++) {
    		recentArr[i] = aL.get(i);
    	}
    	// Sort based on date last modified  
    	Arrays.sort(recentArr, new Comparator<File>() {
    		public int compare(File f1, File f2) {
    			return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
    		}
    	});
    	return recentArr;
    }
	
}