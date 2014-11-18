package com.gtnexus.appxpress.pmextractor.gitmap;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_LINK_D1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.Precondition;
import com.gtnexus.appxpress.Preparation;
import com.gtnexus.appxpress.ZipService;
import com.gtnexus.appxpress.file.FileService;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

/**
 * Performs all prep work for GitMap, including cleaning directories, unzipping
 * the platform, and creating a backup.
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
		backup(vo.getCustomerDir().toPath(), vo.getPlatform());
		clearCustomLinksXML(vo.getCustomerDir());
	}

	private void cleanup(File unzipDestination) throws PMBuilderException {
		if (unzipDestination.exists()) {
			try {
				fs.emptyDir(unzipDestination);
			} catch (IOException e) {
				throw new PMBuilderException(
						"Exception when cleaning unzipDestination", e);
			}
		}
	}

	/**
	 * Custom Link xml files are going to be replaced with the new custom link
	 * files from the exported module. This method rids the CustomLinkD1 folder
	 * of outdated custom links files.
	 *
	 */
	private void clearCustomLinksXML(File customerPath)
			throws PMBuilderException {
		File dir = customerPath.toPath().resolve(CUSTOM_LINK_D1).toFile();
		cleanup(dir);
	}

	/**
	 * Unzips file 'folder' into PlatModX
	 */
	private void unzipPlatformZip(File platformZip, File unzipDestination)
			throws PMBuilderException {
		if (platformZip.exists()) {
			try {
				zs.unzip(platformZip, unzipDestination);
			} catch (AppXpressException e) {
				throw new PMBuilderException(
						"Exception when unzipping platformZip: " + platformZip,
						e);
			}

		} else {
			System.out.println("Cannot find zipped folder!");
		}
	}

	/**
	 * Cleans folder structure of $ signs
	 * 
	 * @param folderName
	 *            Location of file structure
	 * @throws PMBuilderException
	 */
	private void makeHumanReadable(File f) throws PMBuilderException {
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
			try {
				fs.renameFile(f, f.getName().replace($, ""));
			} catch (IOException e) {
				throw new PMBuilderException("Exception when renaming " + f, e);
			}
		}
	}

	/**
	 * Backs up folder/customer/cust/plat into a folder called PM_Git_Backup
	 */
	private void backup(Path customerPath, String platform) {
		AppXpressDirResolver resolver = new AppXpressDirResolver();
		Path bkpPath = resolver.resolveAppXpressDir().resolve("PM_Git_Backup")
				.resolve(platform);
		try {
			if (Files.exists(bkpPath)) {
				fs.emptyDir(bkpPath);
			}
			fs.copyDirectory(customerPath, bkpPath);
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
