package com.gtnexus.pmm.pmextractor.gitmap;

import static com.gtnexus.pmm.AppXpressConstants.$;
import static com.gtnexus.pmm.AppXpressConstants.BACKUP_FLDR;
import static com.gtnexus.pmm.AppXpressConstants.CUSTOM_LINK_D1;
import static com.gtnexus.pmm.AppXpressConstants.UI_BUNDLE_PREFIX;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.gtnexus.pmm.AppXpressConstants;
import com.gtnexus.pmm.AppXpressDirResolver;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.PlatformModuleManagerServices;
import com.gtnexus.pmm.commons.HasPrerequisite;
import com.gtnexus.pmm.commons.Preparation;
import com.gtnexus.pmm.pmbuilder.exception.PMBuilderException;

/**
 * Performs all prep work for GitMap, including cleaning directories, unzipping
 * the platform, and creating a backup.
 * 
 * @author jdonovan
 *
 */
public class GitMapPrep implements HasPrerequisite<GitMapVO>, Preparation<GitMapVO> {

    private final PlatformModuleManagerServices  services;

    public GitMapPrep(PlatformModuleManagerServices services) {
	this.services = services;
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
		this.services.getFileService().emptyDir(unzipDestination);
	    } catch (IOException e) {
		throw new PMBuilderException("Exception when cleaning unzipDestination", e);
	    }
	}
    }

    /**
     * Custom Link xml files are going to be replaced with the new custom link files
     * from the exported module. This method rids the CustomLinkD1 folder of
     * outdated custom links files.
     *
     */
    private void clearCustomLinksXML(File customerPath) throws PMBuilderException {
	File dir = customerPath.toPath().resolve(CUSTOM_LINK_D1).toFile();
	cleanup(dir);
    }

    /**
     * Unzips file 'folder' into PlatModX
     */
    private void unzipPlatformZip(File platformZip, File unzipDestination) throws PMBuilderException {
	if (platformZip.exists()) {
	    try {
		this.services.getZipService().unzip(platformZip, unzipDestination, true);
		this.services.getTemporaryResourceService().markForDeletion(unzipDestination);
	    } catch (AppXpressException e) {
		throw new PMBuilderException("Exception when unzipping platformZip: " + platformZip, e);
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
	    checkIfBundle(f);
	}
	try {
	    if (f.getName().contains($)) {
		this.services.getFileService().renameFile(f, f.getName().replace($, ""));
	    }
	    if (parentIsCustomUi(f) && customUiBundleHasPrefix(f)) {
		this.services.getFileService().renameFile(f, f.getName().substring(2));
	    }
	} catch (IOException e) {
	    throw new PMBuilderException("Exception when coercing module to human readable format ->" + f, e);
	}
    }

    private boolean parentIsCustomUi(File f) {
	File parent = f.getParentFile();
	return parent == null ? false : AppXpressConstants.CUSTOM_UI.equals(parent.getName());
    }

    private boolean customUiBundleHasPrefix(File f) {
	return f.isDirectory() && f.getName().startsWith(UI_BUNDLE_PREFIX);
    }

    private void checkIfBundle(File f) throws PMBuilderException {
	String fName = f.getName();
	if (fName.endsWith("Bundle")) {
	    int terminal = fName.length() - "Bundle".length();
	    fName = fName.substring(0, terminal);
	    try {
		this.services.getFileService().renameFile(f, fName);
	    } catch (IOException e) {
		throw new PMBuilderException("Failed to rename bundle " + fName, e);
	    }
	}
    }

    /**
     * Backs up folder/customer/cust/plat into a folder called PM_Git_Backup
     */
    private void backup(Path customerPath, String platform) {
	AppXpressDirResolver resolver = new AppXpressDirResolver();
	Path bkpPath = resolver.resolveAppXpressDir().resolve(BACKUP_FLDR).resolve(platform);
	try {
	    if (Files.exists(bkpPath)) {
		this.services.getFileService().emptyDir(bkpPath);
	    }
	    this.services.getFileService().copyDirectory(customerPath, bkpPath);
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
	    System.err.println("ERROR: One of the following paths is invalid:\n\t" + vo.getLocalDir().getAbsolutePath()
		    + "\n\t" + vo.getPlatformZip().getAbsolutePath());
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
     * @return true if file structure exists false if file structure does not exist
     */
    private boolean validateAndEnsurePathExists(GitMapVO vo) {
	if (!vo.getCustomerDir().isDirectory()) {
	    System.out.println(
		    "Cannot find specific customer in customer folder [" + vo.getCustomerDir().getAbsolutePath() + "]");
	    System.out.println("\tAssuming this is a new customer, and creating directory...");
	    File customer = vo.getCustomerDir();
	    if (!customer.mkdir()) {
		return false;
	    }
	}
	if (!vo.getPlatformDir().exists()) {
	    return vo.getPlatformDir().mkdir();
	}
	return true;
    }

}
