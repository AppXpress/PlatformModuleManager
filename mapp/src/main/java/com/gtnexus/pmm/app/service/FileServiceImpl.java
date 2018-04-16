package com.gtnexus.pmm.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.gtnexus.pmm.api.v100.service.FileService;
import com.gtnexus.pmm.app.common.file.CopyDirVisitor;
import com.gtnexus.pmm.app.common.file.DeleteDirVisitor;

/**
 * Service class to handle File IO procedures common to app.
 * 
 * @author jdonovan
 *
 */
public class FileServiceImpl implements FileService {

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#prependToName(java.io.File, java.lang.String)
     */
    @Override
    public Path prependToName(File file, String prepend) throws IOException {
	return renameFile(file, prepend + file.getName());
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#prependToName(java.io.File, java.lang.String, com.gtnexus.pmm.commons.HasPrerequisite)
     */
    @Override
    public Path prependToName(File file, String prepend, Predicate<File> precondition) throws IOException {
	return renameFile(file, prepend + file.getName(), precondition);
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#prependToName(java.util.Collection, java.lang.String)
     */
    @Override
    public List<Path> prependToName(Collection<File> files, String prepend) throws IOException {
	return prependToName(files, prepend, Predicates.<File>alwaysTrue());
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#prependToName(java.util.Collection, java.lang.String, com.gtnexus.pmm.commons.HasPrerequisite)
     */
    @Override
    public List<Path> prependToName(Collection<File> files, String prepend, Predicate<File> precondition)
	    throws IOException {
	List<Path> paths = new LinkedList<>();
	for (File file : files) {
	    paths.add(prependToName(file, prepend, precondition));
	}
	return paths;
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#renameFile(java.io.File, java.lang.String)
     */
    @Override
    public Path renameFile(File file, String newName) throws IOException {
	return renameFile(file, newName, Predicates.<File>alwaysTrue());
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#renameFile(java.io.File, java.lang.String, com.gtnexus.pmm.commons.HasPrerequisite)
     */
    @Override
    public Path renameFile(File file, String newName, Predicate<File> precondition) throws IOException {
	Path path = file.toPath();
	if (!precondition.apply(file))
	    return path;
	try {
	    path = Files.move(file.toPath(), file.toPath().resolveSibling(newName));
	} catch (IOException e) {
	    throw new IOException("Exception when trying to rename " + file.getAbsolutePath(), e);
	}
	return path;
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#renameFile(java.util.Collection, java.lang.String, java.lang.String)
     */
    @Override
    public List<Path> renameFile(Collection<File> files, String toReplace, String replacement) throws IOException {
	return renameFile(files, toReplace, replacement, Predicates.<File>alwaysTrue());
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#renameFile(java.util.Collection, java.lang.String, java.lang.String, com.gtnexus.pmm.commons.HasPrerequisite)
     */
    @Override
    public List<Path> renameFile(Collection<File> files, String toReplace, String replacement,
	    Predicate<File> precondition) throws IOException {
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

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#moveFiles(java.util.Collection, java.io.File)
     */
    @Override
    public List<Path> moveFiles(final Collection<File> files, final File destination) throws IOException {
	if (files == null || destination == null) {
	    throw new NullPointerException("files and destination cannot be null.");
	}
	if (!destination.isDirectory()) {
	    throw new IllegalArgumentException("Destination must be a directory.");
	}
	List<Path> paths = new LinkedList<>();
	for (File file : files) {
	    Path p = destination.toPath().resolve(file.getName());
	    Path result = Files.move(file.toPath(), p, StandardCopyOption.REPLACE_EXISTING);
	    paths.add(result);
	}
	return paths;
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#copyFiles(java.util.Collection, java.io.File, com.gtnexus.pmm.commons.HasPrerequisite)
     */
    @Override
    public List<Path> copyFiles(final Collection<File> files, final File destination,
	    Predicate<File> precondition) throws IOException {
	Preconditions.checkArgument(files != null && destination != null, "files and destination cannot be null.");
	Preconditions.checkArgument(destination.isDirectory(),
		"Destination " + destination.getName() + " is not a directory.");
	if (precondition == null) {
	    precondition = Predicates.alwaysTrue();
	}
	return _copyFiles(files, destination, precondition);
    }

    private List<Path> _copyFiles(final Collection<File> files, final File destination,
	    Predicate<File> precondition) throws IOException {
	List<Path> paths = new LinkedList<>();
	for (File file : files) {
	    Path result = _copyFile(destination, precondition, file);
	    if (result != null)
		paths.add(result);
	}
	return paths;
    }

    private Path _copyFile(final File destination, Predicate<File> precondition, File file) throws IOException {
	if (precondition.apply(file)) {
	    Path p = destination.toPath().resolve(file.getName());
	    return Files.copy(file.toPath(), p, StandardCopyOption.REPLACE_EXISTING);
	}
	return null;
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#copyDirectory(java.io.File, java.io.File)
     */
    @Override
    public Path copyDirectory(File sourceDir, File destination) throws IOException {
	return copyDirectory(sourceDir.toPath(), destination.toPath());
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#copyDirectory(java.nio.file.Path, java.nio.file.Path)
     */
    @Override
    public Path copyDirectory(Path source, Path destination) throws IOException {
	if (!Files.exists(source)) {
	    throw new IOException("Cannot copy directory tree from source: " + source.toString()
		    + ". Source directory does not exist.");
	}
	if (destination.startsWith(source)) {
	    throw new IllegalArgumentException("Cannot copy directory structure into subdirectory of itself.");
	}
	if (!Files.exists(destination)) {
	    Files.createDirectories(destination);
	}
	CopyDirVisitor visitor = new CopyDirVisitor(source, destination);
	return Files.walkFileTree(source, visitor);
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#isFileType(java.io.File, java.lang.String)
     */
    @Override
    public boolean isFileType(final File file, final String extension) {
	return file.getName().endsWith(extension);
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#emptyDir(java.io.File)
     */
    @Override
    public void emptyDir(final File root) throws IOException {
	emptyDir(root, false);
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#emptyDir(java.nio.file.Path)
     */
    @Override
    public void emptyDir(final Path root) throws IOException {
	emptyDir(root, false);
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#emptyDir(java.io.File, boolean)
     */
    @Override
    public void emptyDir(final File root, boolean deleteRoot) throws IOException {
	emptyDir(root.toPath(), deleteRoot);
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.file.FileService#emptyDir(java.nio.file.Path, boolean)
     */
    @Override
    public void emptyDir(final Path root, boolean deleteRoot) throws IOException {
	DeleteDirVisitor visitor = new DeleteDirVisitor(root, deleteRoot);
	Files.walkFileTree(root, visitor);
    }

}
