package ace4sd.views;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
//import org.jdom2.input.SAXBuilder;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;

import ace4sd.Activator;
import ace4sd.dialogPA;
import ace4sd.socketUDP_Informations;
import ace4sd.socketUDP_PAMessages;


// This class describes the button in the "informations TAB" view to open the PA window 
public class speakPA extends Action implements IWorkbenchAction{

private static final String ID = "ace4sd.views.SpeakPA";

private static String devLogin;
private static ImageDescriptor image_enable = 
Activator.getImageDescriptor("icons/voice.png"); //flag_green.png");
private static ImageDescriptor image_disable = 
Activator.getImageDescriptor("icons/stop.png");  
final static int taille = 64000; 
public byte buffer[];


public speakPA(String devLogin){
//setId(ID);

this.devLogin = devLogin;

}

public void run(){

	if(this.getImageDescriptor().equals(image_enable)){
		this.setImageDescriptor(image_disable);
		//enable speech recognition
		try {
			sendDatagram("turn_on");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}else{
		this.setImageDescriptor(image_enable);
		
		try {
			sendDatagram("turn_off");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}


public void sendDatagram(String msg) throws IOException{ // send datagram
	  
	InetAddress serveur = InetAddress.getByName("localhost");
	buffer = new byte[taille];
	
    int length = msg.length(); 
    buffer = msg.getBytes("UTF8"); 
    DatagramPacket dataSent = new DatagramPacket(buffer, length, serveur, 9001); // C# - MBA voice (listening)
    DatagramSocket socket = new DatagramSocket(); 
    
    socket.send(dataSent); 
    
    System.out.println("Message sent to MBA Voice: " + msg);
    
	
} // end of send datagram


public void dispose(){
	
}


} // End of the class SpeakPA