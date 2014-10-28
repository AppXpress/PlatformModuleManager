package com.gtnexus.appxpress.pmbuilder;

import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

/**
 * Zips up a folder
 * 
 * @author John Donovan
 * @author Andrew Reynolds
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class ZipUtility {

	private File fileToZip;

	/**
	 * Zips up folder depicted by path folder
	 * 
	 * @param pathToZip
	 *            File to zip up
	 */
	public ZipUtility(String pathToZip) {
		fileToZip = new File(pathToZip);
	}

	/**
	 * Packs the given directory.
	 * 
	 * @param directoryPath
	 *            - the directory that is going to be packed
	 * @throws IOException
	 */
	public void zipDirectory() throws PMBuilderException {
		if (!fileToZip.exists() || !fileToZip.isDirectory()) {
			throw new PMBuilderException("No such directory" + fileToZip.getAbsolutePath());
		}
		System.out.println("Zipping up directory -> " + fileToZip.getAbsolutePath());
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
		for(File file : root.listFiles()) {
			if(file.isDirectory()) {
				zipFiles(file, zos);
			} else {
				ZipEntry entry = new ZipEntry(file.getName());
				zos.putNextEntry(entry);
				FileInputStream fis = new FileInputStream(file);
				byte[] block = new byte[1024];
				int bytesRead = 0;
				while((bytesRead = fis.read(block)) > 0) {
					zos.write(block, 0, bytesRead);
				}
				fis.close();
			}
		}
	}

}