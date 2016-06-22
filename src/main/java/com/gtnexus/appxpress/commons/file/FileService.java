package com.gtnexus.appxpress.commons.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.gtnexus.appxpress.commons.HasPrerequisite;

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

	public Path prependToName(File file, String prepend,
			HasPrerequisite<File> precondition) throws IOException {
		return renameFile(file, prepend + file.getName(), precondition);
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
		return prependToName(files, prepend,
				new HasPrerequisite.EmptyCondition<File>());
	}

	/**
	 * Prepends a String to the the names of a collection of a files.
	 * 
	 * @param files
	 *            The list of files who's name will be altered.
	 * @param prepend
	 *            The String to prepend to each file name.
	 * @precondition The condition that must be met before string is prepended
	 *               to file name
	 */
	public List<Path> prependToName(Collection<File> files, String prepend,
			HasPrerequisite<File> precondition) throws IOException {
		List<Path> paths = new LinkedList<>();
		for (File file : files) {
			paths.add(prependToName(file, prepend, precondition));
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
		return renameFile(file, newName,
				new HasPrerequisite.EmptyCondition<File>());
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
	public Path renameFile(File file, String newName,
			HasPrerequisite<File> precondition) throws IOException {
		Path path = file.toPath();
		if (!precondition.isMet(file))
			return path;
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
		return renameFile(files, toReplace, replacement,
				new HasPrerequisite.EmptyCondition<File>());
	}

	/**
	 * Renames a set of files by replacing a matched string with some
	 * replacement as long as some precondition is met for each file.
	 * 
	 * @param files
	 *            The set of file's whose names will be altered if they match
	 *            the replacement string.
	 * @param toReplace
	 *            The string that will be replaced if found in any file names.
	 * @param replacement
	 *            The string that will replace the found string in the file
	 *            name.
	 * @param precondition
	 *            The precondition that must be met in order for file to be
	 *            renamed.
	 */
	public List<Path> renameFile(Collection<File> files, String toReplace,
			String replacement, HasPrerequisite<File> precondition)
			throws IOException {
		Preconditions.checkNotNull(files, "Files to rename cannot be null.");
		Preconditions.checkNotNull(toReplace, "String to be replaced cannot be null");
		Preconditions.checkNotNull(replacement, "Replacement string cannot be null");
		List<Path> paths = new LinkedList<>();
		for (File file : files) {
			String fileName = file.getName();
			Path result = renameFile(file, fileName.replace(toReplace, replacement), precondition);
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
					"Destination must be a directory.");
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

	public List<Path> copyFiles(final Collection<String> fileNames,
			final NameToPath converter, final File destination,
			HasPrerequisite<File> precondition) throws IOException {
		if (fileNames == null || destination == null) {
			throw new NullPointerException(
					"files and destination cannot be null.");
		}
		if (!destination.isDirectory()) {
			throw new IllegalArgumentException(
					"Destination must be a directory.");
		}
		List<Path> paths = new LinkedList<>();
		for (String file : fileNames) {
			paths.add(_copyFile(destination, precondition,
					converter.resolve(file).toFile()));
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
			final File destination, HasPrerequisite<File> precondition)
			throws IOException {
		Preconditions.checkArgument(files != null && destination != null, 
				"files and destination cannot be null.");
		Preconditions.checkArgument(destination.isDirectory(), 
				"Destination " + destination.getName()+ " is not a directory.");
		if (precondition == null) {
			precondition = new HasPrerequisite.EmptyCondition<>();
		}
		return _copyFiles(files, destination, precondition);
	}

	private List<Path> _copyFiles(final Collection<File> files,
			final File destination, HasPrerequisite<File> precondition)
			throws IOException {
		List<Path> paths = new LinkedList<>();
		for (File file : files) {
			Path result = _copyFile(destination, precondition, file);
			if (result != null)
				paths.add(result);
		}
		return paths;
	}

	private Path _copyFile(final File destination,
			HasPrerequisite<File> precondition, File file) throws IOException {
		if (precondition.isMet(file)) {
			Path p = destination.toPath().resolve(file.getName());
			return Files.copy(file.toPath(), p,
					StandardCopyOption.REPLACE_EXISTING);
		}
		return null;
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
		if (!Files.exists(source)) {
			throw new IOException("Cannot copy directory tree from source: "
					+ source.toString() + ". Source directory does not exist.");
		}
		if (destination.startsWith(source)) {
			throw new IllegalArgumentException(
					"Cannot copy directory structure into subdirectory of itself.");
		}
		if (!Files.exists(destination)) {
			Files.createDirectories(destination);
		}
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

	public void emptyDir(final Path root) throws IOException {
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
		emptyDir(root.toPath(), deleteRoot);
	}

	public void emptyDir(final Path root, boolean deleteRoot)
			throws IOException {
		DeleteDirVisitor visitor = new DeleteDirVisitor(root, deleteRoot);
		Files.walkFileTree(root, visitor);
	}

}
