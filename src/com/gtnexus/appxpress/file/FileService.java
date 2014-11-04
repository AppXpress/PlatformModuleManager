package com.gtnexus.appxpress.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;

import com.gtnexus.appxpress.Precondition;

public class FileService {

	/**
	 * Prepends a String to the front of a single file.
	 * 
	 * @param file
	 *            The file whose name will be altered.
	 * @param prepend
	 *            The String to prepend to the file's name.
	 */
	public void prependToName(File file, String prepend) {
		renameFile(file, prepend + file.getName());
	}

	/**
	 * Prepends a String to the the names of a collection of a files.
	 * 
	 * @param files
	 *            The list of files who's name will be altered.
	 * @param prepend
	 *            The String to prepend to each file name.
	 */
	public void prependToName(Collection<File> files, String prepend) {
		for (File file : files) {
			prependToName(file, prepend);
		}
	}

	/**
	 * Rename a single file
	 * 
	 * @param file
	 *            The file whose name will be altered.
	 * @param newName
	 *            The files new name.
	 */
	public void renameFile(File file, String newName) {
		try {
			Files.move(file.toPath(), file.toPath().resolveSibling(newName));
		} catch (IOException e) {
			System.err.println("Exception when trying to rename "
					+ file.getName());
		}
	}

	/**
	 * Renames a set of files by replacing a matched string with some
	 * replacement.
	 * 
	 * @param files
	 *            The set of file's whose names will be altered if they match
	 *            the replacement string.
	 * @param toReplace
	 *            The string that will be replaced if found in any file names.
	 * @param replacement
	 *            The string that will replace the found string in the file
	 *            name.
	 */
	public void renameSetOfFiles(Collection<File> files, String toReplace,
			String replacement) {
		for (File file : files) {
			String fileName = file.getName();
			renameFile(file, fileName.replace(toReplace, replacement));
		}
	}

	public void moveFiles(final Collection<File> files, final File destination)
			throws IOException {
		for (File file : files) {
			Path p = destination.toPath().resolve(file.getName());
			Files.move(file.toPath(), p, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	public void copyFiles(final Collection<File> files, final File destination,
			Precondition<File> precondition) throws IOException {
		for (File file : files) {
			if(precondition.isMet(file)) {
				Path p = destination.toPath().resolve(file.getName());
				Files.copy(file.toPath(), p, StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

	public boolean isFileType(final File file, final String mimeType) {
		// can we rely on mime types? I would like to see what happens
		// there are a few different types for .zip, and .js still cannot
		// be clearly identified by mime type.
		// perhaps build mime type table and then fall back on file extension
		//
		return false;
	}

	public void emptyDir(final File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				emptyDir(f);
			}
		}
		file.delete();
	}

}
