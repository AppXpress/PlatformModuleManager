import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Ensure Custom Object design xml documents correctly
 * include scriptingFeature tag if it has scripts
 */
public class CoDesignXML {
	public static String designPath;
	public static String scriptPath;
	/*
	 * @args[0]		name of customer
	 * @args[1]		name of Platform Folder
	 */
	public static void main(String args[] ){
		runner( args[0], args[1]);
		//ModifyXMLDOM.main( "tester.xml", "tester ", false);
	}
	public static void runner(String c, String f ){
		String customer = c;
		String folder = f;
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
				String xmlName = designPath + "/" + "Design_$" + s + ".xml";
				System.out.println( "xml " + xmlName);
				System.out.println( sub.list().length );
				if( sub.list().length == 1)
					ModifyXMLDOM.main( xmlName , s , true);
				else
					ModifyXMLDOM.main( xmlName, s, false);
			}
		}
		catch(Exception e){
			System.err.println(" Check XML exception ");
		}
	}
	
}