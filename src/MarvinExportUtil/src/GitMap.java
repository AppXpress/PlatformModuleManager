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

public class GitMap {
	/*
	 * Takes 3 args ->
	 * @arg[0]			Exported Platform Module Name
	 * @arg[1]			Relative Path of GIT staging folder
	 * @arg[2]			Customer of Platform Module
	 * @arg[3]			Custom Object that is being exported
	 * 
	 * Optional Args	
	 * @args[4]			If Y ->		overwriteScripts = true
	 * @args[5]			If Y ->		overwriteFEF = true
	 */
	public static boolean OVER_WRITE_FEF;
	public static boolean OVER_WRITE_SCRIPTS;
	public static ArrayList<String> overwritten_Scripts;
	public static void main(String args[] ) throws IOException{
		setProperties();
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
		
		//Append .zip extension if left off 
		if( ! args[0].contains(".zip"))
			args[0] = args[0] + ".zip";
		
		File exp = new File ( args[0] );
		
		if( ! exp.exists() ){
			System.err.println("Cannot find folder " + args[0]);
			return;
		}
		File git = new File( args[1] );
		if( ! git.exists() ){
			System.err.println("Cannot find folder " + args[1] );
		}
		
		String unzippedName = "PlatModX";
		
		if( new File("PlatModX").exists() )
			UnzipExport.emptyDir("PlatModX");
		//File extract = new File("PlatModX");
		//extract.createNewFile();
		String [] unZipArgs = { args[0] };
		//Unzip folder args[0]
		UnzipExport.run( unZipArgs );
		
		//Make files/folder human readable . i.e -> strip $ signs
		UnzipExport.readable( unzippedName );
		
		UnzipExport.backup( args[1] , args[2] , args[3]);
		
		
		//String unzippedName = "PlatModX1";
		GitMap mapper = new GitMap( unzippedName , args[1] , args[2] , args[3]);
		
		if( OVER_WRITE_SCRIPTS)
			printOWS();
	}
	/*
	 * Clears out Custom Links XML files - these will change
	 * each time and the older ones are not needed
	 */
	private static void clearCustomLinksXML(String git) {
		String fullPath = git + "/" + "$CustomLinkD1";
		File dir = new File( fullPath );
		if( dir.exists() ){
			for( File x : dir.listFiles() )
				x.delete();
		}
	}
	/*
	 * Map exported folder to git structure 
	 */
	public GitMap( String src, String git, String customer, String co){
		//Ensure path exists - GIT repo is set up correctly
		if ( ! validatePath( git , customer, co ) )
			return;
		String path = git + "/customer/" + customer + "/" + co;
		
		//clearCustomLinks folder
		clearCustomLinksXML( path );
		
		mapCoDesign(src);
		mapFolders( src , path );
		
	}
	private void mapFolders( String export , String destination ){
		File platform = new File( export );
		File coFolder = new File( destination );		
		
		//System.out.println("Plat " + platform.getName() + "  Co " + coFolder.getName() );
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
				//Ignore CustomUi folder is over write fef is set to false
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
	/*
	 * Copy src to cop
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
		if( ! exists(path) ){
			//System.err.println("Cannot find "+ subCo + " in customer " + sub );
			File platform = new File( path );
			platform.mkdir();
			//return false;
		}
		return true;
	}
	private boolean exists(String path){

		File temp = new File( path );
		if( ! temp.exists() )
			return false;
		return true;
	}
	
	private static void setProperties(){
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader(); 
			InputStream stream = loader.getResourceAsStream("marvin.properties");
			Properties properties = new Properties();
			properties.load( stream );
		} catch (Exception e) {
			System.out.println("Properties thingy does work now");
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	private static void printOWS(){
		System.out.print("You over wrote these scripts -> ");
		for( String s : overwritten_Scripts)
			System.out.print( s + ",");
		System.out.println();
	}
}
