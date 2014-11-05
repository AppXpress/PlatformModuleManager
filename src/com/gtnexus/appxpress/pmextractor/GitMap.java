package com.gtnexus.appxpress.pmextractor;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.BUNDLE;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOMER;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_LINK_D1;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_OBJECT_MODULE;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_UI;
import static com.gtnexus.appxpress.AppXpressConstants.DESIGNS;
import static com.gtnexus.appxpress.AppXpressConstants.JS_EXTENSION;
import static com.gtnexus.appxpress.AppXpressConstants.PLATFORM_MODULE_UNZIP_NAME;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPTS;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPT_DESIGN;
import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gtnexus.appxpress.Mapper;
import com.gtnexus.appxpress.ZipService;
import com.gtnexus.appxpress.file.FileService;

/**
 * Accomplishes the following steps
 * <ol>
 * <li>Validates passed in parameters to ensure the files exists and are
 * accessible</li>
 * <li>Unzips the exported platform module</li>
 * <li>Strips the platform module of $</li>
 * <li>Backs up the current local directory</li>
 * <li>Overwrites the current directory with the exported platform module by
 * mapping the exported platform module into the local directory</li>
 * </ol>
 * 
 * @author Andrew Reynolds
 * @author Eric Hu
 * @author John Donovan
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class GitMap implements Mapper {

	private final String platformZip;
	private final String localDir;
	private final String customer;
	private final String platform;
	private final boolean overwriteScripts;
	private final boolean overwriteFef;
	private final List<String> overwrittenScripts;
	private final FileService fs;
	private final ZipService zs;

	File unzippedFile = new File(PLATFORM_MODULE_UNZIP_NAME);

	public static GitMap createMapper(Map<ExtractorOption, String> optionMap) {
		// Preconditions would be good here.
		if (optionMap.containsKey(ExtractorOption.PLATFORM_ZIP)) {
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
		this.fs = new FileService();
		this.zs = new ZipService();
	}

	/**
	 * Performs the appropriate actions for module extraction
	 */
	public void doMapping() {
		// Ensure path exists - GIT repo is set up correctly
		// can handle this with preconditions?
		if (!validateAndEnsurePathExists(localDir, customer, platform)) {
			System.err.println("ERROR: Paths are not set up properly!");
			return;
		}
		if (!isFolder(localDir) || !exists(platformZip)) {
			System.err
					.println("ERROR: One of the following paths is invalid:\n\t"
							+ localDir + "\n\t" + platformZip);
			return;
		}
		cleanup();
		unzipPlatformZip(); // TODO if we fail here go ahead and quit.
		makeHumanReadable(unzippedFile);
		backup();
		clearCustomLinksXML();
		mapCustomObjectDesign();
		mapFolders(PLATFORM_MODULE_UNZIP_NAME, buildCustomerPath());
		if (overwriteScripts) {
			printOverwrittenScripts();
		}
	}

	private void cleanup() {
		if (unzippedFile.exists()) {
			fs.emptyDir(unzippedFile); // TODO i dont think we wanna delete it
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
		return localDir + File.separator + CUSTOMER + File.separator + customer
				+ File.separator + platform;
	}

	/**
	 * Unzips file 'folder' into PlatModX
	 */
	private void unzipPlatformZip() {
		try {
			File source = new File(platformZip);
			if (source.exists()) {
				zs.unzip(source, unzippedFile);
			} else {
				System.out.println("Cannot find folder!");
			}
		} catch (Exception e) {
			System.err.println("Error in UnzipExport.run");
		}
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
			fs.emptyDir(dir); // TODO we shouldnt whipe out the top level dir
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
	 * This method moves their location and removes $ from their names
	 * 
	 * @param path
	 *            Path of Unzipped Exported platform module
	 */
	private void mapCustomObjectDesign() {
		Path scriptsPath = unzippedFile.toPath().resolve(CUSTOM_OBJECT_MODULE)
				.resolve(DESIGNS).resolve(SCRIPTS);
		if (!Files.exists(scriptsPath)) {
			System.out.println("Cannot find script folder: "
					+ scriptsPath.toString());
			System.out
					.println("\tAssuming this module contains no custom objects.");
			return;
		}
		File scripts = scriptsPath.toFile();
		for (File co : scripts.listFiles()) {
			if (co.isDirectory()) {
				fs.renameFile(co, co.getName().replace(SCRIPT_DESIGN + $, ""));
			} else {
				String dirName = co.getName().replace(SCRIPT_DESIGN + $, "")
						.replace(JS_EXTENSION, "");
				Path p = unzippedFile.toPath().resolve(dirName);
				try {
					if (!Files.exists(p)) {
						Files.createDirectory(p);
					}
					Path destination = p.resolve(co.getName());
					Files.move(co.toPath(), destination, REPLACE_EXISTING);
				} catch (IOException e) {
					System.err.println("Failed to map custom object design: " + co.getName());
				}
			}
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
	private boolean validateAndEnsurePathExists(String folder, String sub,
			String subCo) {
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
	 * Prints the scripts that were overwritten so the user can quickly noticed
	 * if unwanted actions were performed on local git dir
	 */
	private void printOverwrittenScripts() {
		final StringBuilder sb = new StringBuilder();
		System.out.print("You over wrote these scripts -> ");
		for (String script : overwrittenScripts) {
			sb.append(script).append(", ");
		}
		System.out.println(sb.substring(0, sb.length() - 2));
	}

	/**
	 * Backs up folder/customer/cust/plat into a folder called PM_Git_Backup
	 */
	private void backup() {
		String path = buildCustomerPath();
		String backup = "PM_Git_Backup" + File.separator + platform;
		File bkpFile = new File(backup);
		try {
			fs.copyDirectory(new File(path), bkpFile);
		} catch (IOException e) {
			System.err.println("error backing up -> " + e);
		}
	}

	/**
	 * Cleans folder structure of $ signs
	 * 
	 * @param folderName
	 *            Location of file structure
	 */
	private void makeHumanReadable(File f) {
		if (!f.exists()) {
			System.err.println("Cannot find folder -> " + f.getName());
			return;
		}
		if (f.isDirectory()) {
			for (File s : f.listFiles()) {
				makeHumanReadable(s);
			}
		}
		if (f.getName().contains($)) {
			fs.renameFile(f, f.getName().replace($, ""));
		}
	}

}