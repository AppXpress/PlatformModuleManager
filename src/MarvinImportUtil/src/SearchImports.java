
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Usage: call InitImports.parseD
 */

public class SearchImports {
	private static String filePath;
	private static ArrayList <String> importFiles;
	/*
	 * Look through top comment and try to find @!import statements
	 * File name directly after @!import statement will be attempted
	 * to be imported into the current folder
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
	private static void scanLine( String line ){
		//System.out.println(line);
		line = line.replaceAll(",", " ");
		//System.out.println(line);
		if( line.contains("@!import")  ){
			String [] words = line.split(" ");
			boolean rdyImport = false;
			for( int i = 0 ; i < words.length ; i++ ){
				//System.out.println( words[i]);
				if( rdyImport ){
					//rdyImport = false;
					importFiles.add( words[i] );
				}
				if( words[i].contains("@!import") )
					rdyImport = true;
			}			
		}					
	}
}