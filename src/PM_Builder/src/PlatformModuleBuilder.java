/**
 * Main method of pm_builder_util.jar. This executeable does the following
 * things in order with the end goal to create an importable zip file.
 * 
 * 1) Scans the js scripts in the platform module for the !import symbol. If found,
 * automatically imports indicated scripts from lib folder into correct folder.
 * 2) Makes sure each of the custom object design xml's contain the correct scriptingFeature
 * tag, therefore ensuring that the platform module's scripts will import correctly.
 * 3) Maps the local git structure to the folder structure required to become importable
 * by GTNexus platform. This requires correct folder names and bundling js scripts into
 * zip files when necessary.
 * 4) Zips up correctly mapped file structure into a zip file, ready to import into GTNexus
 * system
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
			System.err.println("Incorrect parameters. Program requires two parameters");
			return;
		}
		String customer = args[0];
		String pmFolder = args[1];
		PlatformModuleBuilder pmb = new PlatformModuleBuilder( customer , pmFolder );
	}
	/**
	 * Inputs recently pulled down git repository and outputs zip file that is
	 * ready to be imported onto GTNexus system
	 * 
	 * @param customer		Name of customer folder
	 * @param platform		Name of platform module folder
	 */
	public PlatformModuleBuilder( String customer , String platform ){
		//Find @!import and import allocated scripts
		System.out.println("Gathering imports...");
		
		//runImportFind( customer , platform );
		this.runImportFind(customer, platform);
		//Zip up the folder -> args[1] 
		String root = "customer/" + customer +"/" + platform;
		//Zips up File structure - now ready to import	
				
		//Ensure design xml files correctly indicate
		//custom object design scripts
		this.xmlDesignCustomObjectScriptMatcher( customer, platform );
				
		//Maps Git repo to importable file structure
		this.map( root );
		
		//Zips up platform module folder
		this.zip( root ); 
					
	}
	/**
	 * Zips up platform module folder
	 * 
	 * @param root	Path of platform module folder to zip
	 */
	private void zip(String root) {
		new ZipUtility( root );		
	}
	/**
	 * Maps pulled platform module to a file structure that can be imported
	 * onto GTNexus system
	 * 
	 * @param root	File path of platform module
	 */
	private void map(String root) {
		PlatformMapUtil.map( root );
	}
	/**
	 * Searches through the custom object module folder and ensures that
	 * each custom object design xml file corresponds to the correct number
	 * of custom object scripts
	 * 
	 * @param customer		Name of customer folder
	 * @param platform		Name of platform module folder
	 */
	private void xmlDesignCustomObjectScriptMatcher(String customer,
			String platform) {
		CoDesignXML.iter( customer , platform ); 
	}
	/**
	 * Iterates through folder customer/customer/folder
	 * @param customer		Name of customer folder
	 * @param folder		Name of platform module folder
	 */
	private void runImportFind( String customer, String folder){
		String path =  "customer/"+customer+"/"+folder;
		ImportScanner.search ( path );
	}
}
