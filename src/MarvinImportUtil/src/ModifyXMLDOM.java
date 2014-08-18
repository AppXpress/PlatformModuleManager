
import java.io.File;
import java.io.IOException;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
 
public class ModifyXMLDOM {

	public static void main(String path , String co , boolean js) {
        File xmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
             
            //update attribute value
            //updateAttributeValue(doc);
            if( checkTag( doc ))
            	if( js )
            		updateTagJs( doc , co );
            	else
            		updateTagZip( doc , co );
            else
            	addScriptingTag( doc , js , co );
            
             
            //update Element value
            //updateElementValue(doc);
             
            //delete element
            //deleteElement(doc);
             
            //add new element
            //addElement(doc);
            
            //addScriptingTag( doc , true , "CO");
             
            //write the updated document to file or console
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            System.out.println("XML file updated successfully");
             
        } catch (SAXException | ParserConfigurationException | IOException | TransformerException e1) {
            e1.printStackTrace();
        }
    }
 
    private static void addElement(Document doc) {
        NodeList employees = doc.getElementsByTagName("Employee");
        Element emp = null;
         
        //loop for each employee
        for(int i=0; i<employees.getLength();i++){
            emp = (Element) employees.item(i);
            Element salaryElement = doc.createElement("salary");
            salaryElement.appendChild(doc.createTextNode("10000"));
            emp.appendChild(salaryElement);
        }
    }
 
    private static void deleteElement(Document doc) {
        NodeList employees = doc.getElementsByTagName("Employee");
        Element emp = null;
        //loop for each employee
        for(int i=0; i<employees.getLength();i++){
            emp = (Element) employees.item(i);
            Node genderNode = emp.getElementsByTagName("gender").item(0);
            emp.removeChild(genderNode);
        }
         
    }
 
    private static void updateElementValue(Document doc) {
        NodeList employees = doc.getElementsByTagName("Employee");
        Element emp = null;
        //loop for each employee
        for(int i=0; i<employees.getLength();i++){
            emp = (Element) employees.item(i);
            Node name = emp.getElementsByTagName("name").item(0).getFirstChild();
            name.setNodeValue(name.getNodeValue().toUpperCase());
        }
    }
    /*
     * Checks the xml DOM to see if the scriptingFeature tag is included in the
     * tree
     */
    private static boolean checkTag(Document doc ){
    	NodeList scriptingFeature = doc.getElementsByTagName("scriptingFeature");
    	if( scriptingFeature.getLength() == 0 )
    		return false;
    	else
    		return true;
    }
    /*
     * Updates the scriptingFeature tag to indicate presence of 1 custom
     * object JS file
     */
    private static void updateTagJs( Document doc , String co){
    	NodeList scriptingTag = doc.getElementsByTagName("scriptingFeature");
    	Element elm = (Element) scriptingTag.item(0);
    	
    	Node name = elm.getElementsByTagName("mimeType").item(0).getFirstChild();
        name.setNodeValue("text/javascript");
        
        String jsName = "ScriptDesign_$"+ co + ".js";
        name = elm.getElementsByTagName("fileName").item(0).getFirstChild();
        name.setNodeValue(jsName);
        
    }
    /*
     * Updates the scriptingFeature atg to indicate presence of more than
     * one custom object JS files - therefore the files must be zipped up
     * in order to properly import
     */
    private static void updateTagZip( Document doc, String co){
    	NodeList scriptingTag = doc.getElementsByTagName("scriptingFeature");
    	Element elm = (Element) scriptingTag.item(0);
    	
    	Node name = elm.getElementsByTagName("mimeType").item(0).getFirstChild();
        name.setNodeValue("application/zip");
        
        String zipName = "ScriptDesign_$"+ co + ".zip";
        name = elm.getElementsByTagName("fileName").item(0).getFirstChild();
        name.setNodeValue(zipName);
    }
    /*
     * Adds scriptingTag to DOM of xml script design. Adds the tag as a jsFile
     * if jsFile is true or a zip file is jsFile is false
     */
    private static void addScriptingTag( Document doc , boolean jsFile , String co ){
    	Element scripting = doc.createElement("scriptingFeature");
    	
    	Element enabled = doc.createElement("enabled");
    	enabled.appendChild( doc.createTextNode("true"));
    	scripting.appendChild(enabled);
    	
    	Element mime = doc.createElement("mimeType");
    	String type;
    	if( jsFile )
    		type = "text/javascript";
    	else
    		type = "application/zip";
    	mime.appendChild( doc.createTextNode(type));
    	scripting.appendChild(mime);
    	
    	Element desc = doc.createElement("description");
    	if( jsFile )
    		type = "generic.js";
    	else
    		type = "generic.zip";
    	desc.appendChild( doc.createTextNode(type));
    	scripting.appendChild(desc);
    	
    	Element fn = doc.createElement("fileName");
    	if( jsFile )
    		type = "ScriptDesign_$"+ co + ".js";
    	else
    		type = "ScriptDesign_$" + co + ".zip";
    	fn.appendChild( doc.createTextNode(type));
    	scripting.appendChild(fn);
    	
    	Element fme = doc.createElement("fieldMaskingEnabled");
    	fme.appendChild( doc.createTextNode("true"));
    	scripting.appendChild(fme);
    	try{
    		NodeList dom = doc.getElementsByTagName("CustomObjectDesignV110");
    		dom.item(0).appendChild(scripting);
    	}
    	catch(NullPointerException e){
    		System.err.println("Cannot find tag CustomObjectDesignV110");
    	}
    	
    }
        
    private static void updateAttributeValue(Document doc) {
        NodeList employees = doc.getElementsByTagName("Employee");
        Element emp = null;
        //loop for each employee
        for(int i=0; i<employees.getLength();i++){
            emp = (Element) employees.item(i);
            String gender = emp.getElementsByTagName("gender").item(0).getFirstChild().getNodeValue();
            if(gender.equalsIgnoreCase("male")){
                //prefix id attribute with M
                emp.setAttribute("id", "M"+emp.getAttribute("id"));
            }else{
                //prefix id attribute with F
                emp.setAttribute("id", "F"+emp.getAttribute("id"));
            }
        }
    }
 
}