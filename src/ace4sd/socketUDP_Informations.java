package ace4sd;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*; 
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;

import ace4sd.Activator;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.lang.reflect.Type;

// Receives and sends the sockets of the "TAB OF INFORMATIONS" in Eclipse
public class socketUDP_Informations implements Runnable{
	
public String message_socket;	

final static int taille = 64000; 
byte buffer[];
public Label label;
public Shell shell;
public Text text;
public StyledText styledText;
public StyledText styledTextInput;
public Button button;
public Color blue;

// This keeps the current text position in the TAB
private int currentTextPosition = 0;

//This keeps the current number of lines in the TAB
private int currentNumberLines = 0;

private static String devLogin;
private static String pathName;
private static String jsonTabFile;
private static ArrayList<listWindows> code_problems_windows;
private static Hashtable line_window_ID;
private static String snapshot_ID_flag; // used to check duplicity in UDP datagram (check the snapshot_ID between 2 packets) 
public static WebSocket ws;
private static int cSharpPort = 9000; // receive text resulted from speech-to-text (C#)

private static String pathCSharp; //= "C:\\ACE4SD\\Speech\\Release\\"; // "C:\\AllegroCL9.0.bkp\\"
private static String communication_channel;

/**
 * The echo server on websocket.org.
 */
private static String SERVER; //"ws://" + localhost + "/developer";

/**
 * The timeout value in milliseconds for socket connection.
 */
private static final int TIMEOUT = 5000;




public socketUDP_Informations(String message_socket){
	this.message_socket = message_socket;
}

public socketUDP_Informations(String devLogin, Shell shell, StyledText styledText, StyledText styledTextInput, Button button, String server, String pathName) throws IOException{
	//this.display = display;
	this.devLogin = devLogin;
	this.shell = shell;
	//this.text = text;
	this.styledText = styledText;
	this.styledTextInput = styledTextInput;
	this.button = button;
	this.pathName = pathName;
	communication_channel = devLogin + "-TAB";
	pathCSharp = pathName + "Speech\\Release\\";
	SERVER = "ws://" + server + "/developer";
	
	System.out.println("communication_channel = " + communication_channel);
	System.out.println("pathCSharp = " + pathCSharp);
	System.out.println("SERVER = " + SERVER);
	
	// ** Vocal interface **
	
	  // First, test if the C# application "MBA_Voice.exe" is already running
	  String line;
	  String pidInfo ="";

	  Process p = null;
	  

		p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");

	    BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));

		while ((line = input.readLine()) != null){
		    pidInfo+=line; 
		  }

		input.close();

	    // if it is not running, then start it
	    if(!pidInfo.contains("MBA_Voice.exe")){
		  //Runtime.getRuntime().exec("cmd /c start /B " + pathCSharp + "MBA_Voice.exe");
	    	
	    	Runtime.getRuntime().exec("cmd /c start /B " + pathCSharp + "MBA_Voice.exe");
	    	
	    	
		  System.out.println("MBA_Voice.exe has been started!");
		  
		  /*try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	    }
	
	  
	  
	//System.out.println("OMAS IP = " + omasIP + " OMAS PORT = " + omasPort);
	// Welcome message
	 //eclipse_tab_display("nil", "nil", "PA", null);
	 eclipse_tab_display("                             ACE4SD - An Advanced Environment for Collaborative Software Development !", "nil", "Welcome_Message", null);
	 
	 //eclipse_tab_display("nil", "nil", "NewLine");
	 
	 eclipse_tab_display("nil", "nil", "PA", null);
	 /*eclipse_tab_display("Welcome to ACE4SD! I am your personal assistant! Here you will receive messages and reports.", "nil", "Welcome_Message2", null);
     texttoSpeech("Welcome! I am your personal assistant! Here you will receive messages and reports.");
	 
	 eclipse_tab_display("nil", "nil", "PA", null);
	 eclipse_tab_display("We can talk to each other by clicking in the button on the right (ballons)", "nil", "Welcome_Message2", null); 
	
	 //giving some time to the next phrase 
	 try {
		    Thread.sleep(1000);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	 texttoSpeech("We can talk to each other by clicking in the button on the right, that is, conversation ballons");
	 
	 */
	 
	 /********************************************************/   
		
		// Connect to the Websockets server (lisp).
     try {
			ws = connect();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WebSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
     
     
     styledTextInput.addListener(SWT.Traverse, new Listener()
	    {
	        @Override
	        public void handleEvent(Event event)
	        {
	            if(event.detail == SWT.TRAVERSE_RETURN)
	            {
	                //System.out.println("Enter pressed");
	                button.notifyListeners(SWT.Selection, new Event());
	            }
	        }
	    });
     
     
     // Add a listener to the button
     button.addListener(SWT.Selection, new Listener(){
         @Override
         public void handleEvent(Event event)
         {
        	String user_input_text = new String(styledTextInput.getText().trim()); 
        	String user_input_JSON = new String("{\"message-name\" : \"pa-message\", \"action\" : \"user-win-conversation\", \"to\" : \"" + devLogin + "\"," + "\"client-name\" : \"" + communication_channel + "\"," + "\"user-input\" : \"" + user_input_text + "\"}");
 	 	    styledTextInput.setText("");
 	 	
 	 	    // Print the text in the tab
		    eclipse_tab_display("nil", "nil", "User", null);
	 	   	eclipse_tab_display(user_input_text, "nil", "Welcome_Message2", null); 
 	 	    
 	 	    styledTextInput.setFocus();
        	
        	System.out.println("user_input_JSON_StyledTextInput = " + user_input_JSON);
 	 	    try {
 	 	    	
 	 	    	// Send a message to the PA
				sendMessage(user_input_JSON);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
     });
     
	 
	 code_problems_windows = new ArrayList<listWindows>();
	 line_window_ID = new Hashtable();
	 snapshot_ID_flag = new String("");
	 
	// Create the dir to manipulate json format
	jsonTabFile = new String(this.pathName+"\\JsonTab\\");
	// Create the folder for the metrics
	new File(jsonTabFile).mkdirs();
	
	
	  shell.getDisplay().asyncExec(new Runnable() {
     	    public void run() { 
    
      styledText.addListener(SWT.MouseDown, event -> {
  	  
  	 // System.out.println("Bonjour2");
  	  // It is up to the application to determine when and how a link should be activated.
			// In this snippet links are activated on mouse down when the control key is held down
			//if ((event.stateMask & SWT.MOD1) != 0) {
				try {
					int offset = styledText.getOffsetAtLocation(new Point (event.x, event.y));
					StyleRange style1 = styledText.getStyleRangeAtOffset(offset);
					
					if ((style1 != null) && (style1.underline) && (style1.underlineStyle == SWT.UNDERLINE_LINK)) {
						
						System.out.println("Click on a Link");
						
					    //dialogProblemsThread abc = new dialogProblemsThread(shell, code_problemsList);
						
						System.out.println("Offset = " + offset);
						//System.out.println("OKOKOK = " + styledText.getText(offset, offset+link1.length()).toString());
						int lineNo = styledText.getLineAtOffset(offset);
						System.out.println("Line number = " + lineNo);
						System.out.println("Line ours = " + currentNumberLines);
						System.out.println("Hashtable = " + line_window_ID.get(lineNo));
						
						
						for(listWindows xx : code_problems_windows){
							System.out.println("window_ID = " + xx.get_ID());
							if((line_window_ID.get(lineNo).toString().equals(xx.get_ID()))){
			                        //if(window_n_times_flag == 0){
							     	 
							         System.out.println("window_ID__Inside_Loop = " + xx.get_ID());
								     
								     dialogProblemsThread abcd = new dialogProblemsThread (xx);
								     
								     Thread thread_dialogProblems = new Thread(abcd);
						         	
								     // start the threads
								     thread_dialogProblems.start();
								     
								    // xx.get_dialog_problems().open();
			                        // System.out.println("???????");
			                       // }
			                         break;
		   						}
						}
					}
				} catch (IllegalArgumentException e) {
					// no character under event.x, event.y
				}
			//} //mod
		});
     	  }
 	    });
	
	  // Test
	  //sendMessage("{\"message-name\" : \"pa-message\", \"action\" : \"user-win-conversation\", \"to\" : \"" + "jcook" + "\"," + "\"client-name\" : \"" + "JOHN-TAB" + "\"," + "\"user-input\" : \"" + "Give to me the contact of" + "\"}");
}


/**
 * Send message to OMAS (Websockets server)
 */
public void sendMessage(String msg) throws IOException{
	  
    //ws.sendText(message_socket);
   
	ws.sendText(msg);
    System.out.println("Message sent to PA (OMAS): " + msg);
	
}


/**
 * UDP/IP - ReceiveDatagram.
 */
/* This function receives the speech-to-text (user) from C#
 * */
public void receiveDatagram() throws Exception{	

	DatagramSocket socket = new DatagramSocket(cSharpPort); 
	int flag_datagram_duplicate = 0;
	int flag_duplicate = 0;
	
    while(true){ 
     
      buffer = new byte[taille];
      DatagramPacket data = new DatagramPacket(buffer, buffer.length); 
      socket.receive(data); 
      String Received_Datagram = new String(data.getData());
      flag_duplicate = 0;
      
      System.out.println("\nReceived data Address: " + data.getAddress()); 
      System.out.println("\nMensagem recebida TAB:" + Received_Datagram);
      
      // Convert the JSON of the received datagram to a format to be displayed to the user
  	    
		    Received_Datagram = Received_Datagram.trim();
		    System.out.println("\nSpeech-to-text from C#:" + Received_Datagram);
		    
		    // Print the text in the tab
		    eclipse_tab_display("nil", "nil", "User", null);
	 	   	eclipse_tab_display(Received_Datagram, "nil", "Welcome_Message2", null); 
		    
		    
		    
		    // Takes the text (resulted from the user's speech) and send to the Websockets server
	 	    String user_input_JSON = new String("{\"message-name\" : \"pa-message\", \"action\" : \"user-win-conversation\", \"to\" : \"" + devLogin + "\"," + "\"client-name\" : \"" + communication_channel + "\"," + "\"user-input\" : \"" + Received_Datagram + "\"}");
	 	    System.out.println("user_input_JSON = " + user_input_JSON);
	 	    sendMessage(user_input_JSON);
		    
	       
	 	    
	 	    
	     /*    JSONParser parser = new JSONParser();
	         System.out.println("A1");
	           
	      try{  
	          Object obj = parser.parse(new FileReader(jsonTabFile + "json-eclipse-tab.json"));
	          JSONObject jsonObject = (JSONObject) obj;          
	          
	          // Extract the recommendations
	          JSONArray general_array = (JSONArray) jsonObject.get("code-problems");
	          Iterator i = general_array.iterator();

	       // String recommendations = new String(getDateTime() + "   PA: " + "############################### QUALITY RECOMMENDATIONS ###############################\n\n");  
	        
	         
	    //    eclipse_tab_display(recommendations, "BLACK");
	         
	         // "Clear" the string for the next time
	    	// recommendations = "";
	          codeProblems code_problemsTemp = new codeProblems();
	          
	          while (i.hasNext()) {
	        
	           JSONObject temp = (JSONObject) i.next();
	           //System.out.println("PoC = " + temp.get("part-of-code").toString());
	           
	           if(snapshot_ID_flag.equals(temp.get("ID").toString())){
	        	   flag_datagram_duplicate = 1;
	        	   break;
	           }
	           
	           if(flag_duplicate == 0){
	        	  snapshot_ID_flag = temp.get("ID").toString();
	        	  flag_duplicate = 1;
	           }
	           
	           
	          // System.out.println("IDDDSDS = " + snapshot_ID_flag);
	           if(temp.get("type").toString().equals("method") || temp.get("type").toString().equals("class")){
	               code_problemsTemp = new codeProblems(temp.get("part-of-code").toString(), temp.get("type").toString(), 
                              temp.get("project").toString(), temp.get("package").toString(), temp.get("source").toString(), 
                              temp.get("login").toString(), temp.get("developer").toString(), 
                              temp.get("ID").toString(), temp.get("date").toString());   
	           
	           }else if (temp.get("type").toString().equals("package")){
	        	   code_problemsTemp = new codeProblems(temp.get("part-of-code").toString(), temp.get("type").toString(), 
                              temp.get("project").toString(), temp.get("package").toString(), 
                              temp.get("login").toString(), temp.get("developer").toString(), 
                              temp.get("ID").toString(), temp.get("date").toString()); 
	        	   
	           }else if (temp.get("type").toString().equals("project")){
	        	   code_problemsTemp = new codeProblems(temp.get("part-of-code").toString(), temp.get("type").toString(), 
                              temp.get("project").toString(), temp.get("login").toString(), temp.get("developer").toString(), 
                              temp.get("ID").toString(), temp.get("date").toString()); 
	           }
	            
	              JSONArray solution_array = (JSONArray) temp.get("solution");
		          Iterator i2 = solution_array.iterator();

	              while(i2.hasNext()){
	            	  ArrayList<String> solutions = code_problemsTemp.get_solutions();
	            	  solutions.add(i2.next().toString());
	              }
	           
	          code_problemsList.add(code_problemsTemp);
	          
	          }
	          
	          if(flag_datagram_duplicate == 0){
	          
	          listWindows list_windows = new listWindows();
	          
	          shell.getDisplay().asyncExec(new Runnable() {
	       	    public void run() {
	       	      // create a new window to show the quality problems
	  	          dialogProblems dialog_problem_window = new dialogProblems(shell, code_problemsList);
	  	          String window_ID = "Report@" + getDateTime();
	  	      
	  	          listWindows list_windows = new listWindows(window_ID, dialog_problem_window);
	  	          // put it in an array of windows
		          code_problems_windows.add(list_windows);
		          
		          // code_problems_windows.get(0).get_dialog_problems().open();
		          
		          // go to print the code problems in a new window
		         eclipse_tab_display("nil", "nil", "PA", null);
		     	 eclipse_tab_display("nil", "nil", "Code_Problems", window_ID); 
		     	 
	       	     }
	       	    });
	          
	          
	        
	          
	          }else{flag_datagram_duplicate = 0;}
	       }catch(ParseException pe){
	 	      System.out.println(pe);
	       }   */
  
    }
    
} // end of receive datagram

private void eclipse_tab_display(String textDisplay, String color, String pattern, String window_ID){
	
	
	shell.getDisplay().asyncExec(new Runnable() {
 	    public void run() {
 	    	
 	    	//styledText.setText(textDisplay);
 	    	// Color selectedColor = new Color(shell.getDisplay(), 0, 0, 0); // default is black
 	    	Color black = shell.getDisplay().getSystemColor(SWT.COLOR_BLACK);
 	    	Color white = shell.getDisplay().getSystemColor(SWT.COLOR_WHITE);
 	      	/*Color blue = shell.getDisplay().getSystemColor(SWT.COLOR_BLUE);*/
 	   	    Color red = shell.getDisplay().getSystemColor(SWT.COLOR_RED);
 	      	blue = shell.getDisplay().getSystemColor(SWT.COLOR_BLUE);
 	      	Color grey = new Color(shell.getDisplay(), 212, 208, 200);
 	        Font font_PA = new Font(shell.getDisplay(), "Times New Roman", 12, SWT.BOLD);
 	        Font font_Welcome = new Font(shell.getDisplay(), "Arial Black", 14, SWT.BOLD);
 	        Font font_Welcome1 = new Font(shell.getDisplay(), "FrankRuehl", 18, SWT.BOLD);
 	        Font font_Welcome2 = new Font(shell.getDisplay(), "Arial", 11, SWT.BOLD);
 	      	//Font font = new Font(shell.getDisplay(), "Times New Roman", 15, SWT.BOLD);
 	      	StyleRange styleRange = new StyleRange();
 	      	
 	      	//String textDisplay_aux = new String(textDisplay);
 	      	//int n_textDisplay_aux = textDisplay_aux.length();
 	      	
 	      	
 	      	System.out.println("Pattern = " + pattern);
 	      	
 	      	switch (pattern){
 	      	
 	      	case "PA" :
 	      	       styledText.append("\n" + getDateTime() + "   PA :   ");
 	      	       updatecurrentNumberLines(1);
 	      	       
 	 	    	   //styledText.setLineBackground(currentNumberLines, 1, white);
 	 	    	  // styledText.setFont(font_PA);
 	 	    	   /*styleRange.start = currentTextPosition;
  		  	       styleRange.length = 18;
  		  	       styleRange.font = font_PA;
  		  	       styledText.setStyleRange(styleRange);
  		  	       updateCurrentTextPosition(19);*/
 	      	         break;
 	      	         
 	      	case "User" :
	      	       styledText.append("\n" + getDateTime() + "   User :   ");
	      	       //updatecurrentNumberLines(1);
	 	    	   //styledText.setLineBackground(currentNumberLines, 1, white);
	 	    	  // styledText.setFont(font_PA);
	 	    	   /*styleRange.start = currentTextPosition;
		  	       styleRange.length = 18;
		  	       styleRange.font = font_PA;
		  	       styledText.setStyleRange(styleRange);
		  	       updateCurrentTextPosition(19);*/
	      	         break;         
 	      	         
 	      	case "Welcome_Message" :       
 	      	       styledText.append(textDisplay + "\n");
 	      	       updatecurrentNumberLines(1);
	 	    	  // styledText.setLineBackground(currentNumberLines, 1, grey);
	 	    	   styleRange.start = currentTextPosition;
		  	       styleRange.length = textDisplay.length();
		  	       // styleRange.foreground = blue;
		  	       styleRange.font = font_Welcome;
		  	       styledText.setStyleRange(styleRange);
		  	       updateCurrentTextPosition(textDisplay.length()+1);
		  	       
		  	       break;
		  	       
 	      	case "Welcome_Message2" :       
	      	       styledText.append(textDisplay + "\n");
	      	       //updatecurrentNumberLines(1);
	      	       
	      	       /*styleRange.start = currentTextPosition;
	   	           styleRange.length = textDisplay.length();
	   	           styleRange.font = font_Welcome2;
	   	           styledText.setStyleRange(styleRange);
	   	        
	   	           updateCurrentTextPosition(textDisplay.length()+1);*/
	      	      // styledText.setLineBackground(currentNumberLines, 1, grey);
	 	    	   //styledText.setFont(font_Welcome2);
	 	    	   /*styleRange.start = currentTextPosition;
		  	       styleRange.length = textDisplay.length();
		  	       // styleRange.foreground = blue;
		  	       styleRange.font = font_Welcome2;
		  	       styledText.setStyleRange(styleRange);
		  	       updateCurrentTextPosition(textDisplay.length()+1); */ // +1 <=> "\n" character     
		  	       break;
		  
 	      	case "Code_Problems" :
 	      		
 	      		   String string = "A new report has just arrived! There are some quality issues in your code, please click in the link for details: ";
 	      		   String string2 = "                    " + window_ID;
 	 			   //final StyledText styledText = new StyledText (shell, SWT.MULTI | SWT.BORDER);
 	      		   String link1 = window_ID;
 	      		   
 	      		   styledText.append(string + "\n");
 	      		   updatecurrentNumberLines(1);
 	 			   styleRange.start = currentTextPosition;
		  	       styleRange.length = string.length();
		  	       // styleRange.foreground = blue;
		  	      // styledText.setStyleRange(styleRange);
		  	       styleRange.font = font_Welcome2;
		  	      // styledText.setLineBackground(currentNumberLines, 1, grey);
		  	       styledText.setStyleRange(styleRange);
		  	       updateCurrentTextPosition(string.length()+1);
		  	       
		  	       styledText.append(string2 + "\n");
		  	       updatecurrentNumberLines(1);
		  	       //styledText.setLineBackground(currentNumberLines, 1, grey);
		  	       styleRange.underline = true;
 	      		   styleRange.underlineStyle = SWT.UNDERLINE_LINK;
 	 			   styleRange.start = currentTextPosition + string2.indexOf(link1);
		  	       styleRange.length = link1.length();
 	 			   styledText.setStyleRange(styleRange);
 	 			   updateCurrentTextPosition(string2.length()+1);
 	 			   line_window_ID.put(currentNumberLines, window_ID);
 	 			   
 	 			   styledText.setTopIndex(styledText.getLineCount() - 1);
 	 			   
 	 			   /*styleRange.start = currentTextPosition+2;
		  	       styleRange.length = string.length()+3;
		  	       // styleRange.foreground = blue;
		  	       styleRange.font = font_Welcome2;
		  	       styledText.setStyleRange(styleRange);
		  	       updateCurrentTextPosition(string.length());*/
 	 			   
 	 			   
 	 			 try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("ace4sd.views.ace4sdView");
					//PlatformUI.getWorkbench().getActiveWorkbenchWindow().setActivePage(arg0);
				} catch (PartInitException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
 	 			   
 	 			 String message = new String("A new report has just arrived! There are some quality issues in your code, please click in the link for details: ");
 	 			 
 	 			 //Speak
 	 			 texttoSpeech(message);
 	 			  
 	      		   break;
		  	       
 	      	case "NewLine" : 
	      	       styledText.append("\n");
	      	       updatecurrentNumberLines(1);
	 	    	  // styledText.setLineBackground(currentNumberLines, 1, white);
	 	    	   // styledText.setFont(font_PA);
	 	    	   //styleRange.start = 0;
		  	       //styleRange.length = 17;
		  	      // styleRange.font = font_PA;
		  	       //styledText.setStyleRange(styleRange);
	      	         break;
		  	
 	      	}
 	      	
 	      	
 	    	//styledText.append("\n" + getDateTime() + "   PA: " + textDisplay + "\n");
 	   	    
 	    	//styledText.setForeground(black);
 	    	
 	    	//styledText.setText(textDisplay);
 	    	
 			
 	      	styledText.setTopIndex(styledText.getLineCount() - 1);
 	      
 			 
	 		  	//styleRange.fontStyle = SWT.BOLD;
	 		  	
	            //styleRange.font = font;
	            //styleRange.foreground = blue;
	            
	          
	            
	            /*System.out.println("COUNT: " + textDisplay.length());
	            styleRange.start = 103;
	 		  	styleRange.length = (textDisplay.length()-103);
	 		  	//styleRange.fontStyle = SWT.BOLD;
	 		  	font = new Font(shell.getDisplay(), "Times New Roman", 13, SWT.BOLD);
	            styleRange.font = font;
	            styleRange.foreground = red;*/
	            
	            
 	    	
 	      /* switch(color){
 	       
 	       case "BLACK":
 	    	    styleRange.start = 0;
 	 		  	styleRange.length = 100;
 	 		  	//styleRange.fontStyle = SWT.BOLD;
 	 		  	Font font = new Font(shell.getDisplay(), "Times New Roman", 15, SWT.BOLD);
 	            styleRange.font = font;
 	            styleRange.foreground = selectedColor;
 	    	   	 
 	    	     break;
 	   	
 		   case "BLUE": 
 			     selectedColor = blue; 
 		         break;
 		         
 		   case "RED": 
 			     selectedColor = red;
 			     
 			    styleRange.start = 0;
 	 		  	styleRange.length = textDisplay.length();
 	 		  	//styleRange.fontStyle = SWT.BOLD;
 	 		  	font = new Font(shell.getDisplay(), "Times New Roman", 15, SWT.BOLD);
 	            styleRange.font = font;
 			     break;
 		
 		   }*/
 	    
 	     	
 			//styleRange.foreground = selectedColor;
 		
 			
 	    	//text.setForeground(color);
 	    	//text.append("\n" + getDateTime() + "   PA: " + textDisplay + "\n");
 	        
 	    }
 	});	
	
}


/**
 * Connect to the server.
 */
private WebSocket connect() throws IOException, WebSocketException
{
	
	//String channel_name = new String(devLogin + "-TAB");
	
    return new WebSocketFactory()
        .setConnectionTimeout(TIMEOUT)
        .createSocket(SERVER)
        .addListener(new WebSocketAdapter() {
            // A text message arrived from the server.
            public void onTextMessage(WebSocket websocket, String message) {
                System.out.println("Eclipse tab - Message received from PA (OMAS): " + message);
                try {
					receiveMessage(message);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        })
        .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
        .addProtocol(communication_channel)//("JOHN-TAB")
        //.addHeader("Sec-WebSocket-Protocol", "JOHN-TAB")
        //.addHeader("user-agent", channel_name) // messages for the Eclipse TAB
        .connect();
}


/**
* Handle the messages received from OMAS
 * @throws Exception 
 * @throws FileNotFoundException 
*/
private void receiveMessage(String msg) throws FileNotFoundException, Exception{
   	
	int flag_datagram_duplicate = 0;
	int flag_duplicate = 0;
	 // array to the code problems that were received
    ArrayList<codeProblems> code_problemsList = new ArrayList<codeProblems>();
	
	
    msg = msg.trim();
    
    System.out.println("TAB -> Msg received from OMAS = " + msg);
  
	  JSONObject results = new JSONObject(msg);
	  String type = results.getString("type");
	  
	  if(type.equals("tab-first")){ // if tab-first
		  JSONArray results_array = results.getJSONArray("eclipse-tab-messages-first");
 	      System.out.println("results_array (size) = " + results_array.length());
 	      
 	          String messageA = null;
 	          //String messageB = null;
 	    	  messageA = results_array.getString(0);
 	    	  //messageB = results_array.getString(1);
 	   	          
 	     eclipse_tab_display(messageA, "nil", "Welcome_Message2", null);
 	     texttoSpeech(messageA);
 	   	 
 	   	 //eclipse_tab_display("nil", "nil", "PA", null);
 	   	 //eclipse_tab_display(messageB, "nil", "Welcome_Message2", null); 
 	   	
 	   	 //giving some time to the next phrase 
 	   	 try {
 	   		    Thread.sleep(5000);
 	   		} catch(InterruptedException ex) {
 	   		    Thread.currentThread().interrupt();
 	   		}
 	   	 //texttoSpeech("Welcome! What Do you need?");
 	   	 
 	    // Test
 		  //sendMessage("{\"message-name\" : \"pa-message\", \"action\" : \"user-win-conversation\", \"to\" : \"" + "jcook" + "\"," + "\"client-name\" : \"" + "JOHN-TAB" + "\"," + "\"user-input\" : \"" + "Give to me the contact of" + "\"}");

 	      
	  }else{ // fim do if tab-first ; else_1
	 	  if(type.equals("tab-recommendations")){
		
	      // Extract the recommendations
		  JSONArray general_array = results.getJSONArray("code-problems");
		  	  //(JSONArray) jsonObject.get("code-problems");
		  codeProblems code_problemsTemp = new codeProblems();
		
		  for(int i = 0; i < general_array.length(); i++){
			  JSONObject temp = general_array.getJSONObject(i);
			  //String message = general_array.getString(i);
			   
		       if(snapshot_ID_flag.equals(temp.get("ID").toString())){
		    	   flag_datagram_duplicate = 1;
		    	   break;
		       }
		       
		       if(flag_duplicate == 0){
		    	  snapshot_ID_flag = temp.get("ID").toString();
		    	  flag_duplicate = 1;
		       }
		       
		       
		      // System.out.println("IDDDSDS = " + snapshot_ID_flag);
		       if(temp.get("type").toString().equals("method") || temp.get("type").toString().equals("class")){
		           code_problemsTemp = new codeProblems(temp.get("part-of-code").toString(), temp.get("type").toString(), 
		                      temp.get("project").toString(), temp.get("package").toString(), temp.get("source").toString(), 
		                      //temp.get("login").toString(), temp.get("developer").toString(), 
		                      temp.get("ID").toString(), temp.get("date").toString());   
		       
		       }else if (temp.get("type").toString().equals("package")){
		    	   code_problemsTemp = new codeProblems(temp.get("part-of-code").toString(), temp.get("type").toString(), 
		                      temp.get("project").toString(), temp.get("package").toString(), 
		                      //temp.get("login").toString(), temp.get("developer").toString(), 
		                      temp.get("ID").toString(), temp.get("date").toString()); 
		    	   
		       }else if (temp.get("type").toString().equals("project")){
		    	   code_problemsTemp = new codeProblems(temp.get("part-of-code").toString(), temp.get("type").toString(), 
		                      temp.get("project").toString(), 
		                      //temp.get("login").toString(), temp.get("developer").toString(), 
		                      temp.get("ID").toString(), temp.get("date").toString()); 
		       }
		        
		          JSONArray solution_array = temp.getJSONArray("solution");
		     
		          for(int j = 0; j < solution_array.length(); j++){
		        	  //JSONObject temp = solution_array.getJSONObject(i);
		        	  String message = solution_array.getString(j);
		        	  ArrayList<String> solutions = code_problemsTemp.get_solutions();
		        	  solutions.add(message);
		          }
		        		           
		      code_problemsList.add(code_problemsTemp);
		 	  
		  }
     
		  if(flag_datagram_duplicate == 0){
		  
		  listWindows list_windows = new listWindows();
		  
		  shell.getDisplay().asyncExec(new Runnable() {
		    public void run() {
		      // create a new window to show the quality problems
		      dialogProblems dialog_problem_window = new dialogProblems(shell, code_problemsList);
		      String window_ID = "Report@" + getDateTime();
		  
		      listWindows list_windows = new listWindows(window_ID, dialog_problem_window);
		      // put it in an array of windows
		      code_problems_windows.add(list_windows);
		      
		      // code_problems_windows.get(0).get_dialog_problems().open();
		      
		      // go to print the code problems in a new window
		     eclipse_tab_display("nil", "nil", "PA", null);
		 	 eclipse_tab_display("nil", "nil", "Code_Problems", window_ID); 
		 	 
		     }
		    });
		  
		  }else{flag_datagram_duplicate = 0;}   
	 		  
		  }else{ // end of the else_2
			  
			  if(type.equals("pa-window")){
				  
				  JSONArray results_array = results.getJSONArray("pa-window-messages");
		 	      System.out.println("results_array (size) = " + results_array.length());
		 	      
		 	      // print the messages received
		 	      for(int i = 0; i < results_array.length(); i++){
		 	    	  String message = results_array.getString(i);
		 	    	
		 	          System.out.println("Message AY = " + message); 
		 	          
		 	          if(message.contains(" <BR> ")){
		 	        	 String[] parts = message.split(" <BR> ");
		 	        	 boolean flag_speak_line = false; // a flag to control the text-to-speech
		 	             
		 	        	 for(int j = 0; j < parts.length; j++){
		 	        		eclipse_tab_display("nil", "nil", "PA", null);
							eclipse_tab_display(parts[j], "nil", "Welcome_Message2", null); 
							
							// to compare the string comming
							String current_part = parts[j];
							current_part = current_part.toLowerCase();
							
							// if it concerns to messages exchanged between developers
							if((current_part.contains("you received a new message from")) || (current_part.contains("message was sent successfully")) ||
									(current_part.contains("sorry"))){
								
								//if(current_part.contains("sorry"))
									//flag_speak_line = true;
									
								// speak only the first utterance: "You received a new message from..."
								// flag_speak_line = false; // if true, then does not speak the message coming from other developers
								 texttoSpeech(parts[j]);
								
								 //giving some time tSSSSSSo the next phrase 
						 	   	 try {
						 	   		    Thread.sleep(4000);
						 	   		} catch(InterruptedException ex) {
						 	   		    Thread.currentThread().interrupt();
						 	   		}
							}else{
							  // speak only the last utterance
							  if((j == (parts.length-1)) && (parts.length > 1) && (flag_speak_line == false)){ //(j == 1){
							    texttoSpeech(parts[j]);
					 	   	 
					 	   	   //giving some time to the next phrase 
					 	   	   try {
					 	   		    Thread.sleep(7000);
					 	   		} catch(InterruptedException ex) {
					 	   		    Thread.currentThread().interrupt();
					 	   		}
					 	   	  }
							}
						 }
		 	          }else{
		 	          
		 	          // Print the text in the tab
				      eclipse_tab_display("nil", "nil", "PA", null);
				      eclipse_tab_display(message, "nil", "Welcome_Message2", null); 
				      
				         texttoSpeech(message);
				 	   	 
				 	   	 //giving some time to the next phrase 
				 	   	 try {
				 	   		    Thread.sleep(7000);
				 	   		} catch(InterruptedException ex) {
				 	   		    Thread.currentThread().interrupt();
				 	   		}
				      
		 	          }
		 	          
		 	          /*texttoSpeech(message);
		 	          Thread.sleep(2000);*/
		 	      }
				  
			  }
			  
		  } // end of the else_2
	 	  
	  } // end of the else_1
      
}


/**
 * Wrap the standard input with BufferedReader.
 */
private static BufferedReader getInput() throws IOException
{
    return new BufferedReader(new InputStreamReader(System.in));
}


//Text to speech
private void texttoSpeech(String message){
	try {
	 Runtime.getRuntime().exec("wscript " + pathName + "/Speech/TTS_Windows.vbs " + message);//("wscript C:/ACE4SD/Speech/TTS_Windows.vbs " + message);
     }catch( IOException e ) {
	   System.out.println(e);
	   System.exit(0);
	 }
}


// pathName + "Speech\\Release\\";


// Get the current time of the system
private String getDateTime() { 
	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	Date date = new Date(); 
	return dateFormat.format(date); 
}

// Update currentTextPosition
private void updateCurrentTextPosition(int value) {
	this.currentTextPosition = this.currentTextPosition + value;
}

//Update currentNumberLines
private void updatecurrentNumberLines(int value) {
	this.currentNumberLines = this.currentNumberLines + value;
}

// Run the thread
public void run(){
	
	// "Server" receive data
	try {
		receiveDatagram();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


} // End of the class socketUDP_Informations
