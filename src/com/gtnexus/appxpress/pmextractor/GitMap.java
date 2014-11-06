package com.gtnexus.appxpress.pmextractor;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.BUNDLE;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOMER;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gtnexus.appxpress.Mapper;
import com.gtnexus.appxpress.Preparation;
import com.gtnexus.appxpress.ZipService;
import com.gtnexus.appxpress.file.FileService;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

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

	private final GitMapVO vo;
	private final List<String> overwrittenScripts;
	private final Preparation<GitMapVO> prep;
	private final FileService fs;

	public static GitMap createMapper(Map<ExtractorOption, String> optionMap) {
		// Preconditions would be good here.
		if (optionMap.containsKey(ExtractorOption.PLATFORM_ZIP)) {
			String platformZip = optionMap.get(ExtractorOption.PLATFORM_ZIP);
			if (!platformZip.endsWith(ZIP_EXTENSION)) {
				platformZip = platformZip + ZIP_EXTENSION;
				optionMap.put(ExtractorOption.PLATFORM_ZIP, platformZip);
			}
		}
		return new GitMap(new GitMapVO(optionMap));
	}

	public GitMap(GitMapVO vo) {
		this.vo = vo;
		this.overwrittenScripts = new ArrayList<>();
		this.prep = new GitMapPrep();
		this.fs = new FileService();
	}

	/**
	 * Performs the appropriate actions for module extraction
	 */
	@Override
	public void doMapping() {
		try {
			prep.prepare(vo);
			mapCustomObjectDesign();
			mapFolders(PLATFORM_MODULE_UNZIP_NAME, vo.getPlatformDir().getName());
			if (vo.isOverwriteScripts()) {
				printOverwrittenScripts();
			}
		} catch (PMBuilderException e) {

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
		
		for(File child : platform.listFiles()) {
			if(child.isDirectory()) {
				
			} else {
				
			}
		}
		
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
		File  pc =  destinationCompanyFolder
				.toPath()
				.resolve(platformChildName)
				.toFile();
		if (pc.exists()) {
			if (vo.isOverwriteFef() || !platformChildName.equals(CUSTOM_UI)) {
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
		if (vo.isOverwriteScripts()) {
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
		Path scriptsPath = vo.getUnzipDir().toPath()
				.resolve(CUSTOM_OBJECT_MODULE).resolve(DESIGNS)
				.resolve(SCRIPTS);
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
				Path p = vo.getUnzipDir().toPath().resolve(dirName);
				try {
					if (!Files.exists(p)) {
						Files.createDirectory(p);
					}
					Path destination = p.resolve(co.getName());
					Files.move(co.toPath(), destination, REPLACE_EXISTING);
				} catch (IOException e) {
					System.err.println("Failed to map custom object design: "
							+ co.getName());
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

}