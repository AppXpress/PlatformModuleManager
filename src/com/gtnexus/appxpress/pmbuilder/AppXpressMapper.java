package com.gtnexus.appxpress.pmbuilder;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_LINK_D1;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_OBJECT_MODULE;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_UI;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPTS;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPT_DESIGN;
import static com.gtnexus.appxpress.AppXpressConstants.TYPE_EXTENSION_D1;
import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import com.gtnexus.appxpress.Mapper;

public class AppXpressMapper implements Mapper {

	private final File root;

	public AppXpressMapper(File root) {
		this.root = root;
	}

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

	private void prependToName(List<File> files, String prepend) {
		for (File file : files) {
			prependToName(file, prepend);
		}
	}

	private void prependToName(File file, String prepend) {
		renameFile(file, prepend + file.getName());
	}

	private void renameFile(File file, String newName) {
		try {
			Files.move(file.toPath(), file.toPath().resolve(newName));
		} catch (IOException e) {
			System.err.println("Exception when trying to rename "
					+ file.getName());
		}
	}

	private void renameSetOfFiles(List<File> files, String toReplace,
			String replacement) {
		for (File file : files) {
			String fileName = file.getName();
			renameFile(file, fileName.replace(toReplace, replacement));
		}
	}

	// TODO refactor me! But I am better than before :)
	private void fixCustomObjectModule(File directory) {
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
		} else {
			// jsbogie
			
		}
	}
	
	private boolean isFileType(File file, String mimeType) {
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
		for (File file : dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File current, String fileName) {
				return !fileName.endsWith(ZIP_EXTENSION);
			}

		})) {
			// zip dir and then delete
		}
	}

}
