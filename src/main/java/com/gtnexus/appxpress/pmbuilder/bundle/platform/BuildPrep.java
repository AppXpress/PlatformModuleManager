package com.gtnexus.appxpress.pmbuilder.bundle.platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.commons.Preparation;
import com.gtnexus.appxpress.commons.file.FileService;
import com.gtnexus.appxpress.context.TempResourceHolder;
import com.gtnexus.appxpress.exception.AppXpressException;
import com.gtnexus.appxpress.pmbuilder.cli.PMBuilderVO;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;
import com.gtnexus.appxpress.pmbuilder.scriptimport.ImportService;
import com.gtnexus.appxpress.pmextractor.gitmap.Mapper;

/**
 * 
 * @author jdonovan
 *
 */
public class BuildPrep implements Preparation<PMBuilderVO> {

	private final FileService fs;
	private final AppXpressDirResolver resolver;
	private final TempResourceHolder tmpHolder;
	private final Path libPath;

	public BuildPrep(TempResourceHolder tmp, Path libPath) {
		this.fs = new FileService();
		this.resolver = new AppXpressDirResolver();
		this.tmpHolder = tmp;
		this.libPath = libPath;
	}

	@Override
	public void prepare(final PMBuilderVO vo) throws PMBuilderException {
		try {
			File tmp = createTemp(vo);
			vo.setWorkingDir(tmp);
			runImportFind(tmp, libPath);
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
		tmpHolder.deleteOnExit(dest);
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
