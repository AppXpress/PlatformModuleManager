import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
/**
 * Main method of pm_builder_util.jar. Takes a folder, maps it to a zippable
 * file structure, then zips it up.
 * 
 * @author Andrew Reynolds
 * @version	1.0
 * @date	8-27-2014
 * GT Nexus
 */
public class PlatformModuleBuilder {
	/**
	 * 
	 * @param args	0 -	Name of customer folder
	 * 				1 - Name of platform module folder
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
		CoDesignXML.iter( args[0] , args[1] ); 
		
		//Maps Git repo to importable file structure
		RepositoryMapper.map( root );
		
		new ZipUtility( root );
	}
	/**
	 * Iterates through folder customer/customer/folder
	 * @param customer		Name of customer folder
	 * @param folder		Name of platform module folder
	 */
	private static void runImportFind( String customer, String folder){
		String path =  "customer/"+customer+"/"+folder;
		recursiveSearch( path );
	}
	/**
	 * Looks recursively through the file structure 
	 * Searching through the file structure to find @!import statements.
	 * It ignores zip, xml , and xsd files
	 * 
	 * @param fp	File structure to recursively search
	 */
	private static void recursiveSearch( String fp ){
		File topFolder = new File( fp );
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
	/**
	 * Imports file 'file' from lib folder into location
	 * 
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
