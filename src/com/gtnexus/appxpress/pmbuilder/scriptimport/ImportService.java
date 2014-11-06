package com.gtnexus.appxpress.pmbuilder.scriptimport;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
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

	private final ImportFile root;
	private final ImportScanner importScanner;
	private final FileService fs;
	private final Precondition<File> precondition;
	

	public ImportService(final File root) {
		this.root = (ImportFile)root;
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
	private void traverse(final ImportFile f) {
		Set<File> filesToImport = new HashSet<>();
		for (ImportFile file : f.listFiles()) {
			if (file.isDirectory()) {
				traverse(file);
			} else if (file.couldHaveImports()) {
				filesToImport.addAll(importScanner.parseDoc(file));
			}
		}
		importFiles(filesToImport, f);
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