package com.gtnexus.appxpress.pmbuilder;

import static com.gtnexus.appxpress.AppXpressConstants.IMPORT_FLAG;
import static com.gtnexus.appxpress.AppXpressConstants.LIB;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gtnexus.appxpress.file.FileFilterFactory;
import com.gtnexus.appxpress.file.ChainedAnd;

/**
 * Parses a file for !import statements
 * 
 * @author Andrew Reynolds
 * @author John Donovan
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class ImportScanner {

	private final File root;
	private final String libPath = LIB + File.separator + "%s";

	public ImportScanner(final File root) {
		this.root = root;
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
				filesToImport.addAll(parseDoc(file));
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
		if(file == null || file.isDirectory()) {
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
		for (File libFile : filesToImport) {
			if (!libFile.exists()) {
				System.err.println("A script in "
						+ destinationDirectory.getAbsolutePath()
						+ " includes an import directive for "
						+ libFile.getName()
						+ ". No such file can be found in the lib.");
				continue;
			}
			importFile(libFile, destinationDirectory);
		}
	}

	/**
	 * Copies single file to the destination. {@link #importFiles(Set, File)}
	 * 
	 * @param source
	 *            The source file, to be copied.
	 * @param destinationDir
	 *            The destination directory where the file will be copied to.
	 */
	private void importFile(final File source, final File destinationDir) {
		Path sourcePath = source.toPath();
		Path destinationPath = destinationDir.toPath().resolve(
				sourcePath.getFileName());
		try {
			Files.copy(sourcePath, destinationPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	private Set<File> parseDoc(final File f) {
		final Set<File> fileNames = new HashSet<>();
		final Pattern startOfBlockComment = Pattern.compile("/[\\*].*");
		final Pattern endOfBlockComment = Pattern.compile(".*[\\*]/");
		final Pattern singleLineComment = Pattern.compile("//.*");
		try {
			final Scanner scan = new Scanner(f);
			String line;
			boolean lineIsInBlock = false;
			while (scan.hasNextLine()) {
				//TODO this logic is foobar. Clean this up!
				line = scan.nextLine();
				Matcher startCommentMatcher = startOfBlockComment.matcher(line);
				Matcher endCommentMatcher = endOfBlockComment.matcher(line);
				Matcher singleLineCommentMatcher = singleLineComment
						.matcher(line);
				if (startCommentMatcher.find()) {
					lineIsInBlock = true;
				}
				if (lineIsInBlock || singleLineCommentMatcher.find()) {
					fileNames.addAll(scanLine(line));
				}
				if (endCommentMatcher.find()) {
					lineIsInBlock = false;
				}
				if (!(lineIsInBlock || singleLineCommentMatcher.find() || 
						!line.equals(""))) {
					break;
				}
			}
			scan.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileNames;
	}

	/**
	 * Looks through a line for the !import statement. If !import is found, adds
	 * the following word, which should be a name of a js script located in lib
	 * folder, and adds it to importFiles
	 * 
	 * @param line
	 *            Line of a file
	 */
	private Set<File> scanLine(String line) {
		if (line == null || line.isEmpty()) {
			return Collections.emptySet();
		}
		final Set<File> libFiles = new HashSet<>();
		line = line.replaceAll(",", " ");
		if (line.contains(IMPORT_FLAG)) {
			final String[] fileNames = line.split("//w+");
			boolean importFlagFound = false;
			for (String fileName : fileNames) {
				if (importFlagFound && !fileName.isEmpty()) {
					libFiles.add(new File(libFilePathFor(fileName)));
				}
				if (fileName.equals(IMPORT_FLAG)) {
					importFlagFound = true;
				}
			}
		}
		return libFiles;
	}

	private String libFilePathFor(final String fileName) {
		return String.format(libPath, fileName);
	}
}