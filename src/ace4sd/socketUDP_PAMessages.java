package ace4sd;

import java.io.*; 
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/*import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;*/

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;

// Receives and sends the sockets of the "PA WINDOW" in Eclipse
public class socketUDP_PAMessages implements Runnable{
	
public String message_json;	
public String raw_input;

final static int taille = 64000; 
public byte buffer[];
//String buffer;
public static Shell shell;
public dialogPA window;
public static Text text;

public static DatagramSocket socket;

private static String devIP;
private static int paWindowPort;
private static String omasIP;
private static int omasPort;
private static String pathName;
private static String jsonPAWindowFile;
public static WebSocket ws;
private static String devLogin;


/**
 * The echo server on websocket.org.
 */
private static final String SERVER = "ws://localhost/developer";

/**
 * The timeout value in milliseconds for socket connection.
 */
private static final int TIMEOUT = 5000;

//private static final String channel = "JOHN-WIN";
//private static final String channel_name;// = new String(devLogin + "-WIN");


/**
 * constructor for the sendMessage().
 */
public socketUDP_PAMessages(String devLogin, String message_json, Shell shell, Text text, String raw_input){
	this.devLogin = devLogin;
	this.message_json = message_json;
	this.text = text;
	this.shell = shell;
	this.raw_input = raw_input;	
}


/**
 * constructor for the receiveMessage().
 */
public socketUDP_PAMessages(String devLogin, Shell shell, Text text,  String devIP, int paWindowPort, String omasIP, int omasPort, String pathName){ 
	this.devLogin = devLogin;
	this.shell = shell;
	this.text = text;
	this.devIP = devIP;
	this.paWindowPort = paWindowPort;
	this.omasIP = omasIP;
	this.omasPort = omasPort;
	this.pathName = pathName;

// Create the dir to manipulate json format
  // jsonPAWindowFile = new String(this.pathName+"\\JsonPAWindow\\");

// Create the folder for the metrics
  // new File(jsonPAWindowFile).mkdirs();
   
// PA welcome message
   /*eclipse_pa_window_display("\n" + getDateTime() + "   PA :   "+ "Hello, I'm Alice! What do you need?");
   texttoSpeech("Hello, I'm Alice! What do you need?");*/

   
// activate speech recognition   
   
 
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
   
   
}

public socketUDP_PAMessages(){}


/**
 * Send message to OMAS.
 */
public void sendMessage() throws IOException{ 
	  
	//InetAddress serveur = InetAddress.getByName(omasIP);
	//buffer = new byte[taille];
	
	
    /*int length = message_socket.length(); 
    buffer = message_socket.getBytes("UTF8"); 
    DatagramPacket dataSent = new DatagramPacket(buffer, length, serveur, omasPort); // 40001 
    DatagramSocket socket = new DatagramSocket();*/
    
    ws.sendText(message_json);
    
    
    //socket.send(dataSent); 
    
    System.out.println("Message sent to PA (OMAS): " + message_json);
    
    
    String sx = raw_input.replaceAll("[\n\r]", "");
    eclipse_pa_window_display("\n" + getDateTime() + "   Developer :   " + sx);
	
}


/**
 * UDP/IP - ReceiveDatagram (Deprecated).
 */
public void receiveDatagram() throws Exception{	
	
	 socket = new DatagramSocket(paWindowPort); // 50620
 
     while(true){

      buffer = new byte[taille];
      DatagramPacket data = new DatagramPacket(buffer, buffer.length); 
      socket.receive(data); 
      System.out.println("Received data Address: " + data.getAddress()); 
      
      String Received_Datagram = new String(data.getData());
      
      System.out.println("Mensagem recebida PA:" + Received_Datagram);
       	
        Received_Datagram = Received_Datagram.trim();
      
        FileOutputStream fop = null;
		File file;

		try {

			file = new File(jsonPAWindowFile + "json-eclipse-pa-message.json");
			fop = new FileOutputStream(file);

			// if file exists, delete and create a new one
			if(file.exists()){
				file.delete();
				file.createNewFile();
			}
			
			// get the content in bytes
			byte[] contentInBytes = Received_Datagram.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
      
	    // JSONParser parser = new JSONParser();
         System.out.println("A1");
    
     // try{
         // Object obj = parser.parse(new FileReader(jsonPAWindowFile + "json-eclipse-pa-message.json"));
         // JSONObject jsonObject = (JSONObject) obj;
         // String message = (String) jsonObject.get("eclipse-pa-window");
         // System.out.println("Message AY = " + message);    
    
          //eclipse_pa_window_display("\n" + getDateTime() + "   PA :   "+ message);
          
          //texttoSpeech(message);
          
        // }catch(ParseException pe){
 	     // System.out.println(pe);
         //}
     
   } 
    
} // end of receive datagram


private static void eclipse_pa_window_display(String textDisplay){
	
	shell.getDisplay().asyncExec(new Runnable() {
  	    public void run() {
  	
  	    	text.append(textDisplay);
  	        
  	    }
  	});	
	
}

/**
 * Get the current time of the system.
 */
private static String getDateTime() { 
	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	Date date = new Date(); 
	return dateFormat.format(date); 
}


/**
 * Text to speech.
 */
private static void texttoSpeech(String message) {
	try {
		   Runtime.getRuntime().exec("wscript C:/TTS_Windows.vbs " + message);
		   }
		   catch( IOException e ) {
		      System.out.println(e);
		      System.exit(0);
		   }
}


/**
 * Connect to the server.
 */
private static WebSocket connect() throws IOException, WebSocketException
{
	
	String channel_name = new String(devLogin + "-WIN");
	
    return new WebSocketFactory()
        .setConnectionTimeout(TIMEOUT)
        .createSocket(SERVER)
        .addListener(new WebSocketAdapter() {
            // A text message arrived from the server.
            public void onTextMessage(WebSocket websocket, String message) {
                System.out.println("Message received from PA (OMAS): " + message);
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
        .addProtocol("JOHN-WIN")
        //.addHeader("user-agent", channel_name) // messages from the PA window
        .connect();
}


/**
* Handle the messages received from OMAS
 * @throws Exception 
 * @throws FileNotFoundException 
*/
private static void receiveMessage(String msg) throws FileNotFoundException, Exception{
   	
    msg = msg.trim();
    System.out.println("Msg received from OMAS = " + msg);
  
   // System.out.println("ASADNSF: " +msg.substring(0, 1));
    
    //if(msg.substring(0, 1).trim().equals("{")){
    
	  JSONObject results = new JSONObject(msg);
      JSONArray results_array = results.getJSONArray("pa-window-messages");
    		 
      // print the messages received
      for(int i = 0; i < results_array.length(); i++){
    	  String message = results_array.getString(i);
    	
          System.out.println("Message AY = " + message);    

          eclipse_pa_window_display("\n" + getDateTime() + "   PA :   "+ message);
      
          texttoSpeech(message);
          Thread.sleep(2000);
      }
      
      
   // }else{
    	
    	/*System.out.println("Message AY = " + msg);    

        eclipse_pa_window_display("\n" + getDateTime() + "   PA :   "+ msg);
    
        texttoSpeech(msg);
        Thread.sleep(2000);
    
    }*/
       
}


/**
 * Wrap the standard input with BufferedReader.
 */
private static BufferedReader getInput() throws IOException
{
    return new BufferedReader(new InputStreamReader(System.in));
}


/**
 * Run the thread.
 */
public void run(){
	
	// "Server" receive data
	try {
		receiveDatagram();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
}


} // End of the class socketUDP_PAMessages
