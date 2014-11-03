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
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.gtnexus.appxpress.Mapper;
import com.gtnexus.appxpress.file.FileFilterFactory;
import com.gtnexus.appxpress.file.FilterChain;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

/**
 * Replacement for PlatfromMapUtil.
 * 
 * Takes a git repository and maps to a file structure that can be imported onto
 * the platform.
 * 
 * @author jdonovan
 *
 */
public class AppXpressMapper implements Mapper {

	private final File root;
	private final ZipUtility zu;

	public AppXpressMapper(File root) {
		this.root = root;
		this.zu = new ZipUtility();
	}

	/**
	 * Performs the directory mappings (renaming, moving, etc.)
	 */
	public void doMapping() {
		if (root == null || !root.isDirectory()) {
			System.err.println("Module root cannot be found, or is empty. "
					+ "No mapping can be performed.");
			return;
		}
		prepareFoldersForImport();
		createBundles(root);
	}

	private void prepareFoldersForImport() {
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
					FileFilterFactory.directoriesOnly(), FileFilterFactory
							.FileNameDoesNotContain("_$")));
			renameSetOfFiles(Arrays.asList(files), SCRIPT_DESIGN, SCRIPT_DESIGN
					+ $);
		}
		File xsdFolder = new File(directory.getAbsoluteFile() + File.separator
				+ "xsd");
		if (xsdFolder.exists()) {
			prependToName(Arrays.asList(xsdFolder.listFiles()), $);
		}
	}

	private void createBundles(File directory) {
		for (File f : directory.listFiles(FileFilterFactory.directoriesOnly())) {
			searchForPotentialBundles(f);
		}
	}

	private void searchForPotentialBundles(File dir) {
		final String platformIndependent = dir.getAbsolutePath()
				+ File.separator + "designs" + File.separator + "scripts";
		if (dir.getAbsolutePath().toLowerCase().contains(platformIndependent)) {
			handleCustomObjectDesignScripts(dir);
		} else if (dir.getName().endsWith("customUi")) {
			handleFef(dir);
		}
		List<File> jsFiles = new LinkedList<>();
		for (File f : dir.listFiles()) {
			if (isFileType(f, "js")) {
				jsFiles.add(f);
			} else if (f.isDirectory()) {
				searchForPotentialBundles(dir);
			}
		}
		if (jsFiles.size() > 1) {
			File bundleFolder = createBundleFolder(dir);
			try {
				moveFiles(jsFiles, bundleFolder);
				zu.zipDirectory(bundleFolder);
			} catch (PMBuilderException | IOException e) {
				e.printStackTrace();
			}
			//TODO empty this dir
		}

	}

	private void moveFiles(List<File> files, File destination)
			throws IOException {
		for (File file : files) {
			Path p = destination.toPath().resolve(file.getName());
			Files.move(file.toPath(), p, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	private File createBundleFolder(File parent) {
		String bundleFolderName = initCap(parent.getName() + BUNDLE);
		File bundle = new File(parent, bundleFolderName);
		bundle.mkdir();
		return bundle;
	}

	/**
	 * Capitalize the first letter in a string.
	 * 
	 * @param string
	 * @return
	 */
	private String initCap(String string) {
		return string.substring(0, 1).toUpperCase().concat(string.substring(1));
	}

	private boolean isFileType(File file, String mimeType) {
		// can we rely on mime types? I would like to see what happens
		// there are a few different types for .zip, and .js still cannot
		// be clearly identified by mime type.
		// perhaps build mime type table and then fall back on file extension
		//

		return false;
	}

	// handleCODesignScripts
	//

	/**
	 * @param dir
	 *            The CustomObject/designs/scripts directory.
	 */
	private void handleCustomObjectDesignScripts(File dir) {
		for (File subDir : dir.listFiles(FileFilterFactory.directoriesOnly())) {
			handleSingleCODScript(subDir);
		}
	}

	private void handleSingleCODScript(File dir) {
		if (dir.list().length == 1) {
			
		} else if (dir.list().length > 1) {

		}
	}

	private void handleFef(File dir) {
		final FilterChain filter = new FilterChain(
				FileFilterFactory.directoriesOnly(),
				FileFilterFactory.endsWith(ZIP_EXTENSION));
		for (File file : dir.listFiles(filter)) {
			try {
				zu.zipDirectory(file);
			} catch (PMBuilderException e) {
				System.err.println(e.getMessage());
			}
			// TODO get rid of folder, no longer needed.
		}
	}

}
