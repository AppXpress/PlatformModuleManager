package com.gtnexus.appxpress;

import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

/**
 * Zips up a folder
 * 
 * @author John Donovan
 * @author Andrew Reynolds
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class ZipService {

	private File fileToZip;

	/**
	 * Zips up folder depicted by path folder
	 * 
	 * @param pathToZip
	 *            File to zip up
	 */
	@Deprecated
	public ZipService(String pathToZip) {
		fileToZip = new File(pathToZip);
	}

	public ZipService() {
	}

	/**
	 * Packs the given directory.
	 * 
	 * @param directoryPath
	 *            - the directory that is going to be packed
	 * @throws IOException
	 */
	public void zipDirectory(File directory) throws PMBuilderException {
		if (!directory.exists() || !directory.isDirectory()) {
			throw new PMBuilderException("No such directory"
					+ directory.getAbsolutePath());
		}
		System.out.println("Zipping up directory -> "
				+ directory.getAbsolutePath());
		String outputFile = directory.getAbsolutePath() + ZIP_EXTENSION;
		try (FileOutputStream fos = new FileOutputStream(outputFile);
				ZipOutputStream zos = new ZipOutputStream(fos)) {
			zipFiles(directory, zos);
			zos.closeEntry();
		} catch (IOException e) {
			throw new PMBuilderException("Exception when recursively zipping "
					+ directory.getAbsolutePath(), e);
		}
	}

	/**
	 * Packs the given directory.
	 * 
	 * @param directoryPath
	 *            - the directory that is going to be packed
	 * @throws IOException
	 */
	@Deprecated
	public void zipDirectory() throws PMBuilderException {
		if (!fileToZip.exists() || !fileToZip.isDirectory()) {
			throw new PMBuilderException("No such directory"
					+ fileToZip.getAbsolutePath());
		}
		System.out.println("Zipping up directory -> "
				+ fileToZip.getAbsolutePath());
		String outputFile = fileToZip.getAbsolutePath() + ZIP_EXTENSION;
		try (FileOutputStream fos = new FileOutputStream(outputFile);
				ZipOutputStream zos = new ZipOutputStream(fos)) {
			zipFiles(fileToZip, zos);
			zos.closeEntry();
		} catch (IOException e) {
			throw new PMBuilderException("Exception when recursively zipping "
					+ fileToZip.getAbsolutePath(), e);
		}
	}

	/**
	 * Recursively pack directory contents.
	 * 
	 * @param root
	 *            - current directory path that is visited recursively
	 * @param zos
	 *            - ZIP output stream reference to add elements to
	 * @throws IOException
	 */
	private void zipFiles(File root, ZipOutputStream zos) throws IOException {
		for (File file : root.listFiles()) {
			if (file.isDirectory()) {
				zipFiles(file, zos);
			} else {
				ZipEntry entry = new ZipEntry(file.getName());
				zos.putNextEntry(entry);
				FileInputStream fis = new FileInputStream(file);
				byte[] block = new byte[1024];
				int bytesRead = 0;
				while ((bytesRead = fis.read(block)) > 0) {
					zos.write(block, 0, bytesRead);
				}
				fis.close();
			}
		}
	}
	
	public void unzip(File source, File destination, boolean recurse) {
		unzip(source, destination);
		if(recurse) {
			recurseUnzip(destination);
		}
	}

	public void unzip(File source, File destination) {
		try {
			ZipFile zip = new ZipFile(source);
			zip.extractAll(destination.getAbsolutePath());
		} catch (ZipException e) {
			System.err.println("Error in unzip");
			e.printStackTrace();
		}
	}

	/**
	 * Recursively iterated through file structure folder and unzips and
	 * contained zip files
	 * 
	 * @param path
	 *            Destination of file structure to iterate over
	 */
	private void recurseUnzip(File f) {
		if (f.isDirectory()) {
			for (File item : f.listFiles()) {
				recurseUnzip(item);
			}
		} else {
			if (f.getName().endsWith(ZIP_EXTENSION)) {
				String cleanedPath = f.getName().replace(ZIP_EXTENSION, "");
				unzip(f, new File(cleanedPath));
				f.delete();
			}
		}
	}

}