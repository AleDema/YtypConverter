
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.apache.commons.math3.complex.Quaternion;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String s = "CreateObject(GetHashKey(" + hash + "),generator.x-1.9,generator.y+3.0,generator.z+0.38,false,false,false)";
		
		try {
	         File inputFile = new File("apa_test.xml");
	         StringBuilder sb = new StringBuilder();
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	         NodeList nList = doc.getElementsByTagName("Item");
	         System.out.println("----------------------------");
	         int count = 0;
	         int index = 0;
	         
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	        	 Node nNode = nList.item(temp);
	        	 Element eElement = (Element) nNode;
	        	 if (eElement.getAttribute("type").equals("CMloArchetypeDef")){
	        		 NodeList testdio = eElement.getElementsByTagName("name");
	        		 Element name = (Element) testdio.item(0);
	        		 String s = name.getTextContent();
	        		 if (s.equals("apa_V_mp_h_01")){
	        			// nList = (NodeList) nList.item(temp);
	        			 index = temp;
	        			 System.out.println("test");
	        			 System.out.println(index);
	        		 }
	        	 }
	         }
	         
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	        //    System.out.println("\nCurrent Element :" + nNode.getNodeName());
	            
	            if (nNode.getNodeType() == Node.ELEMENT_NODE && temp == index) {
	               Element eElement = (Element) nNode;
	             // System.out.println( eElement.getAttribute("type"));
	          	  NodeList list = eElement.getElementsByTagName("Item");
	          	  System.out.println(list.getLength());
	          	  
	          	  for (int i = 0; i < list.getLength(); i++){
	          		  Node nNode2 = list.item(i);
	          		  Element eElement2 = (Element) nNode2;
		              String test = eElement2.getAttribute("type");
		          //	  System.out.println(test);
		          	  
		          	  
		          	 if ( test.equals("CEntityDef")){
			            
			            	
			            	 NodeList testdio = eElement2.getElementsByTagName("position");
			            	 NodeList rotList = eElement2.getElementsByTagName("rotation");
			            	 NodeList diocan = eElement2.getElementsByTagName("archetypeName");
			            	 Element position = (Element) testdio.item(0);
			            	 Element rotation = (Element) rotList.item(0);
			            	 Element archetype = (Element) diocan.item(0);
			            	 String hash = '`' + archetype.getTextContent() + '`';
			            	 
			            	// if (! hash.contains("V_16")) {
			            	 String x =  position.getAttribute("x");
			            	 String y =  position.getAttribute("y");
			            	 String z =  position.getAttribute("z");
			            	 
			            	 float rotX = Float.parseFloat( rotation.getAttribute("x"));
			            	 float rotY = Float.parseFloat( rotation.getAttribute("y"));
			            	 float rotZ = Float.parseFloat( rotation.getAttribute("z"));
			            	 float rotW = Float.parseFloat( rotation.getAttribute("w"));
			            	 
			            	 
			            	    // roll (x-axis rotation)
			            	    double sinr_cosp = 2 * (rotW * rotX + rotY * rotZ);
			            	    double cosr_cosp = 1 - 2 * (rotX* rotX + rotY * rotY);
			            	   double roll = Math.atan2(sinr_cosp, cosr_cosp);
			            	   double pitch = 0;
			            	   double yaw = 0;


			            	    // pitch (y-axis rotation)
			            	    double sinp = 2 * (rotW * rotY - rotZ * rotZ);
			            	    if (Math.abs(sinp) >= 1){
			            	    	System.out.println("dio");
			            	    	pitch = Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
			            	    }
			            	    //  use 90 degrees if out of range
			            	    else
			            	        pitch = Math.asin(sinp);

			            	    // yaw (z-axis rotation)
			            	    double siny_cosp = 2 * (rotW * rotZ + rotX * rotY);
			            	    double cosy_cosp = 1 - 2 * (rotY * rotY + rotZ * rotZ);
			            	    yaw = Math.atan2(siny_cosp, cosy_cosp);
			            	    
			            	    Quaternion q = new Quaternion(0.0, 0.0, 0.0, 1.0);
			            	 
			            	    
			            	    System.out.println("test: " + q.getInverse());
			            	 
			            	 
			            	 
			            	 
			            	 String item = "item";
			            	 String create = item + " = CreateObject(" + hash + ",generator.x + " + x + " ,generator.y +" + y + ",generator.z+ " + z + ",false,false,false)\n";
			            	 String quaternion = "SetEntityQuaternion(" + item + "," + rotX + "," + rotY + "," + rotZ + "," + rotW + ")\n";
			            	 String freeze = "FreezeEntityPosition(" + item + ",true)\n";
			            	 String insert = "table.insert(objects," + item + ")\n";
			            	 
			            	 sb.append(create);
			            	 sb.append(quaternion);
			            	 sb.append(freeze);
			            	 sb.append(insert);

			            }
		          	  
	          	  }
	          	  

	            }
	            
	         }
	         
	         File file = new File("file.txt");
	         BufferedWriter writer = null;
	         try {
	             writer = new BufferedWriter(new FileWriter(file));
	             writer.write(sb.toString());
	         } finally {
	             if (writer != null) writer.close();
	         }
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	      }

	}
	

}
