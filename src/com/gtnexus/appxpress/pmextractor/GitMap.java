package com.gtnexus.appxpress.pmextractor;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.gtnexus.appxpress.AppXpressConstants.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Accomplishes the following steps 1) Validates passed in parameters to ensure
 * the files exists and are accessible 2) Unzips the exported platform module 3)
 * Strips the platform module of $ 4) Backs up the current local directory 5)
 * Overwrites the current directory with the exported platform module by mapping
 * the exported platform module into the local directory
 * 
 * @author Andrew Reynolds
 * @author Eric Hu
 * @author John Donovan
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class GitMap {

	private final String platformZip;
	private final String localDir;
	private final String customer;
	private final String platform;
	private final boolean overwriteScripts;
	private final boolean overwriteFef;
	private final List<String> overwrittenScripts;

	
	public static GitMap createMapper(Map<ExtractorOption, String> optionMap) {
		//Preconditions would be good here.
		if(optionMap.containsKey(ExtractorOption.PLATFORM_ZIP)) {
			String platformZip = optionMap.get(ExtractorOption.PLATFORM_ZIP);
			if (!platformZip.endsWith(ZIP_EXTENSION)) {
				platformZip = platformZip + ZIP_EXTENSION;
				optionMap.put(ExtractorOption.PLATFORM_ZIP, platformZip);
			}
		}
		return new GitMap(optionMap);
	}
	
	public GitMap(Map<ExtractorOption, String> optionMap) {
		this.platformZip = optionMap.get(ExtractorOption.PLATFORM_ZIP);
		this.localDir = optionMap.get(ExtractorOption.LOCAL_DIR);
		this.customer = optionMap.get(ExtractorOption.CUSTOMER);
		this.platform = optionMap.get(ExtractorOption.PLATFORM);
		this.overwriteScripts = optionMap
				.get(ExtractorOption.OVERWRITE_SCRIPTS).equalsIgnoreCase("y");
		this.overwriteFef = optionMap.get(ExtractorOption.OVERWRITE_FEF)
				.equalsIgnoreCase("y");
		this.overwrittenScripts = new ArrayList<>();
	}

	/**
	 * Performs the appropriate actions for module extraction
	 */
	public void doMapping() {
		// Ensure path exists - GIT repo is set up correctly
		if (!validateAndEnsurePathExists(localDir, customer, platform)) {
			System.err.println("ERROR: Paths are not set up properly!");
			return;
		}
		if (!isFolder(localDir) || !exists(platformZip)) {
			System.err.println("ERROR: One of the following paths is invalid:\n\t"
					+ localDir + "\n\t" + platformZip);
			return;
		}
		cleanup();
		unzipLocalDir();
		makeHumanReadable(PLATFORM_MODULE_UNZIP_NAME);
		backup();
		clearCustomLinksXML();
		mapCoDesign(PLATFORM_MODULE_UNZIP_NAME);
		mapFolders(PLATFORM_MODULE_UNZIP_NAME, buildCustomerPath());
		if (overwriteScripts) {
			printOWS();
		}
	}

	private void cleanup() {
		if (new File(PLATFORM_MODULE_UNZIP_NAME).exists()) {
			emptyDir(PLATFORM_MODULE_UNZIP_NAME);
		}
	}

	
	private boolean exists(String path) {
		File f = new File(path);
		return f.exists();
	}
	
	/**
	 * Test to ensure folder at arbitrary path exists
	 * 
	 * @param path
	 * @return true if exists at path and is a directory
	 */
	private boolean isFolder(String path) {
		final String errorMsg = "Cannot find  folder [%s]";
		File f = new File(path);
		if (!f.exists() || !f.isDirectory()) {
			System.err.println(String.format(errorMsg, f.getAbsoluteFile()));
			return false;
		}
		return true;
	}

	private String buildCustomerPath() {
		return localDir + File.separator + CUSTOMER + File.separator
				+ customer + File.separator + platform;
	}

	/**
	 * Custom Link xml files are going to be replaced with the new custom link
	 * files from the exported module. This method rids the CustomLinkD1 folder
	 * of outdated custom links files.
	 *
	 */
	private void clearCustomLinksXML() {
		String gitPath = buildCustomerPath();
		String fullPath = gitPath + File.separator + CUSTOM_LINK_D1;
		File dir = new File(fullPath);
		if (dir.exists()) {
			for (File x : dir.listFiles()) {
				x.delete();
			}
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
		File destinationCompanyFolder = new File(destination);
		for (String platformChildName : platform.list()) {
			File platformChild = new File(export + File.separator
					+ platformChildName);
			if (platformChild.isDirectory()) {
				mapFoldersDir(export, destination, destinationCompanyFolder,
						platformChildName);
			} else {
				mapFoldersFile(export, destination, platformChildName);
			}
		}
	}

	/**
	 * @param export
	 * @param destination
	 * @param destinationCompanyFolder
	 * @param platformChildName
	 */
	private void mapFoldersDir(String export, String destination,
			File destinationCompanyFolder, String platformChildName) {
		boolean gitContains = dirDoesContain(destinationCompanyFolder,
				platformChildName);
		if (gitContains) {
			if (overwriteFef || !platformChildName.equals(CUSTOM_UI)) {
				mapFolders(export + File.separator + platformChildName,
						destination + File.separator + platformChildName);
			}
		} else if (platformChildName.endsWith(BUNDLE)) {
			mapFolders(export + File.separator + platformChildName, destination);
		} else {
			// if file , copy file from export into destination
			System.out.println("Creating " + destination + File.separator
					+ platformChildName);
			File dir = new File(destination + File.separator
					+ platformChildName);
			dir.mkdir();
			mapFolders(export + File.separator + platformChildName, destination
					+ File.separator + platformChildName);
		}
	}

	/**
	 * @param export
	 * @param destination
	 * @param platformChildName
	 */
	private void mapFoldersFile(String export, String destination,
			String platformChildName) {
		File f = new File(destination + File.separator + platformChildName);
		if (overwriteScripts) {
			if (isPreExistingJsFile(f)) {
				System.out.println(f.getName());
				overwrittenScripts.add(f.getName());
			}
			System.out.println("Adding -> " + destination + File.separator
					+ platformChildName);
			mapCopy(export + File.separator + platformChildName, destination
					+ File.separator + platformChildName);
		} else if (!isPreExistingJsFile(f)) {
			System.out.println("Adding [" + destination + File.separator
					+ platformChildName + "]");
			mapCopy(export + File.separator + platformChildName, destination
					+ File.separator + platformChildName);
		}
	}

	private boolean dirDoesContain(File dir, String subDirName) {
		for (String g : dir.list()) {
			if (g.equals(subDirName)) {
				return true;
			}
		}
		return false;
	}

	private boolean isPreExistingJsFile(File f) {
		return f.exists() && f.getName().endsWith(JS_EXTENSION);
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
		path = path + File.separator + CUSTOM_OBJECT_MODULE + File.separator
				+ DESIGNS + File.separator + SCRIPTS;
		File scripts = new File(path);
		if (scripts.exists()) {
			for (String s : scripts.list()) {
				File co = new File(path + File.separator + s);
				if (co.isDirectory()) {
					co.renameTo(new File(path + File.separator
							+ s.replace(SCRIPT_DESIGN + $, "")));
				} else {
					String dn = s.replace(SCRIPT_DESIGN + $, "");
					String dnr = dn.replace(JS_EXTENSION, "");
					File dir = new File(path + File.separator + dnr);
					dir.mkdir();
					File change = new File(path + File.separator
							+ dir.getName() + File.separator + s);
					try {
						Files.move(co.toPath(), change.toPath(),
								REPLACE_EXISTING);
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println(change);
					System.out.println(co.renameTo(change));
				}
			}
		} else {
			System.err.println("Cannot find script folder.");
		}
	}

	/**
	 * Moves file location src to file location cop
	 * 
	 * @param source
	 *            File location
	 * @param destination
	 *            File location
	 */
	private void mapCopy(String source, String destination) {
		try {
			File toCopy = new File(source);
			File toOver = new File(destination);
			FileInputStream fis = new FileInputStream(toCopy);
			FileOutputStream fos = new FileOutputStream(toOver);
			byte[] bytesRead = new byte[512];
			int bytesNum;
			while ((bytesNum = fis.read(bytesRead)) > 0) {
				fos.write(bytesRead, 0, bytesNum);
			}
			fis.close();
			fos.close();
		} catch (IOException e) {
			System.err.println("Exception when mapping "
					+ "files to proper directories.");
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
	private boolean validateAndEnsurePathExists(String folder, String sub, String subCo) {
		File path = new File(folder + File.separator + "customer"
				+ File.separator + sub);
		if (!path.isDirectory()) {
			System.err
			.println("Cannot find specific customer in customer folder ["
					+ path.getAbsolutePath() + "]");
			return false;
		}
		path = new File(path.getPath() + File.separator + subCo);
		if (!path.exists()) {
			path.mkdir();
		}
		return true;
	}

	/**
	 * Unused method -> template if the jar were to have a propeties file
	 */
	@SuppressWarnings("unused")
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
		final StringBuilder sb = new StringBuilder();
		System.out.print("You over wrote these scripts -> ");
		for (String script : overwrittenScripts) {
			sb.append(script).append(", ");
		}
		System.out.println(sb.substring(0, sb.length() -2));
	}

	/**
	 * Backs up folder/customer/cust/plat into a folder called PM_Git_Backup
	 */
	private void backup() {
		String path = buildCustomerPath();
		String backup = "PM_Git_Backup" + File.separator + platform;
		File bkpFile = new File(backup);
		if (bkpFile.exists()) {
			emptyDir(backup);
			(bkpFile).delete();
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
	private void makeHumanReadable(String folderName) {
		File f = new File(folderName);
		if (!f.exists()) {
			System.err.println("Cannot find folder -> " + folderName);
			return;
		} 
		if (f.isDirectory()) {
			for (String s : f.list()) {
				makeHumanReadable(folderName + File.separator + s);
			}
		}
		if (f.getName().contains($)) {
			String rename = f.getName().replace($, "");
			Path p = f.toPath();
			try {
				Files.move(p, p.resolveSibling(rename));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Copies a file structure into anther
	 * 
	 * @param source
	 *            File structure to copy from
	 * @param destination
	 *            File structure to copy to
	 */
	public void copyDirectory(String source, String destination) {
		File sourceFile = new File(source);
		File destinationFile = new File(destination);
		try {
			Files.copy(sourceFile.toPath(), destinationFile.toPath());
			if (sourceFile.isDirectory()) {
				destinationFile.mkdir();
				for (String iter : sourceFile.list()) {
					File sub = new File(sourceFile + File.separator + iter);
					if (sub.isDirectory()) {
						copyDirectory(source + File.separator + iter, destination
								+ File.separator + iter);
					} else
						Files.copy(sub.toPath(), new File(destination + File.separator
								+ iter).toPath());
				}
			} else {
				Files.copy(sourceFile.toPath(), destinationFile.toPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Performs a recursive remove on directory name
	 * 
	 * @param path
	 *            Directory that is to be cleared
	 */
	public void emptyDir(String path) {
		File folder = new File(path);
		if (folder.isDirectory()) {
			for (String s : folder.list()) {
				emptyDir(path + File.separator + s);
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
	private void unzipLocalDir() {
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
	 * @param source
	 *            Zip file
	 * @param destination
	 *            Destination for zip file
	 */
	public void unzip(String source, String destination) {
		try {
			ZipFile zip = new ZipFile(source);
			zip.extractAll(destination);
		} catch (ZipException e) {
			System.err.println("Error in unzip");
		}
	}

	/**
	 * Recursively iterated through file structure folder and unzips and
	 * contained zip files
	 * 
	 * @param path
	 *            Destination of file structure to iterate over
	 */
	private void recurseUnzip(String path) {
		File filePointer = new File(path);
		if (filePointer.isDirectory()) {
			for (String item : filePointer.list()) {
				recurseUnzip(filePointer + File.separator + item);
			}
		} else {
			if (path.endsWith(ZIP_EXTENSION)) {
				String cleanedPath = path.replace(ZIP_EXTENSION, "");
				unzip(path, cleanedPath);
				filePointer.delete();
			}
		}
	}
}