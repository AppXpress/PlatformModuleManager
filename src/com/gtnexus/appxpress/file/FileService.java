package com.gtnexus.appxpress.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.gtnexus.appxpress.Precondition;

/**
 * 
 * @author jdonovan
 *
 */
public class FileService {

	/**
	 * Prepends a String to the front of a single file.
	 * 
	 * @param file
	 *            The file whose name will be altered.
	 * @param prepend
	 *            The String to prepend to the file's name.
	 */
	public Path prependToName(File file, String prepend) {
		return renameFile(file, prepend + file.getName());
	}

	/**
	 * Prepends a String to the the names of a collection of a files.
	 * 
	 * @param files
	 *            The list of files who's name will be altered.
	 * @param prepend
	 *            The String to prepend to each file name.
	 */
	public List<Path> prependToName(Collection<File> files, String prepend) {
		List<Path> paths = new LinkedList<>();
		for (File file : files) {
			paths.add(prependToName(file, prepend));
		}
		return paths;
	}

	/**
	 * Rename a single file
	 * 
	 * @param file
	 *            The file whose name will be altered.
	 * @param newName
	 *            The files new name.
	 */
	public Path renameFile(File file, String newName) {
		Path path = null;
		try {
			path = Files.move(file.toPath(), 
					file.toPath().resolveSibling(newName));
		} catch (IOException e) {
			System.err.println("Exception when trying to rename "
					+ file.getName());
		}
		return path;
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
	public List<Path> renameSetOfFiles(Collection<File> files, String toReplace,
			String replacement) {
		if (files == null) {
			throw new NullPointerException(
					"Files cannot be null.");
		}
		if(toReplace == null || replacement == null) {
			throw new NullPointerException("Replacement Strings cannot be null.");
		}
		List<Path> paths = new LinkedList<>();
		for (File file : files) {
			String fileName = file.getName();
			Path result = renameFile(file, fileName.replace(toReplace, replacement));
			paths.add(result);
		}
		return paths;
	}

	public List<Path> moveFiles(final Collection<File> files, final File destination)
			throws IOException {
		if (files == null || destination == null) {
			throw new NullPointerException(
					"files and destination cannot be null.");
		}
		List<Path> paths = new LinkedList<>();
		for (File file : files) {
			Path p = destination.toPath().resolve(file.getName());
			Path result = Files.move(file.toPath(), p, StandardCopyOption.REPLACE_EXISTING);
			paths.add(result);
		}
		return paths;
	}

	public List<Path> copyFiles(final Collection<File> files,
			final File destination, Precondition<File> precondition)
			throws IOException {
		if (files == null || destination == null) {
			throw new NullPointerException(
					"files and destination cannot be null.");
		}
		if (precondition == null) {
			precondition = new Precondition.EmptyCondition<>();
		}
		List<Path> paths = new LinkedList<>();
		for (File file : files) {
			if (precondition.isMet(file)) {
				Path p = destination.toPath().resolve(file.getName());
				Files.copy(file.toPath(), p,
						StandardCopyOption.REPLACE_EXISTING);
			}
		}
		return paths;
	}
	
	public Path copyDirectory(File sourceDir, File destination) throws IOException {
		return copyDirectory(sourceDir.toPath(), destination.toPath());
	}
	
	public Path copyDirectory(Path source, Path destination) throws IOException {
		CopyDirVisitor visitor = new CopyDirVisitor(source, destination);
		return Files.walkFileTree(source, visitor);
	}

	public boolean isFileType(final File file, final String mimeType) {
		// can we rely on mime types? I would like to see what happens
		// there are a few different types for .zip, and .js still cannot
		// be clearly identified by mime type.
		// perhaps build mime type table and then fall back on file extension
		//
		return false;
	}

	public boolean emptyDir(final File file) {
		if(file == null) {
			throw new NullPointerException("Cannot empty null directory.");
		}
		Boolean result = true;
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				result = result && emptyDir(f);
			}
		}
		return (file.delete() && result);
	}

}
