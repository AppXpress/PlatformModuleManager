import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Properties;

import static java.nio.file.StandardCopyOption.*;
/**
 * Main method used by pm_extrator_util.jar. Unzips an exported platform module
 * and maps it into a working directory.
 * 
 * @author Andrew Reynolds
 * @version	1.0
 * @date	8-27-2014
 * GT Nexus
 */
public class GitMap {
	public static boolean OVER_WRITE_FEF;
	public static boolean OVER_WRITE_SCRIPTS;
	
	public static final String PLATFORM_MODULE_UNZIP_NAME = "PlatModX";
	
	public static ArrayList<String> overwritten_Scripts;
	/**
	 * Takes 4-6 args ->
	 * @arg[0]			Exported Platform Module Name
	 * @arg[1]			Relative Path of GIT staging folder
	 * @arg[2]			Customer of Platform Module
	 * @arg[3]			Custom Object that is being exported
	 * 
	 * Optional Args	
	 * @args[4]			If Y ->		overwriteScripts = true
	 * @args[5]			If Y ->		overwriteFEF = true
	 */
	public static void main(String args[] ) throws IOException{
		if( args.length < 4 ){
			System.err.println("Incorrect Args");
			return;
		}
		overwritten_Scripts = new ArrayList<String>();
		if( args.length > 4 && args[4].equalsIgnoreCase("y")){
			OVER_WRITE_SCRIPTS = true;
		}
		else
			OVER_WRITE_SCRIPTS = false;
		if( args.length > 5 && args[5].equalsIgnoreCase("y")){
			OVER_WRITE_FEF = true;
		}
		else
			OVER_WRITE_FEF = false;
		
		String exportedPlatform = args[0];
		//Append .zip extension if left off 
		if( ! exportedPlatform.contains(".zip"))
			exportedPlatform = exportedPlatform + ".zip";
		
		File exp = new File ( exportedPlatform );
		
		if( ! exp.exists() ){
			System.err.println("Cannot find Exported Folder -> " + args[0]);
			return;
		}
		File git = new File( args[1] );
		if( ! git.exists() ){
			System.err.println("Cannot find local Git Directory -> " + args[1] );
		}
		
		if( new File(PLATFORM_MODULE_UNZIP_NAME).exists() )
			UnzipExport.emptyDir(PLATFORM_MODULE_UNZIP_NAME);
		
		//Unzip Exported Platform Module
		UnzipExport.run( exportedPlatform );
		
		//Make files/folder human readable . i.e -> strip $ signs
		UnzipExport.readable( PLATFORM_MODULE_UNZIP_NAME );
		
		//Back up folder about to be overwritten
		UnzipExport.backup( args[1] , args[2] , args[3]);
		
		//Map exported folder to local Git directory holding same platform module
		new GitMap( args[1] , args[2] , args[3]);
		
		if( OVER_WRITE_SCRIPTS)
			printOWS();
	}
	/**
	 * Custom Link xml files are going to be replaced with the new 
	 * custom link files from the exported module. This method rids the
	 * CustomLinkD1 folder of outdate custom links files.
	 * 
	 * @param gitPath		path of platform module in local dir
	 */
	private static void clearCustomLinksXML(String gitPath) {
		String fullPath = gitPath + "/" + "CustomLinkD1";
		File dir = new File( fullPath );
		if( dir.exists() ){
			for( File x : dir.listFiles() )
				x.delete() ;
		}
	}
	/**
	 * Maps the exported platform module folder to the platform module
	 * folder currently residing in the path depicted by the params.
	 * 
	 * @param src		Source that depicts backend of map -> will be 'PlatModX'
	 * @param git		Folder that holds local Git directory
	 * @param customer	Customer folder in which this platform module resides
	 * @param pm		Platform Module name
	 */
	public GitMap( String git, String customer, String pm){
		//Ensure path exists - GIT repo is set up correctly
		if ( ! validatePath( git , customer, pm ) )
			return;
		
		String path = git + "/customer/" + customer + "/" + pm;
		
		clearCustomLinksXML( path );
		
		this.mapCoDesign( PLATFORM_MODULE_UNZIP_NAME);
		
		this.mapFolders( PLATFORM_MODULE_UNZIP_NAME , path );
		
	}
	/**
	 * Map folder export to folder destination
	 * 
	 * @param export			Folder where unzipped exported platform module is
	 * @param destination		Folder where platform module is in local git directory
	 */
	private void mapFolders( String export , String destination ){
		File platform = new File( export );
		File coFolder = new File( destination );		
		
		for( String p : platform.list() ){
			File platSub = new File( export + "/" + p );
			boolean gitContains = false;
			if( platSub.isDirectory() ){
				for( String g : coFolder.list() ){
					if( g.equals(p) ){
						gitContains = true;
						break;
					}
			}
			//if folder is contained already in GIT, enter the folder
			if( gitContains ) {
				//Ignore CustomUi folder if over write fef is set to false
				//Therefore, does not over write fef folder
				if( ! OVER_WRITE_FEF && p.equals("customUi")){
					//Do not go in here
				}
				else
					mapFolders( export + "/" + p , destination + "/" + p );
			}
			// if it is a bundle within the export, ignore the bundle and go inside
			else if( p.endsWith("Bundle") ){
				mapFolders( export + "/" + p , destination );		
			}
			//if file , copy file from export into destination 
			else{
				System.out.println("Creating " + destination + "/" + p );
				File dir = new File( destination + "/" + p);
				dir.mkdir();
				mapFolders( export + "/" + p , destination + "/" + p);
			}
		}
		else{
			File f = new File( destination + "/" + p);
			//if file -> rewrite file to match that in exported module
			if( OVER_WRITE_SCRIPTS ){
				if( f.exists() && f.getName().endsWith(".js")){
					System.out.println( f.getName() );
					overwritten_Scripts.add(f.getName());
				}
				System.out.println("Adding -> " + destination + "/" + p);
				mapCopy( export + "/" + p ,
						destination + "/" + p );
			}
			//If do not over write scripts , then dont overwrite
			else{
				//Only copy if file does not exist already or if not a script file( .js)
				if( ! f.exists() || ! f.getName().endsWith(".js")){
					System.out.println("Adding -> " + destination + "/" + p);
					mapCopy( export + "/" + p ,
							destination + "/" + p );
				}
			}
		}			
	}		
		
	}
	/**
	 * In the exported unzipped platform module, move around the custom object
	 * design scripts so they can be smoothly mapped 1=1 into git directory. This
	 * method moves there location and removes $ from their names
	 * 
	 * @param path	Path of Unzipped Exported platform module
	 */
	private void mapCoDesign(String path){
		path = path + "/CustomObjectModule/designs/scripts";
		File scripts = new File( path );
		if( scripts.exists() ){
			for( String s : scripts.list() ){
				File co = new File( path + "/" + s);
				if( co.isDirectory() )
					co.renameTo(new File( path + "/" + s.replace("ScriptDesign_$","")));	
				
				else{
					String dn = s.replace("ScriptDesign_$", "");
					String dnr = dn.replace(".js", "");
					File dir = new File( path + "/" + dnr);
					dir.mkdir();
					File change = new File( path + "/" + dir.getName() + "/" + s );
								
					try {
						Files.move( co.toPath() ,  change.toPath() , REPLACE_EXISTING );
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println( change);
					System.out.println( co.renameTo( change ) );
				}			
			}
		}
		else
			System.err.println( " cannot find script folder" );
	}
	/**
	 * Moves file location src to file location cop
	 * 
	 * @param src		File location
	 * @param cop		File location
	 */
	private void mapCopy( String src, String cop ){
		try{
		  File toCopy = new File(src);
		  File toOver = new File(cop);
		  
		  FileInputStream fis = new FileInputStream(toCopy);
		  
		  FileOutputStream fos = new FileOutputStream(toOver);
		  
		// An array to which will hold byte being read from the packed file
	      byte[] bytesRead = new byte[512];
	       
	      // Read bytes from packed file and store them in the ZIP output stream
	      int bytesNum;
	      while ((bytesNum = fis.read(bytesRead)) > 0) {
	          fos.write(bytesRead, 0, bytesNum);
	      }
	      fis.close();
	      fos.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Validates the path composed of parameters
	 * 
	 * @param folder	Highest folder
	 * @param sub		Second highest folder
	 * @param subCo		Lowest subfolder 
	 * @return			true if file structure exists
	 * 					false if file structure does not exist
	 */
	private boolean validatePath( String folder, String sub, String subCo ){
		String path = folder + "/customer";
		if( ! exists(path) ){
			System.err.println("Cannot find customer folder in GIT staging folder");
			return false;
		}
		path = path + "/" + sub;
		if( ! exists(path) ){
			System.err.println("Cannot find specific customer in customer folder");
			return false;
		}
		path = path + "/" + subCo;
		//If path does not exist - create it
		if( ! exists(path) ){
			File platform = new File( path );
			platform.mkdir();
		}
		return true;
	}
	/**
	 * Tests if path exists
	 * @param path		File location
	 * @return			true if file exists
	 * 					false otherwise
	 */
	private boolean exists(String path){

		File temp = new File( path );
		if( ! temp.exists() )
			return false;
		return true;
	}
	/**
	 * Unused method -> template if the jar were to have a propeties file
	 */
	private static void setProperties(){
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader(); 
			InputStream stream = loader.getResourceAsStream("exportor.properties");
			Properties properties = new Properties();
			properties.load( stream );
		} catch (Exception e) {
			//System.out.println("Properties thingy does not work now");
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	/**
	 * Prints the scripts that were overwritten so the user can quickly
	 * noticed if unwanted actions were performed on local git dir
	 */
	private static void printOWS(){
		System.out.print("You over wrote these scripts -> ");
		for( String s : overwritten_Scripts)
			System.out.print( s + ",");
		System.out.println();
	}
}
