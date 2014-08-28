import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
/**
 * Unzips an exported platform module file and puts it in a folder
 * named 'PlatModX'. Also has methods to backup local git directory and
 * strip unwanted $ signs from files and folders
 * 
 * @author Andrew Reynolds
 * @version	1.0
 * @date	8-27-2014
 * GT Nexus
 */

public class ExportedPlatform {
	/**
	 * Unzips file 'folder' into PlatModX
	 * 
	 * @param folder	Path of exported zip file
	 */
	public static void unzip( String folder) {
		try{
			File f = new File( folder );
			if( f.exists() ){
				String destination = GitMap.PLATFORM_MODULE_UNZIP_NAME;
				String source = folder;
				unzip( source, destination );
				recurseUnzip( destination );
			}
			else
				System.out.println("Cannot find folder!");
		}
		catch(Exception e){
			System.out.println("Error in UnzipExport.run");
		}
	}
	/**
	 * Unzips src into dest
	 * @param src		Zip file 
	 * @param dest		Destination for zip file
	 */
	public static void unzip( String src, String dest){
		try {
			ZipFile zip = new ZipFile(src);
			zip.extractAll(dest);
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Recursively iterated through file structure folder and unzips
	 * and contained zip files
	 * @param folder		Destination of file structure to iterate over
	 */
	private static void recurseUnzip( String folder ){
		File f = new File( folder );
		if( f.isDirectory() ){
			for( String s : f.list()){
				recurseUnzip( f + "/" + s);
			}
		}
		else{
			if( folder.endsWith(".zip")){
				String d = folder.replace(".zip", "");
				unzip( folder , d );
				f.delete();
			}
		}
	}
	/**
	 * Backs up folder/customer/cust/plat into a folder called PM_Git_Backup
	 * 
	 * @param folder	Local git directory
	 * @param cust		Name of customer folder name
	 * @param plat		Platform module folder name
	 */
	public static void backup(String folder, String cust, String plat ){
		String path = folder + "/customer" + "/" + cust + "/" + plat ;
		String backup = "PM_Git_Backup/" + plat;
		if( (new File(backup).exists() )){
			emptyDir( backup );
			(new File(backup)).delete();
		}
		else{
			File nb = new File( "PM_Git_Backup" );
			nb.mkdir();
		}
		try{
			copyDirectory(path , backup );
		}
		catch(Exception e){
			System.err.println("error backing up -> " + e);
		}
	}
	/**
	 * Performs a recurisve remove on directory name
	 * 
	 * @param name	Directory that is to be cleared
	 */
	static void emptyDir(String name){
		File folder = new File(name);
		
		if( folder.isDirectory() ){
			for( String s : folder.list() )
			{
				emptyDir( name + "/" + s );
			}
			folder.delete();
		}
		else{
			//System.out.println("Cleaning..." + folder.delete());
			folder.delete();
		}			
	}
	/**
	 * Copies a file structure into anther
	 * 
	 * @param old	File structure to copy from
	 * @param nw	File structure to copy to
	 */
	private static void copyDirectory(String old, String nw ){
		File git = new File( old );
		File gb = new File( nw );
		try {
			Files.copy( git.toPath() , gb.toPath() );
			if( git.isDirectory() ){
				gb.mkdir();
				for( String iter : git.list() ){
					
					File sub = new File( git + "/" + iter);
					if( sub.isDirectory() ){
						copyDirectory( old + "/" + iter , nw + "/" + iter);
					}
					else
						Files.copy( sub.toPath() , new File(nw + "/" + iter).toPath() );						
				}
			}
			else
				Files.copy( git.toPath() , gb.toPath() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Cleans folder structure of $ signs
	 * 
	 * @param folderName	Location of file structure
	 */
	public static void readable( String folderName ){
		File f = new File(folderName);
		if( ! f.exists() )
			System.err.println("Cannot find folder -> " + folderName );
		else{
			if( f.isDirectory() ){
				for( String s : f.list() ) 
					readable( folderName + "/" + s );
			}
			if( f.getName().contains("$") ){
				String rename = f.getName().replace("$", "");
				Path p = f.toPath();
				try {
					Files.move(p, p.resolveSibling(rename));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
