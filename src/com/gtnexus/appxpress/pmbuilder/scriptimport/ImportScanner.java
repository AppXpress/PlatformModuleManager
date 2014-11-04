package com.gtnexus.appxpress.pmbuilder.scriptimport;

import static com.gtnexus.appxpress.AppXpressConstants.IMPORT_FLAG;
import static com.gtnexus.appxpress.AppXpressConstants.LIB;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportScanner {

	private static final String libPath = LIB + File.separator + "%s";
	private static final Pattern startOfBlockComment = Pattern.compile("/[\\*].*");
	private static final Pattern endOfBlockComment = Pattern.compile(".*[\\*]/");
	private static final Pattern singleLineComment = Pattern.compile("//.*");

	/**
	 * Look through top comment and try to find !import statements File name
	 * directly after !import statement will be attempted to be imported into
	 * the current folder
	 * 
	 * @param f
	 *            File to parse
	 */
	public Set<File> parseDoc(final File f) {
		final Set<File> fileNames = new HashSet<>();
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
	public Set<File> scanLine(String line) {
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
