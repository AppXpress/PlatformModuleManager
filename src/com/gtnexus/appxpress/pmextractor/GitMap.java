package com.gtnexus.appxpress.pmextractor;

import static com.gtnexus.appxpress.AppXpressConstants.APPXRESS_FOLDER;
import static com.gtnexus.appxpress.AppXpressConstants.FS;
import static com.gtnexus.appxpress.AppXpressConstants.PLATFORM_MODULE_UNZIP_NAME;
import static com.gtnexus.appxpress.AppXpressConstants.SETTINGS_FILENAME;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Main method used by pm_extrator_util.jar. Unzips an exported platform module
 * and maps it into a working directory.
 * 
 * Accomplishes the following steps 1) Validates passed in parameters to ensure
 * the files exists and are accessible 2) Unzips the exported platform module 3)
 * Strips the platform module of $ 4) Backs up the current local directory 5)
 * Overwrites the current directory with the exported platform module by mapping
 * the exported platform module into the local directory
 * 
 * @author Andrew Reynolds
 * @author Eric Hu
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class GitMap {
	@SuppressWarnings("serial")
	public static final List<String> options = new ArrayList<String>() {
		{
			add("platformZip");
			add("localDir");
			add("customer");
			add("platform");
			add("overwriteScripts");
			add("overwriteFef");
		}
	};

	private String exportedPlatform;
	private String localDir;
	private String customer;
	private String platform;
	private boolean overwriteScripts = false;
	private boolean overwriteFef = false;
	public ArrayList<String> overwrittenScripts;

	/**
	 * Maps the exported platform module folder to the platform module folder
	 * currently residing in the path depicted by the params.
	 * 
	 * @param exportedZip
	 *            String Source that depicts backend of map -> will be
	 *            'PlatModX'
	 * @param localDir
	 *            String Folder that holds local Git directory
	 * @param customer
	 *            String Customer folder in which this platform module resides
	 * @param pm
	 *            String Platform Module name
	 * @param overwriteScripts
	 *            boolean Whether or not to overwrite the scripts
	 * @pawram overwriteFef boolean Whether or not ot overwrite FEF
	 */
	public GitMap(String exportedPlatform, String localDir, String customer,
			String platform, boolean overwriteScripts, boolean overwriteFef) {
		this.exportedPlatform = exportedPlatform;
		this.localDir = localDir;
		this.customer = customer;
		this.platform = platform;
		this.overwriteScripts = overwriteScripts;
		this.overwriteFef = overwriteFef;

		// Ensure path exists - GIT repo is set up correctly
		if (!validatePath(localDir, customer, platform)) {
			System.out.println("ERROR: Paths are not set up properly!");
			return;
		}

		// Unzip Exported Platform Module
		unzipLocalDir();

		// Make files/folder human readable . i.e -> strip $ signs
		readable(PLATFORM_MODULE_UNZIP_NAME);

		// Back up folder about to be overwritten
		backup(localDir, customer, platform);

		String path = localDir + FS + "customer" + FS + customer + FS
				+ platform;

		clearCustomLinksXML(path);

		this.mapCoDesign(PLATFORM_MODULE_UNZIP_NAME);
		this.mapFolders(PLATFORM_MODULE_UNZIP_NAME, path);

		// Make sure Git Folder and Exported Folder exist
		if (!this.validateFolders(exportedPlatform, localDir)) {
			throw new IllegalArgumentException("Folder is not valid: "
					+ exportedPlatform);
		}

	}

	/**
	 * Takes 4-6 args ->
	 * 
	 * @args[0] Exported Platform Module Name
	 * @args[1] Relative Path of GIT staging folder
	 * @args[2] Customer of Platform Module
	 * @args[3] Platform Module that is being exported
	 * 
	 *          Optional args
	 * @args[4] If Y -> overwriteScripts = true
	 * @args[5] If Y -> overwriteFEF = true
	 */
	public static void main(String args[]) throws IOException {
		boolean overwriteScripts = false;
		boolean overwriteFef = false;
		String exportedPlatform;
		String localDir;
		String customer;
		String platform;
		Scanner scanner = new Scanner(new InputStreamReader(System.in));
		String homeDir = System.getProperty("user.home");
		File appXpressDirectory = new File(homeDir + FS + APPXRESS_FOLDER);
		File settingsFile = new File(homeDir + FS + APPXRESS_FOLDER + FS
				+ SETTINGS_FILENAME);

		// Check if directory for settings file exists, if not create
		if (!appXpressDirectory.isDirectory()) {
			appXpressDirectory.mkdirs();
		}
		// Create file for settings if possible
		if (!settingsFile.exists()) {
			if (!settingsFile.createNewFile()) {
				System.out
						.println("WARNING: Settings file couldn't be created: "
								+ homeDir + FS + APPXRESS_FOLDER + FS
								+ SETTINGS_FILENAME);
			}
		}

		FileInputStream propInputStream = new FileInputStream(settingsFile);
		Properties settings = new Properties();
		settings.load(propInputStream);

		exportedPlatform = getParameter(options.get(0), "String", args, 0,
				settings, scanner);
		localDir = getParameter(options.get(1), "String", args, 1, settings,
				scanner);
		customer = getParameter(options.get(2), "String", args, 2, settings,
				scanner);
		platform = getParameter(options.get(3), "String", args, 3, settings,
				scanner);
		overwriteScripts = "y".equalsIgnoreCase(getParameter(options.get(4),
				"Boolean", args, 4, settings, scanner));
		overwriteFef = "y".equalsIgnoreCase(getParameter(options.get(5),
				"Boolean", args, 5, settings, scanner));

		// Append .zip extension if left off
		if (!exportedPlatform.endsWith(".zip")) {
			exportedPlatform = exportedPlatform + ".zip";
		}

		if (new File(PLATFORM_MODULE_UNZIP_NAME).exists()) {
			ExportedPlatform.emptyDir(PLATFORM_MODULE_UNZIP_NAME);
		}

		// Map exported folder to local Git directory holding same platform
		// module
		GitMap tool = new GitMap(exportedPlatform, localDir, customer,
				platform, overwriteScripts, overwriteFef);

		if (overwriteScripts) {
			tool.printOWS();
		}

		System.out.print("Save settings? [y/n]: ");
		if ("y".equalsIgnoreCase(scanner.next())) {
			// Save the settings file
			FileOutputStream settingsOutputStream = new FileOutputStream(
					settingsFile);
			settings.store(settingsOutputStream, null);
		}

		scanner.close();
	}

	/**
	 * Tries to get the a parameter by either getting it from the input, from a
	 * properties file, or by querying the user for it
	 * 
	 * @param paramName
	 *            String Used to determine the name of the parameter when asking
	 *            for it through command line
	 * @param typeOfField
	 *            String Can be Integer, Boolean or anything else, which would
	 *            default it to String. This is used to determine the type of
	 *            quesiton asked through the command line
	 * @param args
	 *            String[] The input arguments to the program
	 * @param index
	 *            int The index of the argument used for this particular field.
	 *            Used to prevent IndexOutOfBoundsException if it was just
	 *            passed in
	 * @param settings
	 *            Properties The properties info based off of the settings file
	 *            loaded
	 * @param consoleInput
	 *            Scanner Used to read input from the command line
	 * @return String of the output. If the input should have been a number, it
	 *         will return the text entered. If it was supposed to be a boolean,
	 *         it should return a y or n.
	 */
	public static String getParameter(String paramName, String typeOfField,
			String args[], int index, Properties settings, Scanner consoleInput) {
		String returnVal = "";
		String input = null;
		if (args.length > index) {
			input = args[index];
		}
		if (input != null && !"".equals(input)) {
			returnVal = input;
		} else if (settings.get(paramName) != null
				&& !"".equals(settings.get(paramName))) {
			returnVal = (String) settings.get(paramName);
		} else {
			if ("Integer".equals(typeOfField)) {
				System.out.print("Please enter the number of " + paramName
						+ "(s): ");
			} else if ("Boolean".equals(typeOfField)) {
				System.out.print("Do you want " + paramName + "? [y/n]: ");
			} else { // String
				System.out.print("Please enter " + paramName + ": ");
			}
			returnVal = consoleInput.next();
		}

		return returnVal;
	}

	/**
	 * Test to ensure local directory and exported zip folder exist
	 * 
	 * @param export
	 *            Exported folder name
	 * @param localDir
	 *            Local git directory
	 * @return true if both folders exists false otherwise
	 */
	private boolean validateFolders(String export, String localDir) {
		File f = new File(export);

		if (!f.exists()) {
			System.err.println("Cannot find Exported Folder ["
					+ f.getAbsolutePath() + "]");
			return false;
		}
		f = new File(localDir);
		if (!f.exists()) {
			System.err.println("Cannot find local Git Directory ["
					+ f.getAbsolutePath() + "]");
			return false;
		}

		return true;
	}

	/**
	 * Custom Link xml files are going to be replaced with the new custom link
	 * files from the exported module. This method rids the CustomLinkD1 folder
	 * of outdated custom links files.
	 * 
	 * @param gitPath
	 *            path of platform module in local dir
	 */
	private void clearCustomLinksXML(String gitPath) {
		String fullPath = gitPath + "/" + "CustomLinkD1";
		File dir = new File(fullPath);
		if (dir.exists()) {
			for (File x : dir.listFiles())
				x.delete();
		}
	}

	/**
	 * Map folder export to folder destination
	 * 
	 * @param export
	 *            Folder where unzipped exported platform module is
	 * @param destination
	 *            Folder where platform module is in local git directory
	 */
	private void mapFolders(String export, String destination) {
		File platform = new File(export);
		File coFolder = new File(destination);

		for (String p : platform.list()) {
			File platSub = new File(export + FS + p);
			boolean gitContains = false;
			if (platSub.isDirectory()) {
				for (String g : coFolder.list()) {
					if (g.equals(p)) {
						gitContains = true;
						break;
					}
				}
				// if folder is contained already in GIT, enter the folder
				if (gitContains) {
					// Ignore CustomUi folder if over write fef is set to false
					// Therefore, does not over write fef folder
					if (!overwriteFef && p.equals("customUi")) {
						// Do not go in here
					} else
						mapFolders(export + FS + p, destination + FS + p);
				}
				// if it is a bundle within the export, ignore the bundle and go
				// inside
				else if (p.endsWith("Bundle")) {
					mapFolders(export + "/" + p, destination);
				}
				// if file , copy file from export into destination
				else {
					System.out.println("Creating " + destination + "/" + p);
					File dir = new File(destination + "/" + p);
					dir.mkdir();
					mapFolders(export + "/" + p, destination + "/" + p);
				}
			} else {
				File f = new File(destination + "/" + p);
				// if file -> rewrite file to match that in exported module
				if (overwriteScripts) {
					if (f.exists() && f.getName().endsWith(".js")) {
						System.out.println(f.getName());
						overwrittenScripts.add(f.getName());
					}
					System.out.println("Adding -> " + destination + "/" + p);
					mapCopy(export + "/" + p, destination + "/" + p);
				}
				// If do not over write scripts , then dont overwrite
				else {
					// Only copy if file does not exist already or if not a
					// script file( .js)
					if (!f.exists() || !f.getName().endsWith(".js")) {
						System.out.println("Adding [" + destination + FS + p
								+ "]");
						mapCopy(export + FS + p, destination + FS + p);
					}
				}
			}
		}
	}

	/**
	 * In the exported unzipped platform module, move around the custom object
	 * design scripts so they can be smoothly mapped 1=1 into git directory.
	 * This method moves there location and removes $ from their names
	 * 
	 * @param path
	 *            Path of Unzipped Exported platform module
	 */
	private void mapCoDesign(String path) {
		path = path + "/CustomObjectModule/designs/scripts";
		File scripts = new File(path);
		if (scripts.exists()) {
			for (String s : scripts.list()) {
				File co = new File(path + "/" + s);
				if (co.isDirectory())
					co.renameTo(new File(path + "/"
							+ s.replace("ScriptDesign_$", "")));

				else {
					String dn = s.replace("ScriptDesign_$", "");
					String dnr = dn.replace(".js", "");
					File dir = new File(path + "/" + dnr);
					dir.mkdir();
					File change = new File(path + "/" + dir.getName() + "/" + s);

					try {
						Files.move(co.toPath(), change.toPath(),
								REPLACE_EXISTING);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(change);
					System.out.println(co.renameTo(change));
				}
			}
		} else
			System.err.println(" cannot find script folder");
	}

	/**
	 * Moves file location src to file location cop
	 * 
	 * @param src
	 *            File location
	 * @param cop
	 *            File location
	 */
	private void mapCopy(String src, String cop) {
		try {
			File toCopy = new File(src);
			File toOver = new File(cop);

			FileInputStream fis = new FileInputStream(toCopy);

			FileOutputStream fos = new FileOutputStream(toOver);

			// An array to which will hold byte being read from the packed file
			byte[] bytesRead = new byte[512];

			// Read bytes from packed file and store them in the ZIP output
			// stream
			int bytesNum;
			while ((bytesNum = fis.read(bytesRead)) > 0) {
				fos.write(bytesRead, 0, bytesNum);
			}
			fis.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Validates the path composed of parameters
	 * 
	 * @param folder
	 *            Highest folder
	 * @param sub
	 *            Second highest folder
	 * @param subCo
	 *            Lowest subfolder
	 * @return true if file structure exists false if file structure does not
	 *         exist
	 */
	private boolean validatePath(String folder, String sub, String subCo) {
		File path = new File(folder + FS + "customer" + FS + sub);

		if (!path.isDirectory()) {
			System.err
					.println("Cannot find specific customer in customer folder ["
							+ path.getAbsolutePath() + "]");
			return false;
		}
		path = new File(path.getPath() + FS + subCo);
		// If path does not exist - create it
		if (!path.exists()) {
			path.mkdir();
		}
		return true;
	}

	/**
	 * Unused method -> template if the jar were to have a propeties file
	 */
	private void setProperties() {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream stream = loader
					.getResourceAsStream("exportor.properties");
			Properties properties = new Properties();
			properties.load(stream);
		} catch (Exception e) {
			// System.out.println("Properties thingy does not work now");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	/**
	 * Prints the scripts that were overwritten so the user can quickly noticed
	 * if unwanted actions were performed on local git dir
	 */
	private void printOWS() {
		System.out.print("You over wrote these scripts -> ");
		for (String s : overwrittenScripts)
			System.out.print(s + ",");
		System.out.println();
	}

	/**
	 * Backs up folder/customer/cust/plat into a folder called PM_Git_Backup
	 * 
	 * @param folder
	 *            Local git directory
	 * @param cust
	 *            Name of customer folder name
	 * @param plat
	 *            Platform module folder name
	 */
	public void backup(String folder, String cust, String plat) {
		String path = folder + FS + "customer" + FS + cust + FS + plat;
		String backup = "PM_Git_Backup" + FS + plat;
		if ((new File(backup).exists())) {
			emptyDir(backup);
			(new File(backup)).delete();
		} else {
			File nb = new File("PM_Git_Backup");
			nb.mkdir();
		}
		try {
			copyDirectory(path, backup);
		} catch (Exception e) {
			System.err.println("error backing up -> " + e);
		}
	}

	/**
	 * Cleans folder structure of $ signs
	 * 
	 * @param folderName
	 *            Location of file structure
	 */
	public void readable(String folderName) {
		File f = new File(folderName);
		if (!f.exists()) {
			System.err.println("Cannot find folder -> " + folderName);
		} else {
			if (f.isDirectory()) {
				for (String s : f.list()) {
					readable(folderName + FS + s);
				}
			}
			if (f.getName().contains("$")) {
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

	/**
	 * Copies a file structure into anther
	 * 
	 * @param old
	 *            File structure to copy from
	 * @param nw
	 *            File structure to copy to
	 */
	public void copyDirectory(String old, String nw) {
		File git = new File(old);
		File gb = new File(nw);
		try {
			Files.copy(git.toPath(), gb.toPath());
			if (git.isDirectory()) {
				gb.mkdir();
				for (String iter : git.list()) {

					File sub = new File(git + FS + iter);
					if (sub.isDirectory()) {
						copyDirectory(old + FS + iter, nw + FS + iter);
					} else
						Files.copy(sub.toPath(),
								new File(nw + FS + iter).toPath());
				}
			} else
				Files.copy(git.toPath(), gb.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Performs a recursive remove on directory name
	 * 
	 * @param name
	 *            Directory that is to be cleared
	 */
	public void emptyDir(String name) {
		File folder = new File(name);
		if (folder.isDirectory()) {
			for (String s : folder.list()) {
				emptyDir(name + FS + s);
			}
			folder.delete();
		} else {
			// System.out.println("Cleaning..." + folder.delete());
			folder.delete();
		}
	}

	/**
	 * Unzips file 'folder' into PlatModX
	 */
	public void unzipLocalDir() {
		try {
			File f = new File(localDir);
			if (f.exists()) {
				String destination = PLATFORM_MODULE_UNZIP_NAME;
				String source = localDir;
				unzip(source, destination);
				recurseUnzip(destination);
			} else {
				System.out.println("Cannot find folder!");
			}
		} catch (Exception e) {
			System.err.println("Error in UnzipExport.run");
		}
	}

	/**
	 * Unzips src into dest
	 * 
	 * @param src
	 *            Zip file
	 * @param dest
	 *            Destination for zip file
	 */
	public static void unzip(String src, String dest) {
		try {
			ZipFile zip = new ZipFile(src);
			zip.extractAll(dest);
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Recursively iterated through file structure folder and unzips and
	 * contained zip files
	 * 
	 * @param folder
	 *            Destination of file structure to iterate over
	 */
	private static void recurseUnzip(String folder) {
		File f = new File(folder);
		if (f.isDirectory()) {
			for (String s : f.list()) {
				recurseUnzip(f + FS + s);
			}
		} else {
			if (folder.endsWith(".zip")) {
				String d = folder.replace(".zip", "");
				unzip(folder, d);
				f.delete();
			}
		}
	}

}
