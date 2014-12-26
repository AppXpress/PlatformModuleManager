package com.gtnexus.appxpress.pmbuilder;

import static com.gtnexus.appxpress.AppXpressConstants.CUSTOMER;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.PMProperties;
import com.gtnexus.appxpress.file.filter.FileFilterFactory;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

public class PlatformSelectorFactory {

	private static final String messageTemplate = "Please select one of the following:\n";
	private static final String optionTemplate = "%s) %s";
	
	public PlatformSelectorFactory() {
	}
	
	/**
	 * 
	 * @param dir
	 * @return
	 * @throws AppXpressException
	 */
	public PlatformSelector createPlatformSelector(final File dir, PMProperties properties)
			throws PMBuilderException {
		if(dir == null) {
			throw new NullPointerException("Cannot Select from a null dir!");
		}
		validate(dir, properties);
		final StringBuilder sb = new StringBuilder();
		final File[] directories = dir.listFiles(FileFilterFactory
				.directoriesOnly());
		if (directories.length == 0) {
			throw new PMBuilderException("There are no modules to select in this directory!");
		}
		sb.append(messageTemplate);
		for (int i = 0; i < directories.length; i++) {
			sb.append("\n");
			sb.append(String.format(optionTemplate, i + 1, directories[i]));
		}
		return new PlatformSelector(System.in, System.out, directories, sb.toString());
	}
	
	/**
	 * 
	 * @param dir
	 * @throws AppXpressException
	 */
	private void validate(final File dir, final PMProperties properties) throws PMBuilderException {
		final String localDir = properties.getProperty(BuilderOption.LOCAL_DIR
				.getLongName());
		if (localDir == null || localDir.isEmpty()) {
			throw new PMBuilderException(
					"Local Directory property is not set. "
							+ "Please check your AppXpress properties file"
							+ "before trying to run the Select option again.");
		}
		if(!dir.isDirectory()) {
			throw new PMBuilderException("Cannot select from here!");
		}
		Path ldPath = Paths.get(localDir);
		Path cPath = ldPath.resolve(CUSTOMER);
		if(cPath.compareTo(dir.toPath()) <= 0) {
			throw new PMBuilderException(
					"The Select option must be run from a customer "
							+ "folder on the local directory path");
		}
	}
	
	
}
