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
 * Service class to handle File IO procedures common to app.
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
	public Path prependToName(File file, String prepend) throws IOException {
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
	public List<Path> prependToName(Collection<File> files, String prepend)
			throws IOException {
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
	 * @throws IOException
	 *             if the renaming operation fails.
	 */
	public Path renameFile(File file, String newName) throws IOException {
		Path path = null;
		try {
			path = Files.move(file.toPath(),
					file.toPath().resolveSibling(newName));
		} catch (IOException e) {
			throw new IOException("Exception when trying to rename "
					+ file.getAbsolutePath(), e);
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
	public List<Path> renameFile(Collection<File> files, String toReplace,
			String replacement) throws IOException {
		if (files == null) {
			throw new NullPointerException("Files cannot be null.");
		}
		if (toReplace == null || replacement == null) {
			throw new NullPointerException(
					"Replacement Strings cannot be null.");
		}
		List<Path> paths = new LinkedList<>();
		for (File file : files) {
			String fileName = file.getName();
			Path result = renameFile(file,
					fileName.replace(toReplace, replacement));
			paths.add(result);
		}
		return paths;
	}

	/**
	 * 
	 * @param files
	 *            The files to be copied.
	 * @param destination
	 *            The destination directory.
	 * @return The list of paths to the the copied files.
	 * @throws IOException
	 * @throws IllegalArgumentDestination
	 *             If destination is not a directory.
	 */
	public List<Path> moveFiles(final Collection<File> files,
			final File destination) throws IOException {
		if (files == null || destination == null) {
			throw new NullPointerException(
					"files and destination cannot be null.");
		}
		if (!destination.isDirectory()) {
			throw new IllegalArgumentException(
					"Detination must be a directory.");
		}
		List<Path> paths = new LinkedList<>();
		for (File file : files) {
			Path p = destination.toPath().resolve(file.getName());
			Path result = Files.move(file.toPath(), p,
					StandardCopyOption.REPLACE_EXISTING);
			paths.add(result);
		}
		return paths;
	}

	/**
	 * 
	 * @param files
	 * @param destination
	 * @param precondition
	 * @return
	 * @throws IOException
	 */
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

	/**
	 * 
	 * @param sourceDir
	 * @param destination
	 * @return
	 * @throws IOException
	 */
	public Path copyDirectory(File sourceDir, File destination)
			throws IOException {
		return copyDirectory(sourceDir.toPath(), destination.toPath());
	}

	/**
	 * 
	 * @param source
	 * @param destination
	 * @return
	 * @throws IOException
	 */
	public Path copyDirectory(Path source, Path destination) throws IOException {
		CopyDirVisitor visitor = new CopyDirVisitor(source, destination);
		return Files.walkFileTree(source, visitor);
	}

	/**
	 * 
	 * @param file
	 * @param mimeType
	 * @return
	 */
	public boolean isFileType(final File file, final String extension) {
		return file.getName().endsWith(extension);
	}

	/**
	 * 
	 * @param root
	 * @throws IOException
	 */
	public void emptyDir(final File root) throws IOException {
		emptyDir(root, false);
	}

	/**
	 * 
	 * @param root
	 * @param deleteRoot
	 * @throws IOException
	 */
	public void emptyDir(final File root, boolean deleteRoot)
			throws IOException {
		DeleteDirVisitor visitor = new DeleteDirVisitor(root.toPath(),
				deleteRoot);
		Files.walkFileTree(root.toPath(), visitor);
	}

}
