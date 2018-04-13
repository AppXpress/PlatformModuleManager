package com.gtnexus.pmm.app.build.bundle.platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.gtnexus.pmm.api.v100.command.SubCommandException;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.app.build.cli.PMBuilderVO;
import com.gtnexus.pmm.app.build.exception.PMBuilderException;
import com.gtnexus.pmm.app.build.scriptimport.ImportService;

/**
 * 
 * @author jdonovan
 *
 */
public class BuildPrep {

    private final PlatformModuleManagerServices services;
    private final Path libPath;

    public BuildPrep(PlatformModuleManagerServices services, Path libPath) {
	this.libPath = libPath;
	this.services = services;
    }

    public void prepare(final PMBuilderVO vo) throws PMBuilderException {
	try {
	    File tmp = createTemp(vo);
	    vo.setWorkingDir(tmp);
	    runImportFind(tmp, libPath);
	    map(tmp);
	} catch (SubCommandException | IOException e) {
	    throw new PMBuilderException("Exception when mapping file structure.", e);
	}
    }

    private File createTemp(final PMBuilderVO vo) throws IOException {
	Path source = vo.getRootFile().toPath();
	Path appxDir = services.getEnvironmentService().getAppXDir();
	Path tmpPath = appxDir.toAbsolutePath();
	String tmpPrefix = String.valueOf(System.currentTimeMillis());
	Path destination = Files.createTempDirectory(tmpPath, tmpPrefix);
	File dest = destination.toFile();
	services.getTemporaryResourceService().markForDeletion(dest);
	services.getFileService().copyDirectory(source, destination);
	return dest;
    }

    /**
     * Iterates through folder customer/customer/folder
     * 
     * @param customer
     *            Name of customer folder
     * @param folder
     *            Name of platform module folder
     * @throws SubCommandException
     */
    private void runImportFind(final File rootFile, final Path lib) throws SubCommandException {
	System.out.println("Gathering imports...");
	ImportService iScanner = new ImportService(rootFile, lib);
	iScanner.scanAndImport();
    }

    /**
     * Maps pulled platform module to a file structure that can be imported onto
     * GTNexus system
     * 
     * @param root
     *            File path of platform module
     */
    private void map(File rootFile) throws SubCommandException {
	new AppXpressMapper(rootFile).doMapping();
    }

}
