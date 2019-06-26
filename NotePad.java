// Evan Bariquit
// Notepad Application


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class NotePad extends JFrame{
    JMenuBar mb = new JMenuBar();
    JMenu fm = new JMenu("File");
    JMenu em = new JMenu("Edit");
    JTextPane d = new JTextPane();
    JMenuItem nf = new JMenuItem("New File");
    JMenuItem sf = new JMenuItem("Save File");
    JMenuItem pf = new JMenuItem("Print File");
    JMenuItem c = new JMenuItem("Copy");
    JMenuItem p = new JMenuItem("Paste");
    
    // Added to implement new requirements
    JMenuItem open = new JMenuItem("Open");				
    JMenu openRecent = new JMenu("Open Recent");
    ArrayList<File> recents = new ArrayList<File>();			
    JMenuItem replace = new JMenuItem("Replace");		
    
   public NotePad() {
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
    
   @SuppressWarnings("unused")
   public static void main(String[] args) {
       NotePad app = new NotePad();
   }
   
   public void buildGUI() {
	   	mb.add(fm);
	   	mb.add(em);
	   	setJMenuBar(mb);
	   	add(new JScrollPane(d));
	   	setPreferredSize(new Dimension(600,600));
	   	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   	setVisible(true);
	   	pack();
   }
   
   public void addNewFileFeature() {
	   fm.add(nf);
       nf.addActionListener(new java.awt.event.ActionListener() {            
    	   public void actionPerformed(ActionEvent evt) {              
    		   d.setText("");            
    	   }
       });
       nf.setActionCommand("new");
   }
    
   public void addSaveFileFeature() {   
	   fm.add(sf);      
	   sf.addActionListener(new java.awt.event.ActionListener() {        
		   public void actionPerformed(ActionEvent evt) {        
			   File fileToWrite = null;
		       JFileChooser fc = new JFileChooser();
		       int returnVal = fc.showSaveDialog(null);
		       if (returnVal == JFileChooser.APPROVE_OPTION)
		    	   fileToWrite = fc.getSelectedFile();
		       try {
		           PrintWriter out = new PrintWriter(new FileWriter(fileToWrite));
		           out.println(d.getText());
		           JOptionPane.showMessageDialog(null, "File is saved successfully...");
		           out.close();
		       } catch (IOException ex) {}
		   }
	   });
	   sf.setActionCommand("save");
   }
    
   public void addPrintFileFeature() {
       fm.add(pf);        
       pf.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(ActionEvent evt) {
        	   try{
        		   PrinterJob pjob = PrinterJob.getPrinterJob();
                   pjob.setJobName("Sample Command Pattern");
                   pjob.setCopies(1);
                   pjob.setPrintable(new Printable() {          
                	   public int print(Graphics pg, PageFormat pf, int pageNum) {
                             
                		   if (pageNum>0){
                			   return Printable.NO_SUCH_PAGE;
                		   }
                           pg.drawString(d.getText(), 500, 500);
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
       pf.setActionCommand("print");
   }
    
   public void addCopyFeature() {
       em.add(c);
       c.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(ActionEvent evt) {
        	   d.copy();
           }
       });
       c.setActionCommand("copy");
   }
    
   public void addPasteFeature() {
       em.add(p);
       p.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(ActionEvent evt) {
        	   d.paste(); 
           }
       });
       p.setActionCommand("paste");
   }
   
   // Implementation of new requirement.
   // Calls new method: 
   //	'openFile(File)'
   public void addOpenFeature() {
       fm.add(open);
       open.addActionListener(new java.awt.event.ActionListener() {
    	   public void actionPerformed(ActionEvent evt) {
    		   JFileChooser jFileChooser = new JFileChooser();   	
    		   int result = jFileChooser.showOpenDialog(getParent());    	
    		   if(result == JFileChooser.APPROVE_OPTION) {
    			   File file = jFileChooser.getSelectedFile();  
    			   openFile(file);
    	    	} 
    	   }
        });
       open.setActionCommand("open");
   }
   
   // Implementation of new requirement.
   // Calls new methods:
   //	'findTextFiles(String,ArrayList<File>)'		-> returns ArrayList of .txt files
   //	'arrayListToSortedArray(ArrayList<File>)'	-> converts ArrayList to Array & sorts
   //	'add5RecentFiles(JMenu,File[])'				-> adds 5 files from array to recent files menu
   public void addOpenRecentFeature() {
	   // store recents in ArrayList - easier to add to for large amount of files
	   recents = findTextFiles("C:\\Users\\Owner\\Documents", recents);
       // convert recents ArrayList to arrary - easier to sort
       File[] recentArr = arrayListToSortedArray(recents);
       // add 5 most recent files to 'openRecent' submenu
       addRecentFiles(5, openRecent, recentArr);
       fm.add(openRecent);
   }
   
   // Implementation of new requirement.
   //	Builds new GUI for 'Replace With' window.
   public void addReplaceFeature() {
       em.add(replace);
       replace.addActionListener(new java.awt.event.ActionListener() {        		
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
    				   d.replaceSelection(replaceField.getText());
    			   }
    		   });
    		   frame.add(replace, c);
    		   frame.setVisible(true);
     	   }        
       });
   }
   
   // Repeated code for 'Open' and 'Open Recent' features.
   // Compressed into one method.
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
            d.setText(buffer.toString());
        } 
		catch (FileNotFoundException ex) {} 
		catch (IOException ex) {}   
   }

   // Adds files to the 'Open Recent' submenu.
   // Calls new method:
   //	'openFile(File)'
   public void addRecentFiles(int size, JMenu menu, File[] fileArr) {
	   for(int i = 0; i < size; i++) {	    
		   int j = i;
		   // Display file name without file path
		   String fullName = (i+1) + ". " + fileArr[i].toString();
		   String f = fullName.substring(fullName.lastIndexOf("\\") + 1, fullName.length());
		   JMenuItem recent = new JMenuItem(f);
		   // Add file to submenu
		   openRecent.add(recent);
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

   // Converts an ArrayList to an Array,
   // sorts, and returns said Array
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