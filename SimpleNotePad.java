// Evan Bariquit
// Notepad Application


import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;

public class SimpleNotePad extends JFrame{
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
    
   public SimpleNotePad() {
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
    
   public static void main(String[] args) {
       SimpleNotePad app = new SimpleNotePad();
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
    		   newFile_ActionPerformed(evt);
            }
       });
       nf.setActionCommand("new");
   }
      
   public void newFile_ActionPerformed(ActionEvent evt) {   
	   d.setText("");
   }
    
   public void addSaveFileFeature() {   
	   fm.add(sf);      
	   sf.addActionListener(new java.awt.event.ActionListener() {        
		   public void actionPerformed(ActionEvent evt) {        
			   save_ActionPerformed(evt);
		   }
	   });
	   sf.setActionCommand("save");
   }
    
   public void save_ActionPerformed(ActionEvent evt) {
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
    
   public void addPrintFileFeature() {
       fm.add(pf);        
       pf.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(ActionEvent evt) {
               print_ActionPerformed(evt);
           }
       });
       pf.setActionCommand("print");
   }
    
   public void print_ActionPerformed(ActionEvent evt) {
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
    
   public void addCopyFeature() {
       em.add(c);
       c.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(ActionEvent evt) {
               copy_ActionPerformed(evt);
           }
       });
       c.setActionCommand("copy");
   }
    
   public void copy_ActionPerformed(ActionEvent evt) {
   	d.copy();
   }
    
   public void addPasteFeature() {
       em.add(p);
       p.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(ActionEvent evt) {
               paste_ActionPerformed(evt);
           }
       });
       p.setActionCommand("paste");
   }
    
   public void paste_ActionPerformed(ActionEvent evt) {
      StyledDocument doc = d.getStyledDocument();
      Position position = doc.getEndPosition();
      System.out.println("offset"+position.getOffset());
      d.paste();        
   }
   
   // Implementation of new requirements
   public void addOpenFeature() {
       fm.add(open);
       open.addActionListener(new java.awt.event.ActionListener() {
    	   public void actionPerformed(ActionEvent evt) {
    		   open_ActionPerformed(evt);
    	   }
        });
       open.setActionCommand("open");
   }
   
   public void open_ActionPerformed(ActionEvent evt) {
	   JFileChooser jFileChooser = new JFileChooser();   	
	   int result = jFileChooser.showOpenDialog(this);    	
	   if(result == JFileChooser.APPROVE_OPTION) {
		   File file = jFileChooser.getSelectedFile();  
		   openFile(file);
    	}   
   }
   
   public void addOpenRecentFeature() {
	   // store recents in ArrayList - easier to add to for large amount of files
       recents = findTextFiles("C:\\Users\\Owner\\Documents", recents);
       // convert recents ArrayList to arrary - easier to sort
       File[] recentArr = arrayListToSortedArray(recents);
       // add 5 most recent files to 'openRecent' submenu
       add5RecentFiles(openRecent, recentArr);
       fm.add(openRecent);
   }
   
   // Opens a file. Repeated code for 'Open'
   // and 'Open Recent'
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
            d.setText(buffer.toString());
        } 
		catch (FileNotFoundException ex) {} 
		catch (IOException ex) {}   
   }
   
   public void addReplaceFeature() {
       em.add(replace);
       replace.addActionListener(new java.awt.event.ActionListener() {        		
    	   public void actionPerformed(ActionEvent evt) {    		   
    		   replace_ActionPerformed(evt);
     	   }        
       });
   }
    
   public void replace_ActionPerformed(ActionEvent evt) {
	   JFrame frame = new JFrame("replace");
	   Container pane = frame.getContentPane();
	   pane.setLayout(new GridBagLayout());
	   GridBagConstraints c = new GridBagConstraints();    	
	   Label replacement = new Label("Replace with");
	   c.gridy = 1;  
	   pane.add(replacement, c);
	   JTextField replaceField = new JTextField(5);
	   c.gridy = 2; 
	   pane.add(replaceField, c);
	   JButton replace = new JButton("Replace");
	   replace.addActionListener(new java.awt.event.ActionListener() {
		   public void actionPerformed(ActionEvent evt) {
			   d.replaceSelection(replaceField.getText());
		   }
	   });
	   frame.add(replace, c);
	   frame.setVisible(true);
   }  
  
   public void add5RecentFiles(JMenu menu, File[] arr) {
	   for(int i = 0; i < 5; i++) {	    
		   final int j = i;	        
		   String f = (i+1) + ". " +arr[i].toString();	  
		   JMenuItem recent = new JMenuItem(f);	    
		   openRecent.add(recent);	        
		   recent.addActionListener(new java.awt.event.ActionListener() {
			   public void actionPerformed(ActionEvent evt) {
				   openFile(arr[j]);
			   }	        	
		   });	        
	   }
   }
   
   public ArrayList<File> findTextFiles(String directory, ArrayList<File> files) {
	   File dir = new File(directory);
	   File[] fList = dir.listFiles();
	   if(fList != null) {
		   for (File file: fList ) {
			   if(file.isFile() && file.toString().toLowerCase().endsWith(".txt")) {
				   files.add(file);
			   } else if (file.isDirectory()) {
				   findTextFiles(file.getAbsolutePath(), recents);
			   }
		   }
	   }	
	   return recents;
   }

   public File[] arrayListToSortedArray(ArrayList<File> aL) {
	   File[] recentArr = new File[recents.size()];
	   for(int i = 0; i < recents.size(); i++) {
		   recentArr[i] = recents.get(i);
	   }
	   // sort 
	   Arrays.sort(recentArr, new Comparator<File>() {
		   public int compare(File f1, File f2) {
			   return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
		   }
	   });
	   return recentArr;
   }

	
}