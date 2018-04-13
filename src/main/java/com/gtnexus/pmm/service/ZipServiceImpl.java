package com.gtnexus.pmm.service;

import static com.gtnexus.pmm.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.gtnexus.pmm.api.v100.command.SubCommandException;
import com.gtnexus.pmm.api.v100.service.FileService;
import com.gtnexus.pmm.api.v100.service.ZipService;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Zips up a folder
 * 
 * @author John Donovan
 * @author Andrew Reynolds
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class ZipServiceImpl implements ZipService {

    private final FileService fs;
    private final Set<String> ignoreSet;

    public ZipServiceImpl() {
	this(Collections.<String>emptySet());
    }

    public ZipServiceImpl(Set<String> ignoreDirNames) {
	this.fs = new FileServiceImpl();
	this.ignoreSet = ignoreDirNames;
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.ZipService#zipFiles(java.util.Collection, java.lang.String)
     */
    @Override
    public void zipFiles(Collection<File> files, String absPathToDestinationZip) throws SubCommandException {
	try (FileOutputStream fos = new FileOutputStream(absPathToDestinationZip);
		ZipOutputStream zos = new ZipOutputStream(fos)) {
	    for (File f : files) {
		zipSingle(zos, f);
	    }
	} catch (IOException e) {
	    throw new SubCommandException("Error when zipping collection of files", e);
	}
    }

    private void zipSingle(ZipOutputStream zos, File f) throws IOException {
	try (FileInputStream fis = new FileInputStream(f)) {
	    ZipEntry entry = new ZipEntry(f.getName());
	    zos.putNextEntry(entry);
	    byte[] block = new byte[1024];
	    int bytesRead = 0;
	    while ((bytesRead = fis.read(block)) > 0) {
		zos.write(block, 0, bytesRead);
	    }
	}
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.ZipService#zipDirectory(java.nio.file.Path)
     */
    @Override
    public void zipDirectory(Path directory) throws SubCommandException {
	zipDirectory(directory.toFile());
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.ZipService#zipDirectory(java.io.File)
     */
    @Override
    public void zipDirectory(File directory) throws SubCommandException {
	if (!directory.exists() || !directory.isDirectory()) {
	    throw new SubCommandException("No such directory" + directory.getAbsolutePath());
	}
	String outputZip = directory.getAbsolutePath() + ZIP_EXTENSION;
	zipDirectory(directory, outputZip);
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.ZipService#zipDirectory(java.io.File, java.lang.String)
     */
    @Override
    public void zipDirectory(File directory, String outputZip) throws SubCommandException {
	zipDirectory(directory, new File(outputZip));
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.ZipService#zipDirectory(java.io.File, java.io.File)
     */
    @Override
    public void zipDirectory(File directory, File outputZip) throws SubCommandException {
	if (!directory.exists() || !directory.isDirectory()) {
	    throw new SubCommandException("No such directory " + directory.getAbsolutePath());
	}
	if (!outputZip.getName().endsWith(ZIP_EXTENSION)) {
	    outputZip = new File(outputZip.toString() + ZIP_EXTENSION);
	}
	System.out.println("Creating zip " + outputZip.getAbsolutePath());
	try (FileOutputStream fos = new FileOutputStream(outputZip); ZipOutputStream zos = new ZipOutputStream(fos)) {
	    zipDirFiles(directory, directory, zos);
	    zos.closeEntry();
	} catch (IOException e) {
	    throw new SubCommandException("Exception when recursively zipping " + directory.getAbsolutePath(), e);
	}
    }

    /**
     * Recursively pack directory contents.
     * 
     * @param file
     *            - current directory path that is visited recursively
     * @param zos
     *            - ZIP output stream reference to add elements to
     * @throws IOException
     */
    private void zipDirFiles(File root, File file, ZipOutputStream zos) throws IOException {
	for (File f : file.listFiles()) {
	    if (f.isDirectory()) {
		if (!ignoreSet.contains(f.getName())) {
		    zipDirFiles(root, f, zos);
		}
	    } else {
		ZipEntry entry = new ZipEntry(zipName(root, f));
		zos.putNextEntry(entry);
		FileInputStream fis = new FileInputStream(f);
		byte[] block = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = fis.read(block)) > 0) {
		    zos.write(block, 0, bytesRead);
		}
		fis.close();
	    }
	}
    }

    private String zipName(File root, File file) throws IOException {
	return file.getCanonicalPath().substring(root.getCanonicalPath().length() + 1,
		file.getCanonicalPath().length());
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.ZipService#unzip(java.io.File, java.io.File, boolean)
     */
    @Override
    public void unzip(File source, File destination, boolean recurse) throws SubCommandException {
	unzip(source, destination);
	if (recurse) {
	    recurseUnzip(destination);
	}
    }

    /* (non-Javadoc)
     * @see com.gtnexus.pmm.commons.ZipService#unzip(java.io.File, java.io.File)
     */
    @Override
    public void unzip(File source, File destination) throws SubCommandException {
	try {
	    ZipFile zip = new ZipFile(source);
	    zip.extractAll(destination.getAbsolutePath());
	} catch (ZipException e) {
	    throw new SubCommandException("Exception when unzipping", e);
	}
    }

    /**
     * Recursively iterated through file structure folder and unzips and contained
     * zip files
     * 
     * In the case that afolder.zip unzips to afolder/afolder/{files} the folder is
     * bubbled up.
     * 
     * @param path
     *            Destination of file structure to iterate over
     */
    private void recurseUnzip(File f) throws SubCommandException {
	if (f.isDirectory() && !ignoreSet.contains(f.getName())) {
	    for (File item : f.listFiles()) {
		recurseUnzip(item);
	    }
	} else {
	    if (f.getName().endsWith(ZIP_EXTENSION)) {
		String cleanedPath = f.getAbsolutePath().replace(ZIP_EXTENSION, "");
		File destination = new File(cleanedPath);
		unzip(f, destination);
		f.delete();
		bubbleWhenNecessary(destination);
	    }
	}
    }

    private void bubbleWhenNecessary(File f) {
	if (!shouldBubble(f)) {
	    return;
	}
	File onlyChild = f.listFiles()[0];
	try {
	    fs.moveFiles(Arrays.asList(onlyChild.listFiles()), f);
	    onlyChild.delete();
	} catch (IOException e) {
	    System.out.println("Unable to bubble up " + f.getAbsolutePath() + ". Continuing to unzip anyways.");
	}
    }

    private boolean shouldBubble(File f) {
	if (f.list().length != 1) {
	    return false;
	}
	File onlyChild = f.listFiles()[0];
	return onlyChild.isDirectory() && (f.getName().endsWith(onlyChild.getName()));
    }

}