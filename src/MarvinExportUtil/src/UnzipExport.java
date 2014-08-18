import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;


public class UnzipExport {

	public static void run(String[] args) {
		if( args.length != 1){
			System.out.println("Need to enter a folder and version");
			return;
		}
		try{
			File f = new File( args[0] );
			if( f.exists() ){
				String destination = "PlatModX";
				String source = args[0];
				unzip( source, destination );
				recurseUnzip( destination );
			}
			else
				System.out.println("Cannot find folder!");
		}
		catch(Exception e){
			System.out.println("main error");
		}
	}
	
	public static void unzip( String src, String dest){
		try {
			ZipFile zip = new ZipFile(src);
			zip.extractAll(dest);
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
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
			System.err.println("error backing up " + e);
		}
	}
	/*
	 * Empty out folder and then delete folder recursively
	 */
	private static void emptyDir(String name){
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

	private static void copyDirectory(String old, String nw ){
		File git = new File( old );
		File gb = new File( nw );
		//if(! gb.exists()) {
		//    try {
		//		gb.createNewFile();
		//	} catch (IOException e) {
		//		// TODO Auto-generated catch block
		//		e.printStackTrace();
		//	}
		//} 
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
}
