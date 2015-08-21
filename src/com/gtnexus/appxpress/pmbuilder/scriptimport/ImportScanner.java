package com.gtnexus.appxpress.pmbuilder.scriptimport;

import static com.gtnexus.appxpress.commons.AppXpressConstants.IMPORT_FLAG;

import java.io.Reader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportScanner {

	private static final Pattern startOfBlockComment = Pattern.compile("/[\\*].*");
	private static final Pattern endOfBlockComment = Pattern.compile(".*[\\*]/");
	private static final Pattern singleLineComment = Pattern.compile("//.*");

	
	public Set<String> parseDoc(final Reader fileContents) {
		final Set<String> fileNames = new HashSet<>();
		try {
			final Scanner scan = new Scanner(fileContents);
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
	public Set<String> scanLine(String line) {
		if(line == null) {
			throw new NullPointerException("Cannot scan null line");
		}
		if (line.isEmpty()) {
			return Collections.emptySet();
		}
		final Set<String> libFiles = new HashSet<>();
		line = line.replaceAll(",", " ");
		if (line.contains(IMPORT_FLAG)) {
			final String[] fileNames = line.split("\\s+");
			boolean importFlagFound = false;
			for (String fileName : fileNames) {
				if (importFlagFound && !fileName.isEmpty()) {
					libFiles.add(fileName);
				}
				if (isImportFlag(fileName)) {
					importFlagFound = true;
				}
			}
		}
		return libFiles;
	}

	private boolean isImportFlag(String fileName) {
		if (fileName.equals(IMPORT_FLAG)){
			return true;
		}
		if(fileName.length() >= 2) {
			return fileName.substring(2, fileName.length()).equals(IMPORT_FLAG);
		}
		return false;
	}
	
}
