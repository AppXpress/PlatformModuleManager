package com.gtnexus.appxpress.pmextractor.gitmap;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_LINK_D1;

import java.io.File;
import java.io.IOException;

import com.gtnexus.appxpress.Precondition;
import com.gtnexus.appxpress.Preparation;
import com.gtnexus.appxpress.ZipService;
import com.gtnexus.appxpress.file.FileService;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

/**
 * Performs all prep work for GitMap, including cleaning directories,
 * unzipping the platform, and creating a backup.
 * 
 * @author jdonovan
 *
 */
public class GitMapPrep implements Precondition<GitMapVO>,
		Preparation<GitMapVO> {

	private final FileService fs;
	private final ZipService zs;

	public GitMapPrep() {
		fs = new FileService();
		zs = new ZipService();
	}

	@Override
	public void prepare(GitMapVO vo) throws PMBuilderException {
		if (!isMet(vo)) {
			throw new PMBuilderException("Precondition failed.");
		}
		cleanup(vo.getUnzipDir());
		unzipPlatformZip(vo.getPlatformZip(), vo.getUnzipDir());
		makeHumanReadable(vo.getUnzipDir());
		backup(vo.getCustomerDir(), vo.getPlatform());
		clearCustomLinksXML(vo.getCustomerDir());
	}

	private void cleanup(File unzipDestination) {
		if (unzipDestination.exists()) {
			try {
				fs.emptyDir(unzipDestination); 
			} catch(IOException e) {
				//TODO
			}
		}
	}

	/**
	 * Custom Link xml files are going to be replaced with the new custom link
	 * files from the exported module. This method rids the CustomLinkD1 folder
	 * of outdated custom links files.
	 *
	 */
	private void clearCustomLinksXML(File customerPath) {
		File dir = customerPath.toPath().resolve(CUSTOM_LINK_D1).toFile();
		cleanup(dir);
	}

	/**
	 * Unzips file 'folder' into PlatModX
	 */
	private void unzipPlatformZip(File platformZip, File unzipDestination) {
		if (platformZip.exists()) {
			zs.unzip(platformZip, unzipDestination);
		} else {
			System.out.println("Cannot find zipped folder!");
		}
	}

	/**
	 * Cleans folder structure of $ signs
	 * 
	 * @param folderName
	 *            Location of file structure
	 */
	private void makeHumanReadable(File f) {
		if (!f.exists()) {
			System.err.println("Cannot find folder -> " + f.getName());
			return;
		}
		if (f.isDirectory()) {
			for (File s : f.listFiles()) {
				makeHumanReadable(s);
			}
		}
		if (f.getName().contains($)) {
			fs.renameFile(f, f.getName().replace($, ""));
		}
	}

	/**
	 * Backs up folder/customer/cust/plat into a folder called PM_Git_Backup
	 */
	private void backup(File customerPath, String platform) {
		String backup = "PM_Git_Backup" + File.separator + platform;
		File bkpFile = new File(backup);
		try {
			fs.copyDirectory(customerPath, bkpFile);
		} catch (IOException e) {
			System.err.println("error backing up -> " + e);
		}
	}

	@Override
	public boolean isMet(GitMapVO vo) {
		if (!validateAndEnsurePathExists(vo)) {
			System.err.println("ERROR: Paths are not set up properly!");
			return false;
		}
		if (!vo.getLocalDir().isDirectory() || !vo.getPlatformZip().exists()) {
			System.err
					.println("ERROR: One of the following paths is invalid:\n\t"
							+ vo.getLocalDir().getAbsolutePath()
							+ "\n\t"
							+ vo.getPlatformZip().getAbsolutePath());
			return false;
		}
		return true;
	}

	/**
	 * Validates the path composed of parameters
	 * 
	 * @param folder
	 *            Highest folder
	 * @param sub
	 *            Second highest folder
	 * @param subCo
	 *            Lowest subfolder
	 * @return true if file structure exists false if file structure does not
	 *         exist
	 */
	private boolean validateAndEnsurePathExists(GitMapVO vo) {
		if (!vo.getCustomerDir().isDirectory()) {
			System.err
					.println("Cannot find specific customer in customer folder ["
							+ vo.getCustomerDir().getAbsolutePath() + "]");
			return false;
		}
		if (!vo.getPlatformDir().exists()) {
			vo.getPlatformDir().mkdir();
		}
		return true;
	}

}
