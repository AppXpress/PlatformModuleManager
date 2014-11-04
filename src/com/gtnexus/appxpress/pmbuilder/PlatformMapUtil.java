package com.gtnexus.appxpress.pmbuilder;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.BUNDLE;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_LINK_D1;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_OBJECT_MODULE;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_UI;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPTS;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPT_DESIGN;
import static com.gtnexus.appxpress.AppXpressConstants.TYPE_EXTENSION_D1;
import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.gtnexus.appxpress.ZipService;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

/**
 * Take pulled Git repository and map to a file structure that can be imported
 * onto Platform
 * 
 * Assumed Repository structure is that shown in SourceControlFolderFileMappings
 * excel sheet
 * 
 * Program will malfunction/produce unexpected unimportable results if Git
 * folder structure is different.
 * 
 * @author Andrew Reynolds
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
@Deprecated
public class PlatformMapUtil {

	/**
	 * Maps folder src
	 * 
	 * @param src
	 *            folder to map
	 */
	public static void map(String src) {
		File root = new File(src);
		if (!root.exists()) {
			System.out.println("Cannot find " + src);
			return;
		}
		prepareImport(src);
		createBundles(src);
	}

	/**
	 * Searches through platform module and renames specific folders and files
	 * so that they can be properly imported.
	 * 
	 * Mostly, adds $ signs where they belong to files and folders
	 * 
	 * @param src
	 *            Path of Platform Module
	 */
	private static void prepareImport(String src) {
		File platform = new File(src);
		for (String subFolder : platform.list()) {
			String path = src + File.separator + subFolder;
			fixTopFolder(path);
		}
	}

	/**
	 * Fixes names in p to be ready for the import onto GTNexus system
	 * 
	 * @param p
	 *            path of platform module
	 */
	private static void fixTopFolder(String p) {
		if (p.endsWith(CUSTOM_LINK_D1)) {
			renameFile(p, CUSTOM_LINK_D1);
		} else if (p.endsWith(TYPE_EXTENSION_D1)) {
			renameFile(p, $ + TYPE_EXTENSION_D1);
		} else if (p.endsWith(CUSTOM_OBJECT_MODULE)) {
			fixCOModule(p);
		} else if (p.endsWith(CUSTOM_UI)) {
			File customUi = new File(p);
			for (String bundle : customUi.list()) {
				File design = new File(p + File.separator + bundle);
				for (String d : design.list()) {
					renameFile(
							p + File.separator + bundle + File.separator + d, $
									+ d);
				}
			}
		}
	}

	/**
	 * Iterates through platform module
	 * 
	 * @param src
	 *            Path of platform module
	 */
	private static void createBundles(String src) {
		File r = new File(src);
		for (String folderName : r.list()) {
			System.out.println("Running... " + folderName);
			String path = src + File.separator + folderName;
			if (new File(path).isDirectory()) {
				searchSubFolder(path);
			}
		}
	}

	/**
	 * Searches to see if folder has multiple js scripts. If so, bundle them up.
	 * 
	 * @param name
	 *            Folder
	 */
	private static void searchSubFolder(String name) {
		File f = new File(name);
		// Special handling for CustomObjectModule/designs/Scripts folder
		String pathHandle = f.getAbsolutePath().toLowerCase();
		String platformIndependent = "customobjectmodule" + File.separator
				+ "designs" + File.separator + "scripts";
		if (pathHandle.contains(platformIndependent)) {
			handleCODesignScripts(name);
			return;
		}
		System.out.println(name);
		// Special handling for customUi fef bundle
		if (name.endsWith("/customUi")) {
			handleFef(name);
			// emptyDir( name );
			return;
		}
		// Count number of .js files - if greater than 1, zip em up
		int jsCount = 0;
		for (String folderName : f.list()) {
			String path = name + File.separator + folderName;
			if (isJavaScriptFile(folderName) && !(isZipDir(name))) {
				jsCount++;
			}
			if (new File(path).isDirectory()) {
				searchSubFolder(path);
			}
		}
		if (jsCount > 1) {
			System.out.println("create bundle ");
			// Creates new bundle folder and returns name
			String bundleFolder = createBundleFolder(name);
			File cur = new File(name);
			for (String s : cur.list()) {
				if (isJavaScriptFile(s)) {
					moveFiles(s, name, bundleFolder);
				}
			}
			// Zip up bundle folder
			System.out.println(" " + name + File.separator + bundleFolder);
			// Zip up new bundle folder
			ZipService zu = new ZipService(name + File.separator + bundleFolder);
			try {
				zu.zipDirectory();
			} catch (PMBuilderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Get rid of folder no longer needed
			emptyDir(name + File.separator + bundleFolder);
		}
	}

	/**
	 * Set up CO/designs/scripts into correct format
	 * 
	 * @param name
	 *            CO/designs/scripts folder
	 */
	private static void handleCODesignScripts(String name) {
		File f = new File(name);
		// Look through CO folders
		for (String foldername : f.list()) {
			System.out.println(foldername + " Foldah");
			File coFolder = new File(name + File.separator + foldername);
			if (!(coFolder.isDirectory())) {
				continue;
			}
			int fileCount = coFolder.list().length;
			// One js file - ensure its named correctly and move up
			if (fileCount == 1) {
				String rename = SCRIPT_DESIGN + $ + coFolder.getName() + ".js";
				File coJs = coFolder.listFiles()[0];
				boolean a = coJs.renameTo(new File(f.getAbsolutePath()
						+ File.separator + rename));
				if (!a) {
					System.err.println("Error with script design files");
				}
				emptyDir(coFolder.getAbsolutePath());
			}
			// Zip up multiple js files into a bundle - bundle named [Custom
			// Object]
			if (fileCount > 1) {
				String zipName = SCRIPT_DESIGN + $ + foldername;
				boolean reNameFolder = coFolder.renameTo(new File(name
						+ File.separator + zipName));
				if (!reNameFolder) {
					System.err.println("Error renaming folder");
				}
				ZipService zu = new ZipService(name + File.separator + zipName);
				try {
					zu.zipDirectory();
				} catch (PMBuilderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Get rid of folder no longer needed
				emptyDir(name + File.separator + zipName);
			}
		}

	}

	/**
	 * Is this file a js script?
	 * 
	 * @param s
	 *            Path of file
	 * @return true - if file is a js script false otherwise
	 */
	private static boolean isJavaScriptFile(String s) {
		String sub = s.substring(s.length() - 2);
		if (sub.equals("js")) {
			return true;
		}
		return false;
	}

	/**
	 * Is file a zip file?
	 * 
	 * @param s
	 *            Path of file
	 * @return true - if file is zip file false otherwise
	 */
	private static boolean isZipDir(String s) {
		String sub = s.substring(s.length() - 3);
		if (sub.equals("zip")) {
			return true;
		}
		return false;
	}

	/**
	 * Creates a bundle folder - returns its name
	 * 
	 * @param parentName
	 *            Folder
	 * @return returns Folder<Bundle> file name
	 */
	private static String createBundleFolder(String parentName) {
		File folder = new File(parentName);
		String[] extractName = parentName
				.split(File.separatorChar == '\\' ? "\\\\" : File.separator);
		String newFolderName = extractName[extractName.length - 1];
		newFolderName = newFolderName.concat(BUNDLE);
		newFolderName = newFolderName.substring(0, 1).toUpperCase()
				.concat(newFolderName.substring(1));
		System.out.println(newFolderName);
		File child = new File(folder, newFolderName);
		child.mkdir();
		return child.getName();
	}

	/**
	 * Moves a file into the specified target folder Used for moving js files
	 * into a bundle folder
	 * 
	 * @param fileName
	 *            Name of file to be moved
	 * @param folderPath
	 *            Path of current folder of file
	 * @param subFolder
	 *            Path of folder where file is to be moved
	 */
	private static void moveFiles(String fileName, String folderPath,
			String subFolder) {
		File original = new File(folderPath + File.separator + fileName);
		File newFile = new File(folderPath + File.separator + subFolder
				+ File.separator + fileName);
		boolean move = original.renameTo(newFile);
		if (!move) {
			System.err.println("Did not work");
		}
	}

	/**
	 * Renames a file in the same directory
	 * 
	 * @param org
	 *            Original file path
	 * @param name
	 *            New name of file designated by org path
	 */
	private static void renameFile(String org, String name) {
		File custom = new File(org);
		Path src = custom.toPath();
		try {
			Files.move(src, src.resolveSibling(name));
		} catch (IOException e) {
			System.out.println("File move failed");
		}
	}

	/**
	 * Empty out folder and then delete folder recursively
	 * 
	 * @param name
	 *            Name of folder to empty
	 */
	private static void emptyDir(String name) {
		File folder = new File(name);
		if (folder.isDirectory()) {
			for (String s : folder.list()) {
				emptyDir(name + File.separator + s);
			}
			folder.delete();
		} else {
			folder.delete();
		}
	}

	/**
	 * Handles fef folder
	 * 
	 * @param name
	 *            name of folder
	 */
	private static void handleFef(String name) {
		File sub = new File(name);
		for (String s : sub.list()) {
			// If its already a zip, ignore
			if (s.endsWith(ZIP_EXTENSION)) {
				continue;
			}
			System.out.println("s--- " + s);
			// Zip up fef bundle folder
			ZipService zu = new ZipService(name + File.separator + s);
			try {
				zu.zipDirectory();
			} catch (PMBuilderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Get rid of folder no longer needed
			emptyDir(name + File.separator + s);
		}
	}

	/**
	 * Adds $ to designs and scripts inside CustomObjectModule folder
	 * 
	 * @param path
	 *            Folder path
	 */
	private static void fixCOModule(String path) {
		// Go into designs
		String designPath = path + File.separator + "designs";
		File designsFolder = new File(designPath);
		// Fixes xml files
		if (designsFolder.exists()) {
			for (String s : designsFolder.list()) {
				String add = s.replace("Design_", "Design_$");
				renameFile(designPath + File.separator + s, add);
			}
		}
		// Fixes scripts
		String scriptPath = designPath + File.separator + SCRIPTS;
		File scriptFolder = new File(scriptPath);
		if (!scriptFolder.exists()) {
			System.err
					.println("Cannot find CustomObjectModule/designs/scripts folder");
		} else {
			for (String s : scriptFolder.list()) {
				File f = new File(scriptPath + File.separator + s);
				if (f.isDirectory()) {
					for (String y : f.list()) {
						if (!y.contains("_$")) {
							String rep = y.replace(SCRIPT_DESIGN, SCRIPT_DESIGN
									+ $);
							renameFile(scriptPath + File.separator + s
									+ File.separator + y, rep);
						}
					}
				}
			}
		}
		// Go into xsd
		String xsdPath = path + File.separator + "xsd";
		File xsdFolder = new File(xsdPath);
		if (!xsdFolder.exists())
			System.err.println("Cannot find CustomObjectModule/xsd folder");
		else {
			for (String s : xsdFolder.list()) {
				// Add $ infront of file name -> rename file
				renameFile(xsdPath + File.separator + s, $ + s);
			}
		}
	}
}