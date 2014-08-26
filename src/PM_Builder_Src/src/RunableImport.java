

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunableImport {
	/*
	 * Args		0	Name of Customer
	 * 	  		1	Name of Folder
	 */
	
	public static void main( String args[]){
		if( args.length != 2 ){
			System.out.println("Incorrect parameters");
			return;
		}
		//Find @!import and import allocated scripts
		System.out.println("Gathering imports...");
		runImportFind( args[0] , args[1] );
		
		//Zip up the folder -> args[1] 
		String root = "customer/" + args[0] +"/" + args[1];
		//Zips up File structure - now ready to import	
		
		//Ensure design xml files correctly indicate
		//custom object design scripts
		CoDesignXML.main( args ); 
		
		//Maps Git repo to importable file structure
		Repository_Mapper.map( root );
		
		new ZipUtility( root );
	}
	
	private static void runImportFind( String customer, String folder){
		String path =  "customer/"+customer+"/"+folder;
		recursiveSearch( path );
	}
	/*
	 * Looks recursively through the file structure 
	 * Searching through the file structure to find @!import statements
	 */
	private static void recursiveSearch( String fp ){
		File topFolder = new File( fp );
		//System.out.println( topFolder.getName() );
		for( String iter : topFolder.list()){
			File folder = new File( fp + "/" + iter );
			if( folder.isDirectory() )
				recursiveSearch( fp + "/" + folder.getName() );
			else{
				String fileName = fp + "/" + folder.getName();
				if( fileName.length() > 2 ){
					String type = fileName.substring(fileName.length()-3 , fileName.length()  );
					if( type.equals("zip") || type.equals("xml") || type.equals("xsd") ||
							type.equals("xlf"))
						continue;
				}
				System.out.println(fileName);
				ArrayList<String> filesToImport = SearchImports.parseDoc(new File(fileName));
				if( filesToImport.size() > 0 )
					for( String s : filesToImport )
						importFile( s, fp);
			}
		}
	}
	/*
	 * @param	file		Name of file to import - file found in /lib
	 * @param	location	Name of current location to import lib file
	 */
	private static void importFile( String file , String location){
		File source = new File("lib/" + file);
		if( ! ( source.exists() ) ){
			System.out.println("Failed to import " + source.getName() + " - File not Found");
			return;
		}
		File dest = new File(location + "/"+file);
		try {
			if( ! ( dest.exists() ) )
				Files.copy(source.toPath(), dest.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
