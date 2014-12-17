package com.gtnexus.appxpress.pmbuilder;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.PMProperties;
import com.gtnexus.appxpress.cli.Asker;
import com.gtnexus.appxpress.cli.BoundIntegerAsker;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.file.filter.FileFilterFactory;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;
import static com.gtnexus.appxpress.AppXpressConstants.*;
/**
 * 
 * @author jdonovan
 *
 */
public class PlatformSelector {

	private static final String messageTemplate = "Please select one of the following:\n";
	private static final String optionTemplate = "%s) %s";
	private static AppXpressContext<BuilderOption> context;

	/**
	 * 
	 * @param dir
	 * @return
	 * @throws AppXpressException
	 */
	public static PlatformSelector createPlatformSelector(final File dir)
			throws PMBuilderException {
		validate(dir);
		final StringBuilder sb = new StringBuilder();
		final File[] directories = dir.listFiles(FileFilterFactory
				.directoriesOnly());
		if (directories.length == 0) {
			throw new PMBuilderException("There are no modules to select in this directory!");
		}
		sb.append(messageTemplate);
		for (int i = 0; i < directories.length; i++) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(String.format(optionTemplate, i + 1, directories[i]));
		}
		return new PlatformSelector(System.in, System.out, directories, sb.toString());
	}
	
	/**
	 * 
	 * @param dir
	 * @throws AppXpressException
	 */
	private static void validate(final File dir) throws PMBuilderException {
		PMProperties props = null;
		try {
			props = context.getPMProperties();
		} catch(AppXpressException e) {
			throw new PMBuilderException("Cant' select!", e);
		}
		final Properties properties = props.getProperties();
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

	private final Asker<Integer> asker;
	private final String message;
	private final File[] candidates;

	private PlatformSelector(InputStream in, PrintStream out,
			File[] candidates, String message) {
		this.asker = new BoundIntegerAsker(in, out, 0, candidates.length);
		this.candidates = candidates;
		this.message = message;
	}

	/**
	 * 
	 * @return
	 */
	public String select() {
		Integer selection = getSelectionFromUser();
		return candidates[selection].getName();
	}

	/**
	 * 
	 * @return
	 */
	public Integer getSelectionFromUser() {
		Integer selection = asker.ask(message);
		while (selection == null) {
			selection = asker.ask(Asker.INVALID_INPUT);
		}
		return selection + 1;
	}

}
