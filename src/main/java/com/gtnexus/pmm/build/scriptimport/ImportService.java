package com.gtnexus.pmm.build.scriptimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.gtnexus.pmm.api.v100.command.SubCommandException;
import com.gtnexus.pmm.api.v100.service.FileService;
import com.gtnexus.pmm.common.HasPrerequisite;
import com.gtnexus.pmm.common.file.LibResourceToPath;
import com.gtnexus.pmm.service.FileServiceImpl;

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
    private final HasPrerequisite<File> precondition;
    private final Path libPath;

    public ImportService(final File root, final Path libPath) {
	this.root = new ImportFile(root);
	this.libPath = libPath;
	this.importScanner = new ImportScanner();
	this.fs = new FileServiceImpl();
	this.precondition = new HasPrerequisite<File>() {
	    /**
	     * This precondition is here as a guard. Some File objects are created via
	     * manual appending of path Strings. This ensures we do not try to copy an
	     * object that does not exist.
	     */
	    @Override
	    public boolean isMet(File f) {
		return f != null && f.exists();
	    }
	};
    }

    /**
     * Looks recursively through the file structure Searching through the file
     * structure to find !import statements. It ignores zip, xml , and xsd files
     * 
     * @param filePath
     *            File path to recursively search
     * @throws SubCommandException
     */
    public void scanAndImport() throws SubCommandException {
	if (!root.isDirectory()) {
	    throw new SubCommandException(
		    root.getAbsolutePath() + " is invalid. filePath " + "must be a valid path to a directory");
	}
	traverse(root);
    }

    /**
     * Recursively traverses
     * 
     * @param f
     */
    private void traverse(final ImportFile f) {
	Set<String> fileNamesForImport = new HashSet<>();
	for (ImportFile file : f.listFiles()) {
	    if (file.isDirectory()) {
		traverse(file);
	    } else if (file.couldHaveImports()) {
		fileNamesForImport.addAll(readInImports(file));
	    }
	}
	if (fileNamesForImport.size() > 0) {
	    importFiles(fileNamesForImport, f);
	}
    }

    private Set<String> readInImports(ImportFile file) {
	try {
	    BufferedReader reader = new BufferedReader(new FileReader(file));
	    return importScanner.parseDoc(reader);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	return Collections.emptySet();
    }

    /**
     * Copies the set of files to a given destination
     * 
     * @param filesToImport
     * @param destinationDirectory
     */
    private void importFiles(final Set<String> filesToImport, final File destinationDirectory) {
	System.out.println(filesToImport.size() + " import(s) directives found.");
	try {
	    fs.copyFiles(filesToImport, new LibResourceToPath(libPath), destinationDirectory, precondition);
	} catch (IOException e) {
	    System.err.println("Unable to import files!");
	}
    }

}