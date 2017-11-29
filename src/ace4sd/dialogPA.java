package ace4sd;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;


import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

import java.io.*; 
import java.net.*;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.wb.swt.SWTResourceManager;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;

// Class that controls the dialog window of the PA
public class dialogPA extends Dialog {

	protected Object result;
	public Shell shell;
	private Text text_1;
	public static Text text;
	
	private static String devLogin;
	private static String devIP;
	private static int paWindowPort;
	private static String omasIP;
	private static int omasPort;
	private static String pathName;
	private Label lblAcesdV;
	private Label lblPersonalAssistant;
	private static String communication_channel;
	
	
	
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 * @wbp.parser.constructor
	 */

	
	public dialogPA(Shell parent, int style, String devLogin, String devIP, int paWindowPort, String omasIP, int omasPort, String pathName) {
		super(parent, style);
		setText("ACE4SD - Personal Assistant Dialogue Window");
		
		this.devLogin = devLogin;
		this.devIP = devIP;
		this.paWindowPort = paWindowPort;
		this.omasIP = omasIP;
		this.omasPort = omasPort;
		this.pathName = pathName;
		
		communication_channel = devLogin + "-WIN";
		
		
	}
	
	public dialogPA(Shell parent){
		super(parent, 0);
		setText("ACE4SD - Personal Assistant Dialogue");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		
		Display display = getParent().getDisplay();
		
		// center the window
		Rectangle screenSize = display.getPrimaryMonitor().getBounds();
		shell.setLocation((screenSize.width - shell.getBounds().width) / 2, (screenSize.height - shell.getBounds().height) / 2);
		
		
		shell.open();
		shell.layout();
		
		socketUDP_PAMessages pa_window = new socketUDP_PAMessages(devLogin, shell, text, devIP, paWindowPort, omasIP, omasPort, pathName);
		
		lblAcesdV = new Label(shell, SWT.NONE);
		lblAcesdV.setBackground(SWTResourceManager.getColor(245, 255, 250));
		lblAcesdV.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.BOLD));
		lblAcesdV.setBounds(635, 328, 84, 19);
		lblAcesdV.setText("ACE4SD v1.0");
		
		lblPersonalAssistant = new Label(shell, SWT.NONE);
		lblPersonalAssistant.setBackground(SWTResourceManager.getColor(245, 255, 250));
		lblPersonalAssistant.setFont(SWTResourceManager.getFont("FrankRuehl", 18, SWT.BOLD));
		lblPersonalAssistant.setBounds(10, 17, 257, 23);
		lblPersonalAssistant.setText("Personal Assistant");
		
		// Create a new thread
		Thread thread =  new Thread(pa_window) ;
		// start the thread
		thread.start() ; 
		
		// while the PA window is not disposed
	    display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		// dispose dialog
	    socketUDP_PAMessages.socket.close();
	    socketUDP_PAMessages.ws.disconnect();
	    thread.interrupt(); /*****NEW*****/
		
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() { // function createContents
		shell = new Shell(getParent(), getStyle());
		shell.setBackground(SWTResourceManager.getColor(245, 255, 250));
		shell.setSize(736, 386);
		shell.setText(getText());
		
		text_1 = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		text_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_1.setBounds(10, 281, 631, 23);
		
	

		
		text = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text.setFont(SWTResourceManager.getFont("Mongolian Baiti", 13, SWT.NORMAL));
		text.setBounds(10, 46, 694, 211);
		
		Button btnSend = new Button(shell, SWT.NONE);
		shell.setDefaultButton(btnSend);
		
		// Button event - send the user message to the PA in OMAS
		btnSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) { 
			
				// remove the carriage return of the string
				String s2 = text_1.getText().replaceAll("[\n\r]", "");
				
				
				// put the user input in the JSON format
				String user_input_JSON = new String("{\"message-name\" : \"pa-message\", \"action\" : \"user-win-conversation\", \"to\" : \"" + devLogin + "\"," + "\"client-name\" : \"" + communication_channel + "\"," + "\"user-input\" : \"" + s2 + "\"}");
				String raw_input = new String(text_1.getText());
				
				// send developer's message to OMAS
				 socketUDP_PAMessages user_message = new socketUDP_PAMessages(devLogin, user_input_JSON, shell, text, raw_input);
				 try {
					user_message.sendMessage();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 System.out.println("User message sent to PA: " + text_1.getText());
				 // clear the text_1 box
				 text_1.setText("");
			}
		});
		btnSend.setBounds(647, 279, 57, 25);
		btnSend.setText("SEND");
		
		
		
		text_1.addListener(SWT.Traverse, new Listener()
	    {
	        @Override
	        public void handleEvent(Event event)
	        {
	            if(event.detail == SWT.TRAVERSE_RETURN)
	            {
	                //System.out.println("Enter pressed");
	                btnSend.notifyListeners(SWT.Selection, new Event());
	            }
	        }
	    });
		
	} // end of the function createContents
	
	
} // End of the class dialogPA
