import java.io.File;

/**
 * Ensures that custom object designs xml match up with their scripting
 * 
 * @author Andrew Reynolds
 * @version	1.0
 * @date	8-27-2014
 * GT Nexus
 */
public class CoDesignXML {
	public static String designPath;
	public static String scriptPath;
	/**
	 * Iterates through a platform module
	 * 
	 * @param customer	Customer folder where platform module exists
	 * @param folder	Platform module folder
	 */
	public static void iter(String customer, String folder ){
		String path =  "customer/"+customer+"/"+folder;
		File exist = new File( path );
		if( ! exist.exists() ){
			System.out.println("Cannot find path customer/" + customer + "/" + folder);
			return;
		}
		scriptPath = path + "/CustomObjectModule/designs/Scripts";
		exist = new File( scriptPath );
		if( ! exist.exists() ){
			System.out.println("No scripts folder, must not be any CO scripts");
			return;
		}
		designPath =  path + "/CustomObjectModule/designs";
		checkXML( );
	}
	/**
	 * Checks to see if xml matches with scripts for custom object
	 */
	private static void checkXML(  ){
		try{
			File scripts = new File( scriptPath );
			
			for( String s : scripts.list() ){
				File sub = new File( scriptPath + "/" + s );
				if( ! sub.exists() ){
					System.err.println("abort " + scriptPath + "/" + s );
					break;
				}
				System.out.println( scriptPath + "/" + s);
				String xmlName = designPath + "/" + "Design_" + s + ".xml";
				System.out.println( "xml " + xmlName);
				System.out.println( sub.list().length );
				if( sub.list().length == 1)
					ModifyXMLDOM.modify( xmlName , s , true);
				else
					ModifyXMLDOM.modify( xmlName, s, false);
			}
		}
		catch(Exception e){
			System.err.println(" Check XML exception ");
		}
	}
	
}