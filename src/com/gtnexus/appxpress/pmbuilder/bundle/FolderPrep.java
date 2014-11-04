package com.gtnexus.appxpress.pmbuilder.bundle;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_LINK_D1;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_OBJECT_MODULE;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_UI;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPTS;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPT_DESIGN;
import static com.gtnexus.appxpress.AppXpressConstants.TYPE_EXTENSION_D1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import com.gtnexus.appxpress.file.FileFilterFactory;
import com.gtnexus.appxpress.file.FilterChain;

public class FolderPrep implements Preparation {

	/**
	 * Renames directories, and handle's special cases for specific directories.
	 */
	@Override
	public void prepare(File root) {
		for (File dir : root.listFiles(FileFilterFactory.directoriesOnly())) {
			// TODO this is whack yo. directly ported from original
			String directoryName = dir.getName();
			if (directoryName.endsWith(CUSTOM_LINK_D1)) {
				renameFile(dir, CUSTOM_LINK_D1);
			} else if (directoryName.endsWith(TYPE_EXTENSION_D1)) {
				renameFile(dir, $ + TYPE_EXTENSION_D1);
			} else if (directoryName.endsWith(CUSTOM_OBJECT_MODULE)) {
				fixCustomObjectModule(dir);
			} else if (directoryName.endsWith(CUSTOM_UI)) {
				prependToName(Arrays.asList(dir.listFiles()), $);
			}
		}
	}
	
	/**
	 * 
	 * @param directory
	 */
	private void fixCustomObjectModule(File directory) {
		// TODO refactor me! But I am better than before :)
		File designFolder = new File(directory.getAbsolutePath()
				+ File.separator + "designs");
		if (designFolder.exists()) {
			renameSetOfFiles(Arrays.asList(designFolder.listFiles()),
					"Design_", "Design_$");
		}
		File scriptFolder = new File(directory.getAbsolutePath()
				+ File.separator + SCRIPTS);
		if (scriptFolder.exists()) {
			File[] files = scriptFolder.listFiles(new FilterChain(
					FileFilterFactory.directoriesOnly(), 
					FileFilterFactory.FileNameDoesNotContain("_$")));
			renameSetOfFiles(Arrays.asList(files), 
					SCRIPT_DESIGN, SCRIPT_DESIGN + $);
		}
		File xsdFolder = new File(directory.getAbsoluteFile() + File.separator
				+ "xsd");
		if (xsdFolder.exists()) {
			prependToName(Arrays.asList(xsdFolder.listFiles()), $);
		}
	}
	
	/**
	 * Prepends a String to the front of a single file.
	 * 
	 * @param file
	 *            The file whose name will be altered.
	 * @param prepend
	 *            The String to prepend to the file's name.
	 */
	private void prependToName(File file, String prepend) {
		renameFile(file, prepend + file.getName());
	}
	
	/**
	 * Prepends a String to the the names of a list of a files.
	 * 
	 * @param files
	 *            The list of files who's name will be altered.
	 * @param prepend
	 *            The String to prepend to each file name.
	 */
	private void prependToName(List<File> files, String prepend) {
		for (File file : files) {
			prependToName(file, prepend);
		}
	}
	
	/**
	 * Rename a single file
	 * 
	 * @param file
	 *            The file whose name will be altered.
	 * @param newName
	 *            The files new name.
	 */
	private void renameFile(File file, String newName) {
		try {
			Files.move(file.toPath(), file.toPath().resolveSibling(newName));
		} catch (IOException e) {
			System.err.println("Exception when trying to rename "
					+ file.getName());
		}
	}
	
	/**
	 * Renames a set of files by replacing a matched string with some
	 * replacement.
	 * 
	 * @param files
	 *            The set of file's whose names will be altered if they match
	 *            the replacement string.
	 * @param toReplace
	 *            The string that will be replaced if found in any file names.
	 * @param replacement
	 *            The string that will replace the found string in the file
	 *            name.
	 */
	private void renameSetOfFiles(List<File> files, String toReplace,
			String replacement) {
		for (File file : files) {
			String fileName = file.getName();
			renameFile(file, fileName.replace(toReplace, replacement));
		}
	}

}
