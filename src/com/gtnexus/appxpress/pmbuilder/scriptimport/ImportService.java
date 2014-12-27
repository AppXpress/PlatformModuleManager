package com.gtnexus.appxpress.pmbuilder.scriptimport;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.commons.Precondition;
import com.gtnexus.appxpress.commons.file.FileService;

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
		this.root = new ImportFile(root);
		this.importScanner = new ImportScanner();
		this.fs = new FileService();
		this.precondition = new Precondition<File>() {
			/**
			 * This precondition is here as a guard. Some File objects are
			 * created via manual appending of path Strings. This ensures we do
			 * not try to copy an object that does not exist.
			 */
			@Override
			public boolean isMet(File f) {
				return f != null && f.exists();
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
	 * @throws AppXpressException 
	 */
	public void scanAndImport() throws AppXpressException {
		if (!root.isDirectory()) {
			throw new AppXpressException(root.getAbsolutePath()
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
		if(filesToImport.size() > 0) {
			importFiles(filesToImport, f);
		}
	}

	/**
	 * Copies the set of files to a given destination
	 * 
	 * @param filesToImport
	 * @param destinationDirectory
	 */
	private void importFiles(final Set<File> filesToImport,
			final File destinationDirectory) {
		System.out.println(filesToImport.size() + " imports directives found. Importing now.");
		try {
			fs.copyFiles(filesToImport, destinationDirectory, precondition);
		} catch (IOException e) {
			System.err.println("Unable to import files!");
		}
	}

}