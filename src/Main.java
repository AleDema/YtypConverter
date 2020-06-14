
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.apache.commons.math3.complex.*;
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
	         File inputFile = new File("input.xml");
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
	        		 if (s.equals("v_apartment_high")){
	        			// nList = (NodeList) nList.item(temp);
	        			 index = temp;
	        			// System.out.println(index);
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

						 Quaternion our =  new Quaternion(rotX,rotY,rotZ,rotW);
						 Quaternion parent = new Quaternion(0,0,0,1);
						 parent = parent.getInverse();
						 parent = parent.normalize();

						 Quaternion newQ = parent.multiply(our);
						 newQ = newQ.normalize();

						 newQ = newQ.getInverse();
						 newQ = newQ.normalize();
//
//						 System.out.println(hash);
//						 System.out.println("NEWQ");
//						 System.out.println(newQ.getQ0());
//						 System.out.println(newQ.getQ1());
//						 System.out.println(newQ.getQ2());
//						 System.out.println(newQ.getQ3());

						 String item = "item";
						 String create = item + " = CreateObjectNoOffset(" + hash + ",generator.x + " + x + " ,generator.y +" + y + ",generator.z+ " + z + ",false,false,false)\n";
						 String quaternion = "SetEntityQuaternion(" + item + "," + newQ.getQ3() + "," + newQ.getQ2() + "," + newQ.getQ1() + "," + newQ.getQ0() + ")\n";
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
