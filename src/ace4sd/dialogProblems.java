package ace4sd;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;


import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;

import java.io.*; 
import java.net.*;
import java.util.ArrayList;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

// Class that controls the window of quality problems of the PA
public class dialogProblems extends Dialog {

	protected Object result;
	public Shell shell;
	private StyledText styledText;
	private ArrayList<codeProblems> code_problemsList;
	
	// This keeps the current text position in the TAB
	private int currentTextPosition = 0;

	// This keeps the current number of lines in the TAB
	private int currentNumberLines = 0;
	 
	// flag to print only once the date
	private int flag_date = 0;
	
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 * @wbp.parser.constructor
	 */
	
	public dialogProblems(Shell parent, ArrayList<codeProblems> code_problemsList){
		super(parent, 0);
		setText("ACE4SD - Quality Problems Window");
		this.code_problemsList = code_problemsList;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() { //Object
		createContents();
		
		
		Display display = getParent().getDisplay();
		
		// center the window
		Rectangle screenSize = display.getPrimaryMonitor().getBounds();
		shell.setLocation((screenSize.width - shell.getBounds().width) / 2, (screenSize.height - shell.getBounds().height) / 2);
		
		
		
		
		shell.open();
		shell.layout();
		
		    Color black = shell.getDisplay().getSystemColor(SWT.COLOR_BLACK);
	    	Color white = shell.getDisplay().getSystemColor(SWT.COLOR_WHITE);
	      	/*Color blue = shell.getDisplay().getSystemColor(SWT.COLOR_BLUE);*/
	   	    Color red = shell.getDisplay().getSystemColor(SWT.COLOR_RED);
	      	Color blue = shell.getDisplay().getSystemColor(SWT.COLOR_BLUE);
	      	Color grey = new Color(shell.getDisplay(), 212, 208, 200);
	        Font font_PA = new Font(shell.getDisplay(), "Times New Roman", 12, SWT.BOLD);
	        Font font_Welcome = new Font(shell.getDisplay(), "Mongolian Baiti", 14, SWT.BOLD);
	        Font font_Welcome2 = new Font(shell.getDisplay(), "Arial", 11, SWT.BOLD);
	        StyleRange styleRange = new StyleRange();
	        
		for(codeProblems xx : code_problemsList){
			  
			if(flag_date == 0){
				flag_date = 1;
				
				// DATE
				   styledText.append("DATE : " + xx.get_date() + "\n\n");
				   updatecurrentNumberLines(2);
				
				   //styledText.setLineBackground(currentNumberLines, 1, grey);
		    	  // styleRange.start = styleRange.length;
		  	       //styleRange.length = styleRange.length;
		  	       // styleRange.foreground = blue;
		  	      // styleRange.font = font_Welcome;
		  	       //styledText.setStyleRange(styleRange);
		  	       updateCurrentTextPosition(xx.get_date().length()+1);
			}
			
			   styledText.append("***************************************************************************\n");
			   updatecurrentNumberLines(1);
			   
			 // PART-OF-CODE
			   styledText.append("\nPROBLEM IN : " + xx.get_part_of_code() + "\n");
			   updatecurrentNumberLines(2);
			
			   //styledText.setLineBackground(currentNumberLines, 1, grey);
	    	 //  styleRange.start = currentTextPosition;
	  	      // styleRange.length = xx.get_part_of_code().length();
	  	       // styleRange.foreground = blue;
	  	       //styleRange.font = font_Welcome;
	  	      // styledText.setStyleRange(styleRange);
	  	       updateCurrentTextPosition(xx.get_part_of_code().length()+2);
	  	       
	  	     // TYPE
			   styledText.append("\nECLIPSE TYPE : " + xx.get_type() + "\n");
			   updatecurrentNumberLines(2);
			
			   //styledText.setLineBackground(currentNumberLines, 1, grey);
	    	  // styleRange.start = currentTextPosition;
	  	      // styleRange.length = xx.get_type().length();
	  	       // styleRange.foreground = blue;
	  	      // styleRange.font = font_Welcome;
	  	      // styledText.setStyleRange(styleRange);
	  	       updateCurrentTextPosition(xx.get_part_of_code().length()+2);   
	  	       
	  	       
	  	       if(xx.get_type().equals("method")){
	  	    	   
	  	    	 // LOCATION
				   styledText.append("\nLOCATION -> " + "Project : " + xx.get_a_project() + " Package : " + xx.get_a_package() + 
						             " Class : " + xx.get_source() + "\n");
				   updatecurrentNumberLines(2);
				   
				   //styledText.setLineBackground(currentNumberLines, 1, grey);
		    	//   styleRange.start = currentTextPosition;
		  	      // styleRange.length = xx.get_type().length();
		  	       // styleRange.foreground = blue;
		  	      // styleRange.font = font_Welcome;
		  	     //  styledText.setStyleRange(styleRange);
		  	       updateCurrentTextPosition(xx.get_part_of_code().length()+2); 
	  	      
	  	       }else if(xx.get_type().equals("class")){
	  	    	 // LOCATION
				   styledText.append("\nLOCATION -> " + "Project : " + xx.get_a_project() + " Package : " + xx.get_a_package() + "\n");
				   updatecurrentNumberLines(2);
				   
				   //styledText.setLineBackground(currentNumberLines, 1, grey);
		    	//   styleRange.start = currentTextPosition;
		  	     //  styleRange.length = xx.get_type().length();
		  	       // styleRange.foreground = blue;
		  	     //  styleRange.font = font_Welcome;
		  	     //  styledText.setStyleRange(styleRange);
		  	       updateCurrentTextPosition(xx.get_part_of_code().length()+2); 
	  	      
	  	       }else if(xx.get_type().equals("package") || xx.get_type().equals("project")){
	  	    	// LOCATION
				   styledText.append("\nLOCATION -> " + "Project : " + xx.get_a_project() + "\n");
				   updatecurrentNumberLines(2);
				   
				   //styledText.setLineBackground(currentNumberLines, 1, grey);
		    	 //  styleRange.start = currentTextPosition;
		  	     //  styleRange.length = xx.get_type().length();
		  	       // styleRange.foreground = blue;
		  	     //  styleRange.font = font_Welcome;
		  	     //  styledText.setStyleRange(styleRange);
		  	       updateCurrentTextPosition(xx.get_part_of_code().length()+2);	   
	  	       }
	  	       
	  	       
	  	     // SOLUTION
			   styledText.append("\nISSUES : " + "\n\n");
			   updatecurrentNumberLines(3);
			   updateCurrentTextPosition(12);  
			   
	  	       for(String yy : xx.get_solutions()){
	  	    	    styledText.append(" - " + yy + "\n\n");
				   //styledText.setLineBackground(currentNumberLines, 1, grey);
		    	//   styleRange.start = currentTextPosition;
		  	     //  styleRange.length = yy.length();
		  	       // styleRange.foreground = blue;
		  	    //   styleRange.font = font_Welcome;
		  	     //  styledText.setStyleRange(styleRange);
		  	       updateCurrentTextPosition(yy.length()+2);   	 
	  	        }
	  	       
			
	  	       // NEXT
	  	       styledText.append("\n");
	  	       styledText.append("***************************************************************************\n");
	  	       updatecurrentNumberLines(3);
	  	       updateCurrentTextPosition(57 + 3); 
			
		}
		
		   //styleRange.start = 0;
	       //styleRange.length = styleRange.length;
	       // styleRange.foreground = blue;
	       //styleRange.font = font_Welcome;
	       //styledText.setStyleRange(styleRange);
         
	       
		
		// reset the flag (open/close window)
		flag_date = 0;
		
		// while the code problems window is not disposed
		display = shell.getDisplay();//getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		//shell.dispose();
		//display.dispose();
		
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() { // function createContents
		shell = new Shell(getParent(), SWT.DIALOG_TRIM);
		shell.setBackground(SWTResourceManager.getColor(245, 255, 250));
		shell.setSize(800, 800);
		shell.setText(getText());
		
		styledText = new StyledText(shell, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		styledText.setFont(SWTResourceManager.getFont("Mongolian Baiti", 14, SWT.NORMAL));
		styledText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		styledText.setEditable(false);
		styledText.setBounds(10, 44, 774, 694);
		
		Label lblCodeQualityProblems = new Label(shell, SWT.NONE);
		lblCodeQualityProblems.setBackground(SWTResourceManager.getColor(245, 255, 250));
		lblCodeQualityProblems.setFont(SWTResourceManager.getFont("FrankRuehl", 18, SWT.BOLD));
		lblCodeQualityProblems.setBounds(10, 16, 261, 22);
		lblCodeQualityProblems.setText("Code Quality Problems");
		
		Label lblAcesdV = new Label(shell, SWT.NONE);
		lblAcesdV.setBackground(SWTResourceManager.getColor(245, 255, 250));
		lblAcesdV.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.BOLD));
		lblAcesdV.setBounds(700, 747, 84, 15);
		lblAcesdV.setText("ACE4SD v1.0");
		
	} // end of the function createContents
	
	
	// Update currentTextPosition
	private void updateCurrentTextPosition(int value) {
		this.currentTextPosition = this.currentTextPosition + value;
	}

	//Update currentNumberLines
	private void updatecurrentNumberLines(int value) {
		this.currentNumberLines = this.currentNumberLines + value;
	}
} // End of the class dialogPA
