
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a file for !import statements
 * 
 * @author Andrew Reynolds
 * @version	1.0
 * @date	8-27-2014
 * GT Nexus
 */

public class ImportScanner {
	private static String filePath;
	private static ArrayList <String> importFiles;
	
	static final String IMPORT_SYMBOL = "!import";
	// TODO	For IDE, compare imported scripts functions with functions in
	// currently folder to ensure no identical, conflicting functions
	/**
	 * Looks recursively through the file structure 
	 * Searching through the file structure to find !import statements.
	 * It ignores zip, xml , and xsd files
	 * 
	 * @param fp	File structure to recursively search
	 */
	static void search( String fp ){
		File topFolder = new File( fp );
		for( String iter : topFolder.list()){
			File folder = new File( fp + "/" + iter );
			if( folder.isDirectory() )
				search( fp + "/" + folder.getName() );
			else{
				String fileName = fp + "/" + folder.getName();
				if( fileName.length() > 2 ){
					String type = fileName.substring(fileName.length()-3 , fileName.length()  );
					if( type.equals("zip") || type.equals("xml") || type.equals("xsd") ||
							type.equals("xlf"))
						continue;
				}
				System.out.println(fileName);
				ArrayList<String> filesToImport = parseDoc(new File(fileName));
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
	
	/**
	 * Look through top comment and try to find !import statements
	 * File name directly after !import statement will be attempted
	 * to be imported into the current folder
	 * 
	 * @param f		File to parse
	 */
	public static ArrayList<String> parseDoc( File f ){
		importFiles = new ArrayList<String>();
		try{
			filePath = f.getAbsolutePath();
			Scanner scan = new Scanner(f);
			String readline;
			boolean commentStart = false;
			while ( scan.hasNextLine() ){
				readline = scan.nextLine();
				Pattern startBlock = Pattern.compile("/[\\*].*");
				Pattern singleLine = Pattern.compile("//.*");
				Pattern endBlock = Pattern.compile(".*[\\*]/");
				Matcher m1 = startBlock.matcher( readline );
				Matcher m2 = singleLine.matcher( readline );
				Matcher m3 = endBlock.matcher( readline ); 
				if( m1.find() )
					commentStart = true;
				if( commentStart || m2.find() )
					scanLine( readline );
				if( m3.find() )
					commentStart = false;
				//On first line not blank that is not a comment, stop looking
				//for import statement
				if(! ( commentStart || m2.find() || ! readline.equals("")))
					break;
			}
			System.out.println("Finished Scanning");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return importFiles;
	}
	/**
	 * Looks through a line for the !import statement. If !import
	 * is found, adds the following word, which should be a name of a js script
	 * located in lib folder, and adds it to importFiles
	 * 
	 * @param	line	Line of a file
	 */
	private static void scanLine( String line ){
		line = line.replaceAll(",", " ");
		if( line.contains("!import")  ){
			String [] words = line.split(" ");
			boolean rdyImport = false;
			for( int i = 0 ; i < words.length ; i++ ){
				//System.out.println( words[i]);
				if( rdyImport ){
					importFiles.add( words[i] );
				}
				if( words[i].contains("!import") )
					rdyImport = true;
			}			
		}					
	}
}