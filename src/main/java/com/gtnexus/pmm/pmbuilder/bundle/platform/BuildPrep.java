package com.gtnexus.pmm.pmbuilder.bundle.platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.gtnexus.pmm.AppXpressDirResolver;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.TemporaryResourceService;
import com.gtnexus.pmm.commons.Mapper;
import com.gtnexus.pmm.commons.Preparation;
import com.gtnexus.pmm.commons.file.FileService;
import com.gtnexus.pmm.context.TempResourceHolder;
import com.gtnexus.pmm.pmbuilder.cli.PMBuilderVO;
import com.gtnexus.pmm.pmbuilder.exception.PMBuilderException;
import com.gtnexus.pmm.pmbuilder.scriptimport.ImportService;

/**
 * 
 * @author jdonovan
 *
 */
public class BuildPrep implements Preparation<PMBuilderVO> {

    private final FileService fs;
    private final AppXpressDirResolver resolver;
    private final TempResourceHolder tmpHolder;
    private final TemporaryResourceService tempService;
    private final Path libPath;

    public BuildPrep(TempResourceHolder tmp, Path libPath) {
	this.fs = new FileService();
	this.resolver = new AppXpressDirResolver();
	this.tmpHolder = tmp;
	this.libPath = libPath;
	this.tempService = null;
    }

    public BuildPrep(FileService fs, TemporaryResourceService tmpService, Path libPath) {
	this.fs = fs;
	this.resolver = new AppXpressDirResolver();
	this.tmpHolder = null;
	this.libPath = libPath;
	this.tempService = tmpService;
    }

    @Override
    public void prepare(final PMBuilderVO vo) throws PMBuilderException {
	try {
	    File tmp = createTemp(vo);
	    vo.setWorkingDir(tmp);
	    runImportFind(tmp, libPath);
	    map(tmp);
	} catch (AppXpressException | IOException e) {
	    throw new PMBuilderException("Exception when mapping file structure.", e);
	}
    }

    private File createTemp(final PMBuilderVO vo) throws IOException {
	Path source = vo.getRootFile().toPath();
	Path tmpPath = resolver.resolveAppXpressDir().toAbsolutePath();
	String tmpPrefix = String.valueOf(System.currentTimeMillis());
	Path destination = Files.createTempDirectory(tmpPath, tmpPrefix);
	File dest = destination.toFile();
	if (tmpHolder != null) {
	    tmpHolder.deleteOnExit(dest);
	} else {
	    tempService.markForDeletion(dest);
	}
	fs.copyDirectory(source, destination);
	return dest;
    }

    /**
     * Iterates through folder customer/customer/folder
     * 
     * @param customer
     *            Name of customer folder
     * @param folder
     *            Name of platform module folder
     * @throws AppXpressException
     */
    private void runImportFind(final File rootFile, final Path lib) throws AppXpressException {
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
    private void map(File rootFile) throws AppXpressException {
	Mapper mapper = new AppXpressMapper(rootFile);
	mapper.doMapping();
    }

}
