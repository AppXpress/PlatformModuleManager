package com.gtnexus.appxpress.pmextractor.gitmap;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_OBJECT_MODULE;
import static com.gtnexus.appxpress.AppXpressConstants.DESIGNS;
import static com.gtnexus.appxpress.AppXpressConstants.JS_EXTENSION;
import static com.gtnexus.appxpress.AppXpressConstants.PLATFORM_MODULE_UNZIP_NAME;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPTS;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPT_DESIGN;
import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.Mapper;
import com.gtnexus.appxpress.Preparation;
import com.gtnexus.appxpress.file.FileService;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;

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
public class GitMapper implements Mapper {

	private final GitMapVO vo;
	private List<Path> overwrittenScripts;
	private final Preparation<GitMapVO> prep;
	private final FileService fs;

	public static GitMapper createMapper(Map<ExtractorOption, String> optionMap) {
		// Preconditions would be good here.
		if (optionMap.containsKey(ExtractorOption.PLATFORM_ZIP)) {
			String platformZip = optionMap.get(ExtractorOption.PLATFORM_ZIP);
			if (!platformZip.endsWith(ZIP_EXTENSION)) {
				platformZip = platformZip + ZIP_EXTENSION;
				optionMap.put(ExtractorOption.PLATFORM_ZIP, platformZip);
			}
		}
		return new GitMapper(new GitMapVO(optionMap));
	}

	public GitMapper(GitMapVO vo) {
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
			mapFolders(new File(PLATFORM_MODULE_UNZIP_NAME), vo.getPlatformDir());
			if (vo.isOverwriteScripts()) {
				printOverwrittenScripts();
			}
		} catch (AppXpressException e) {

		}
	}

	/**
	 * Map folder export to folder destination
	 * 
	 * @param exportedPlatform
	 *            Folder where unzipped exported platform module is
	 * @param destinationPlatform
	 *            Folder where platform module is in local git directory
	 */
	private void mapFolders(File exportedPlatform, File destinationPlatform) {
		GitMapVisitor visitor = new GitMapVisitor(vo,
				exportedPlatform.toPath(), destinationPlatform.toPath());
		try {
			Files.walkFileTree(exportedPlatform.toPath(), visitor);
			overwrittenScripts = visitor.getOverwrittenScripts();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 * Prints the scripts that were overwritten so the user can quickly noticed
	 * if unwanted actions were performed on local git dir
	 */
	private void printOverwrittenScripts() {
		final StringBuilder sb = new StringBuilder();
		System.out.print("You over wrote these scripts -> ");
		for (Path script : overwrittenScripts) {
			sb.append(script.toString()).append(",\n");
		}
		System.out.println(sb.substring(0, sb.length() - 2));
	}

}