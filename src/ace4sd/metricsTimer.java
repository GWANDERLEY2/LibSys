package ace4sd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class metricsTimer extends TimerTask {
	
	private IPreferenceStore store;
	private static String paName;	
	private static String devLogin;
	private static String pathName;

	public metricsTimer(){   
		this.store = Activator.getDefault().getPreferenceStore();
		this.paName = new String (store.getString("devName"));
		this.devLogin = new String (store.getString("devLogin"));
	}

	public void run() {
	
	while(true){	
		try {
			// sleep the thread for the defined time
			Thread.sleep(Integer.parseInt(store.getString("metricsPeriod")));
			
			pathName = new String(store.getString("folderData") + "\\Metrics\\");
			// when it wakes, sends the metrics to OMAS
			File folder = new File(pathName);
            
            synchronized(folder){ // if synchronized
              ArrayList <File> files = new ArrayList<File>(Arrays.asList(folder.listFiles()));
              // if the directory is not empty
              if(files.size() > 0){ //if files.length
                
            	  String metrics_in_json = new String();
                 
            	  metrics_in_json = metrics_to_json(new File (pathName + files.get(0).getName().toString()));
                  
                  // if it is not a file of the type "CompilationUnit", then send it to OMAS
                  if(!(metrics_in_json.equals(""))){
                  //send to OMAS - Use "socketUDP_Informations"
                  socketUDP_Informations metrics_message = new socketUDP_Informations(metrics_in_json);
 				 /*try{
 					metrics_message.sendMessage();
 					
 					// delete the file
 		              files.get(0).delete();			
 		
 				 } catch (IOException e1) {
 					// TODO Auto-generated catch block
 					e1.printStackTrace();
 				 }*/
 				
              }
            	  
         	
              } // end of the if files.length
            } // end of the synchronized
            	
    		
		}catch (InterruptedException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
  }
	
	
	public static String metrics_to_json(File inputFile) throws SAXException, IOException, ParserConfigurationException{ // function metrics_to_json

		String metrics_JSON = new String();
			   metrics_JSON = "";
					   
	    try{
		 DocumentBuilderFactory dbFactory 
		 = DocumentBuilderFactory.newInstance();
	    
		 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		 Document doc = dBuilder.parse(inputFile);
		 doc.getDocumentElement().normalize();
		
		 // Root element of the .xml file
		 System.out.println("Root element :" 
		   + doc.getDocumentElement().getNodeName());
				         
	     String file_name = inputFile.getName();
	  
	     // takes the inputFile name without its extension
		 int pos = file_name.lastIndexOf("."); 
				        
		 if (pos > 0){
			file_name = file_name.substring(0, pos);
		 }
				         
		 // Get the root node of the .xml - Usually called "Metric"
		 Element rootNode = doc.getDocumentElement();
							
	  	 // Create JSON - Global parameters - Take the heading of the root "Metric"
							
		// if the metrics are about some project...(not a "compilation unit" file)
		 if((rootNode.getAttributeNode("type").toString()).equals("type=\"Project\"")){ 
								
		 // message-name (OMAS) is the metrics file name
			metrics_JSON = "{\"message-name\" : \"" + file_name + "\", \"action\" : \"EVALUATE-METRICS\", " + "\"PA\" : \"" + paName + "\", " + "\"LOGIN\" : \"" + devLogin + "\", " + "\"project\" : \"" + rootNode.getAttribute("scope") + "\", " + "\"date\" : \"" + rootNode.getAttribute("date") + "\", " + "\"time\" : \"" + rootNode.getAttribute("time") + "\"";
							
			// children of the root node
			NodeList list = rootNode.getChildNodes(); 
							
			// loop for the tag "Metric"
			for(int i = 0; i < list.getLength(); i++){
								
				Node child = list.item(i);
								 
			    if(child instanceof Element){ // if_a1
				  Element node = (Element) child;
				  metrics_JSON = metrics_JSON + ", "; // to the next element
									    
				  // Create JSON - Global parameters - Take the heading of the children "Metric"
							
				  // if the metric is one of the following: WMC, NSL, NORM, NOF, NSF, NOM, NSM, NOC, NOI, MLOC
				  // then we add one more parameter to the JSON file: namely "total"
								
				  // "{\"name\" : \"metric-heading\"  
				  // \"total\" : \"...\"		   -> the total value
				  // \"avg\" : \"...\"		   -> the global average
				  // \"stddev\" : \"...\"		   -> the global standard deviation
				  // \"max\" : \"...\"		   -> the maximum value obtained
									
				 if((node.getAttribute("id").equals("WMC")) || (node.getAttribute("id").equals("NSL")) || 
				    (node.getAttribute("id").equals("NORM")) || (node.getAttribute("id").equals("NOF")) || 
				    (node.getAttribute("id").equals("NSF")) || (node.getAttribute("id").equals("NOM")) || 
				    (node.getAttribute("id").equals("NSM")) || (node.getAttribute("id").equals("NOC")) || 
				    (node.getAttribute("id").equals("NOI")) || (node.getAttribute("id").equals("MLOC"))){ // if parameter "total"
				  
				   // children = tag "Values"
				   NodeList list2 = node.getChildNodes();
									
				   // for tag "Values"
				   for(int j = 0; j < list2.getLength(); j++){ 
										
				     Node child2 = list2.item(j);
										 
					 if(child2 instanceof Element){ // if_a2
					    // a child element to be processed
						Element node2 = (Element) child2;
										
					    // get the heading (tag "Values")
					    metrics_JSON = metrics_JSON + "\"" + node.getAttribute("id") + "\" : [{\"values-per\" : \"" + node2.getAttribute("per") + "\", \"total\" : \"" + node2.getAttribute("total") + "\", \"avg\" : \"" + node2.getAttribute("avg") + "\", \"stddev\" : \"" + node2.getAttribute("stddev") + "\", \"max\" : \"" + node2.getAttribute("max") + "\"}";
									
					    // children = tag "Value"
						NodeList list3 = node2.getChildNodes(); 
										
						// for tag "Value"
						for(int k = 0; k < list3.getLength(); k++){
										 	
						    Node child3 = list3.item(k);
									 
						    if(child3 instanceof Element){ // if_a3
							// a child element to be processed
							  Element node3 = (Element) child3;
											
							  metrics_JSON = metrics_JSON + ", "; // to the next element
							  metrics_JSON = metrics_JSON + "{\"name\" : \"" + node3.getAttribute("name") + "\", \"source\" : \"" + node3.getAttribute("source") + "\", \"package\" : \"" + node3.getAttribute("package") + "\", \"value\" : \"" + node3.getAttribute("value") + "\"}";
							} // end of the if_a3
						 } // end of the for tag "Value"
					   } // end of the if_a2
					 } // end for tag "Values"
									
					// close the JSON metric array
					   metrics_JSON = metrics_JSON + "]";
									
					}else{ // end of if parameter "total" - else_1
								
			         // NOP and TLOC have special format:
					  //\"NOP\" : \"value\"
					  //\"TLOC\" : \"value\"
								
					  // if NOP or TLOC
					  if((node.getAttribute("id").equals("NOP")) || (node.getAttribute("id").equals("TLOC"))){ 
										
						// children = tag "Value"
					    NodeList list2 = node.getChildNodes();
										
					    // for tag "Value"
						for(int j = 0; j < list2.getLength(); j++){ 
											
						   Node child2 = list2.item(j);
											 
						   if(child2 instanceof Element){ // if_a2
						    // a child element to process
							   Element node2 = (Element) child2;
							   metrics_JSON = metrics_JSON + "\"" + node.getAttribute("id") + "\" : \"" + node2.getAttribute("value") + "\""; 
						   } // end of the if_a2
						 } // end of the for tag "Value"
					   }else{ // end of the if NOP or TLOC - else for the other cases
									
						 // Create JSON - Global parameters - Take the heading of the children "Metric"
						
						 // "{\"name\" : \"metric-heading\"    
						// \"avg\" : \"...\"		   -> the global average
						// \"stddev\" : \"...\"		   -> the global standard deviation
				   	    // \"max\" : \"...\"		   -> the maximum value obtained
										
						    // children = tag "Values"
							NodeList list2 = node.getChildNodes(); 
										
							// for tag "Values"
							for(int j = 0; j < list2.getLength(); j++){ 
											
							   Node child2 = list2.item(j);
											 
							   if(child2 instanceof Element){ // if_a2
								
								 // a child element to process
								  Element node2 = (Element) child2;
											
								 // get the heading (tag "Values")
									metrics_JSON = metrics_JSON + "\"" + node.getAttribute("id") + 
									    "\" : [{\"values-per\" : \"" + node2.getAttribute("per") + 
									    "\", \"avg\" : \"" + node2.getAttribute("avg") + "\", \"stddev\" : \"" + 
									     node2.getAttribute("stddev") + "\", \"max\" : \"" + node2.getAttribute("max") + "\"}";
									
									     // children = tag "Value"
									     NodeList list3 = node2.getChildNodes(); 
											
									     // for tag "Value"
										 for(int k = 0; k < list3.getLength(); k++){ 
												 Node child3 = list3.item(k);
												 
												 if(child3 instanceof Element){ // if_a3
												   // a child element to process
												   Element node3 = (Element) child3;
												 
												   // preparing to the next element
												   metrics_JSON = metrics_JSON + ", "; 
												   metrics_JSON = metrics_JSON + "{\"name\" : \"" + node3.getAttribute("name") + 
													 "\", \"source\" : \"" + node3.getAttribute("source") + "\", \"package\" : \"" + 
												     node3.getAttribute("package") + "\", \"value\" : \"" + node3.getAttribute("value") + "\"}";
												 } // end of the if_a3
											} // end of the for tag "Value"
										  } // end of the if_a2
										} // end for tag "Values"
										
										// close the metric's array
										metrics_JSON = metrics_JSON + "]";
										
									} // end of the else for the other cases
								} // end of the else_1
								
							  } // end of the if_a1
							} // end of the loop for the tag "Metric"
							
							// close JSON file
							metrics_JSON = metrics_JSON + "}";
							
						  } // end of the if - metrics file is type "Project", not "CompilationUnit"
							
						 }catch(Exception e){ 
							 e.printStackTrace();
					     }
		 
	    return metrics_JSON;
				      
	} // end of the function metrics_to_json
	
	
}


