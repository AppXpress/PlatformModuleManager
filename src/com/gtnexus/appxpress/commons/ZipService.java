package com.gtnexus.appxpress.commons;

import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.commons.file.FileService;

/**
 * Zips up a folder
 * 
 * @author John Donovan
 * @author Andrew Reynolds
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class ZipService {

	private final FileService fs;

	public ZipService() {
		this.fs = new FileService();
	}

	public void zipFiles(Collection<File> files, String absPathToDestinationZip)
			throws AppXpressException {
		try (FileOutputStream fos = new FileOutputStream(
				absPathToDestinationZip);
				ZipOutputStream zos = new ZipOutputStream(fos)) {
			for (File f : files) {
				zipSingle(zos, f);
			}
		} catch (IOException e) {
			throw new AppXpressException(
					"Error when zipping collection of files", e);
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

	/**
	 * Packs the given directory into a a zip file named after the directory.
	 * 
	 * @param directoryPath
	 *            - the directory that is going to be packed
	 * @throws IOException
	 */
	public void zipDirectory(File directory) throws AppXpressException {
		if (!directory.exists() || !directory.isDirectory()) {
			throw new AppXpressException("No such directory"
					+ directory.getAbsolutePath());
		}
		String outputZip = directory.getAbsolutePath() + ZIP_EXTENSION;
		zipDirectory(directory, outputZip);
	}

	/**
	 * Packs the given directory into a a zip file named after the directory.
	 * 
	 * @param directoryPath
	 *            - the directory that is going to be packed
	 * @throws IOException
	 */
	public void zipDirectory(File directory, String outputZip)
			throws AppXpressException {
		zipDirectory(directory, new File(outputZip));
	}

	/**
	 * Packs the given directory into the zip file pointer. If the outputZip
	 * does not end with .zip it is appended.
	 * 
	 * @param directory
	 * @param outputZip
	 * @throws AppXpressException
	 */
	public void zipDirectory(File directory, File outputZip)
			throws AppXpressException {
		if (!directory.exists() || !directory.isDirectory()) {
			throw new AppXpressException("No such directory "
					+ directory.getAbsolutePath());
		}
		if (!outputZip.getName().endsWith(ZIP_EXTENSION)) {
			outputZip = new File(outputZip.toString() + ZIP_EXTENSION);
		}
		System.out.println("Zipping up directory -> "
				+ directory.getAbsolutePath());
		try (FileOutputStream fos = new FileOutputStream(outputZip);
				ZipOutputStream zos = new ZipOutputStream(fos)) {
			zipDirFiles(directory, directory, zos);
			zos.closeEntry();
		} catch (IOException e) {
			throw new AppXpressException("Exception when recursively zipping "
					+ directory.getAbsolutePath(), e);
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
	private void zipDirFiles(File root, File file, ZipOutputStream zos)
			throws IOException {
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				zipDirFiles(root, f, zos);
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
		return file.getCanonicalPath().substring(
				root.getCanonicalPath().length() + 1,
				file.getCanonicalPath().length());
	}

	public void unzip(File source, File destination, boolean recurse)
			throws AppXpressException {
		unzip(source, destination);
		if (recurse) {
			recurseUnzip(destination);
		}
	}

	public void unzip(File source, File destination) throws AppXpressException {
		try {
			ZipFile zip = new ZipFile(source);
			zip.extractAll(destination.getAbsolutePath());
		} catch (ZipException e) {
			throw new AppXpressException("Exception when unzipping", e);
		}
	}

	/**
	 * Recursively iterated through file structure folder and unzips and
	 * contained zip files
	 * 
	 * In the case that afolder.zip unzips to afolder/afolder/{files} the folder
	 * is bubbled up.
	 * 
	 * @param path
	 *            Destination of file structure to iterate over
	 */
	private void recurseUnzip(File f) throws AppXpressException {
		if (f.isDirectory()) {
			for (File item : f.listFiles()) {
				recurseUnzip(item);
			}
		} else {
			if (f.getName().endsWith(ZIP_EXTENSION)) {
				String cleanedPath = f.getAbsolutePath().replace(ZIP_EXTENSION,
						"");
				File destination = new File(cleanedPath);
				unzip(f, destination);
				f.delete();
				bubbleWhenNecessary(destination);
			}
		}
	}

	private void bubbleWhenNecessary(File f) {
		if (f.list().length != 1) {
			return;
		}
		File onlyChild = f.listFiles()[0];
		if (!onlyChild.isDirectory() || !onlyChild.getName().equals(f.getName())) {
			return;
		}
		try {
			fs.moveFiles(Arrays.asList(onlyChild.listFiles()), f);
			onlyChild.delete();
		} catch (IOException e) {
			System.out.println("Unable to bubble up " + f.getAbsolutePath()
					+ ". Continuing to unzip anyways.");
		}
	}

}