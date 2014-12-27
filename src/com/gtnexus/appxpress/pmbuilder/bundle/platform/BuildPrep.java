package com.gtnexus.appxpress.pmbuilder.bundle.platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.commons.Preparation;
import com.gtnexus.appxpress.commons.file.FileService;
import com.gtnexus.appxpress.pmbuilder.cli.PMBuilderVO;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;
import com.gtnexus.appxpress.pmbuilder.scriptimport.ImportService;
import com.gtnexus.appxpress.pmextractor.gitmap.Mapper;

public class BuildPrep implements Preparation<PMBuilderVO> {

	private final FileService fs;
	private final AppXpressDirResolver resolver;

	public BuildPrep() {
		this.fs = new FileService();
		this.resolver = new AppXpressDirResolver();
	}

	@Override
	public void prepare(final PMBuilderVO vo) throws PMBuilderException {
		try {
			File tmp = createTemp(vo);
			vo.setWorkingDir(tmp);
			runImportFind(tmp);
			//xmlDesignCustomObjectScriptMatcher(tmp);
			map(tmp);
		} catch (AppXpressException | IOException e) {
			throw new PMBuilderException(
					"Exception when mapping file structure.", e);
		}
	}

	private File createTemp(final PMBuilderVO vo) throws IOException {
		Path source = vo.getRootFile().toPath();
		Path tmpPath = resolver
				.resolveAppXpressDir()
				.toAbsolutePath();
		String tmpPrefix = String.valueOf(System.currentTimeMillis());
		Path destination = Files
				.createTempDirectory(tmpPath, tmpPrefix);
		File dest = destination.toFile();
		dest.deleteOnExit();
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
	private void runImportFind(File rootFile) throws AppXpressException {
		System.out.println("Gathering imports...");
		ImportService iScanner = new ImportService(rootFile);
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
