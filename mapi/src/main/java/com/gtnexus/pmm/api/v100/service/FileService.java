package com.gtnexus.pmm.api.v100.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.gtnexus.pmm.api.v100.NameToPath;

public interface FileService {

    /**
     * Prepends a String to the front of a single file.
     * 
     * @param file
     *            The file whose name will be altered.
     * @param prepend
     *            The String to prepend to the file's name.
     */
    Path prependToName(File file, String prepend) throws IOException;

    Path prependToName(File file, String prepend, Predicate<File> precondition) throws IOException;

    /**
     * Prepends a String to the the names of a collection of a files.
     * 
     * @param files
     *            The list of files who's name will be altered.
     * @param prepend
     *            The String to prepend to each file name.
     */
    List<Path> prependToName(Collection<File> files, String prepend) throws IOException;

    /**
     * Prepends a String to the the names of a collection of a files.
     * 
     * @param files
     *            The list of files who's name will be altered.
     * @param prepend
     *            The String to prepend to each file name.
     * @precondition The condition that must be met before string is prepended to
     *               file name
     */
    List<Path> prependToName(Collection<File> files, String prepend, Predicate<File> precondition)
	    throws IOException;

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
    Path renameFile(File file, String newName) throws IOException;

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
    Path renameFile(File file, String newName, Predicate<File> precondition) throws IOException;

    /**
     * Renames a set of files by replacing a matched string with some replacement.
     * 
     * @param files
     *            The set of file's whose names will be altered if they match the
     *            replacement string.
     * @param toReplace
     *            The string that will be replaced if found in any file names.
     * @param replacement
     *            The string that will replace the found string in the file name.
     */
    List<Path> renameFile(Collection<File> files, String toReplace, String replacement) throws IOException;

    /**
     * Renames a set of files by replacing a matched string with some replacement as
     * long as some precondition is met for each file.
     * 
     * @param files
     *            The set of file's whose names will be altered if they match the
     *            replacement string.
     * @param toReplace
     *            The string that will be replaced if found in any file names.
     * @param replacement
     *            The string that will replace the found string in the file name.
     * @param precondition
     *            The precondition that must be met in order for file to be renamed.
     */
    List<Path> renameFile(Collection<File> files, String toReplace, String replacement,
	    Predicate<File> precondition) throws IOException;

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
    List<Path> moveFiles(Collection<File> files, File destination) throws IOException;

    List<Path> copyFiles(Collection<String> fileNames, NameToPath converter, File destination,
	    Predicate<File> precondition) throws IOException;

    /**
     * 
     * @param files
     * @param destination
     * @param precondition
     * @return
     * @throws IOException
     */
    List<Path> copyFiles(Collection<File> files, File destination,
	    Predicate<File> precondition) throws IOException;

    /**
     * 
     * @param sourceDir
     * @param destination
     * @return
     * @throws IOException
     */
    Path copyDirectory(File sourceDir, File destination) throws IOException;

    /**
     * 
     * @param source
     * @param destination
     * @return
     * @throws IOException
     */
    Path copyDirectory(Path source, Path destination) throws IOException;

    /**
     * 
     * @param file
     * @param mimeType
     * @return
     */
    boolean isFileType(File file, String extension);

    /**
     * 
     * @param root
     * @throws IOException
     */
    void emptyDir(File root) throws IOException;

    void emptyDir(Path root) throws IOException;

    /**
     * 
     * @param root
     * @param deleteRoot
     * @throws IOException
     */
    void emptyDir(File root, boolean deleteRoot) throws IOException;

    void emptyDir(Path root, boolean deleteRoot) throws IOException;

}