package com.gtnexus.appxpress.pmbuilder;

import static com.gtnexus.appxpress.AppXpressConstants.IMPORT_FLAG;
import static com.gtnexus.appxpress.AppXpressConstants.LIB;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a file for !import statements
 * 
 * @author Andrew Reynolds
 * @author John Donovan
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class ImportScanner {

	private ArrayList<String> importFiles;

	public ImportScanner() {
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
	public void search(String filePath) {
		File topFolder = new File(filePath);
		if (!topFolder.isDirectory()) {
			throw new IllegalArgumentException(filePath
					+ " is invalid. filePath "
					+ "must be a valid path to a directory");
		}
		for (File file : topFolder.listFiles()) {
			if (file.isDirectory()) {
				search(file.getAbsolutePath());
			} else if (isForImport(file)) {
				for (String s : parseDoc(file)) {
					importFile(s, filePath);
				}
			}
		}
	}

	private boolean isForImport(File file) {
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
	 * Imports file 'file' from lib folder into location
	 * 
	 * @param file
	 *            Name of file to import - file found in /lib
	 * @param location
	 *            Name of current location to import lib file
	 */
	private void importFile(String file, String location) {
		File source = new File(LIB + File.separator + file);
		if (!source.exists()) {
			System.out.println("Failed to import " + source.getName()
					+ " - File not Found");
			return;
		}
		File destination = new File(location + File.separator + file);
		try {
			if (!destination.exists()) {
				Files.copy(source.toPath(), destination.toPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Look through top comment and try to find !import statements File name
	 * directly after !import statement will be attempted to be imported into
	 * the current folder
	 * 
	 * @param f
	 *            File to parse
	 */
	private ArrayList<String> parseDoc(File f) {
		importFiles = new ArrayList<String>();
		try {
			Scanner scan = new Scanner(f);
			String readline;
			boolean commentStart = false;
			while (scan.hasNextLine()) {
				readline = scan.nextLine();
				Pattern startBlock = Pattern.compile("/[\\*].*");
				Pattern singleLine = Pattern.compile("//.*");
				Pattern endBlock = Pattern.compile(".*[\\*]/");
				Matcher m1 = startBlock.matcher(readline);
				Matcher m2 = singleLine.matcher(readline);
				Matcher m3 = endBlock.matcher(readline);
				if (m1.find()) {
					commentStart = true;
				}
				if (commentStart || m2.find()) {
					scanLine(readline);
				}
				if (m3.find()) {
					commentStart = false;
				}
				// On first line not blank that is not a comment, stop looking
				// for import statement
				if (!(commentStart || m2.find() || !readline.equals(""))) {
					break;
				}
				// TODO importFiles is always an empty list. Thus, no files will
				// ever be imported @see search()
			}
			scan.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return importFiles;
	}

	/**
	 * Looks through a line for the !import statement. If !import is found, adds
	 * the following word, which should be a name of a js script located in lib
	 * folder, and adds it to importFiles
	 * 
	 * @param line
	 *            Line of a file
	 */
	private void scanLine(String line) {
		line = line.replaceAll(",", " ");
		if (line.contains(IMPORT_FLAG)) {
			String[] words = line.split(" ");
			boolean rdyImport = false;
			for (int i = 0; i < words.length; i++) {
				if (rdyImport) {
					importFiles.add(words[i]);
				}
				if (words[i].contains(IMPORT_FLAG)) {
					rdyImport = true;
				}
			}
		}
	}
}