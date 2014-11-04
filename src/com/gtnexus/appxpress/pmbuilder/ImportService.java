package com.gtnexus.appxpress.pmbuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gtnexus.appxpress.Precondition;
import com.gtnexus.appxpress.file.FileService;

/**
 * Parses a file for !import statements
 * 
 * @author Andrew Reynolds
 * @author John Donovan
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class ImportService {

	private final File root;
	private final ImportScanner importScanner;
	private final FileService fs;
	private final Precondition<File> precondition;
	

	public ImportService(final File root) {
		this.root = root;
		this.importScanner = new ImportScanner();
		this.fs = new FileService();
		this.precondition = new Precondition<File>() {
			@Override
			public boolean isMet(File f) {
				return f != null;
			}
		};
		
	}

	// TODO For IDE, compare imported scripts functions with functions in
	// currently folder to ensure no identical, conflicting functions

	/**
	 * Looks recursively through the file structure Searching through the file
	 * structure to find !import statements. It ignores zip, xml , and xsd files
	 * 
	 * @param filePath
	 *            File path to recursively search
	 */
	public void scanAndImport() {
		if (!root.isDirectory()) {
			throw new IllegalArgumentException(root.getAbsolutePath()
					+ " is invalid. filePath "
					+ "must be a valid path to a directory");
		}
		traverse(root);
	}

	/**
	 * Recursively traverses
	 * 
	 * @param f
	 */
	private void traverse(final File f) {
		Set<File> filesToImport = new HashSet<>();
		for (File file : f.listFiles()) {
			if (file.isDirectory()) {
				traverse(file);
			} else if (couldHaveImports(file)) {
				filesToImport.addAll(importScanner.parseDoc(file));
			}
		}
		importFiles(filesToImport, f);
	}

	/**
	 * Checks file by extension to determine if this file should be scanned for
	 * !import statements.
	 * 
	 * @param file
	 *            The file to be checked
	 * @return True if this file should be scanned, else false.
	 */
	private boolean couldHaveImports(final File file) {
		if (file == null || file.isDirectory()) {
			return false;
		}
		// TODO we should maintain a whitelist, not a blacklist.
		final List<String> invalidExtensions = Arrays.asList("zip", "xml",
				"xsd", "xlf");
		String[] splitName = file.getName().split("\\.");
		if (splitName.length > 1
				&& !invalidExtensions.contains(splitName[splitName.length - 1])) {
			return true;
		}
		return false;
	}

	/**
	 * Copies the set of files to a given destination
	 * 
	 * @param filesToImport
	 * @param destinationDirectory
	 */
	private void importFiles(final Set<File> filesToImport,
			final File destinationDirectory) {
		try {
			fs.copyFiles(filesToImport, destinationDirectory, precondition);
		} catch (IOException e) {
			System.err.println("Unable to import files!");
		}
	}

}