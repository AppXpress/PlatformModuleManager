package com.gtnexus.appxpress.pmbuilder;

import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import com.gtnexus.appxpress.Mapper;

public class AppXpressMapper implements Mapper {

	private final File root;
	private final FileFilter dirsOnly;

	public AppXpressMapper(File root) {
		this.root = root;
		this.dirsOnly = new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
		};
	}

	public void doMapping() {
		if (root == null || !root.isDirectory()) {
			return;
		}
		prepareFoldersForImport();
		createBundles(root);

	}

	private void prepareFoldersForImport() {
		for (File dir : root.listFiles(dirsOnly)) {
			/*
			 * a bunch of logic on when to rename files goes here
			 */
		}
	}

	private void createBundles(File directory) {

	}

	// handleCODesignScripts
	// handleFeF
	//

	/**
	 * 
	 * @param dir
	 *            The CustomObject/designs/scripts directory.
	 */
	private void handleCustomObjectDesignScripts(File dir) {
		for (File subDir : dir.listFiles(dirsOnly)) {
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
