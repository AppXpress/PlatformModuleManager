package com.gtnexus.appxpress.context;

import static com.gtnexus.appxpress.commons.AppXpressConstants.LIB;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.commons.CommandInformation;
import com.gtnexus.appxpress.commons.DirectoryHelper;
import com.gtnexus.appxpress.commons.PMProperties;
import com.gtnexus.appxpress.commons.PropertiesPersister;
import com.gtnexus.appxpress.commons.SimpleShutdown;
import com.gtnexus.appxpress.commons.exception.AppXpressException;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;

/**
 * 
 * @author jdonovan
 *
 * @param <T>
 */
public class AppXpressContext implements
		SimpleShutdown, CommandInformation, TempResourceHolder {

	private final CommandInformation app;
	private final DirectoryHelper dHelper;
	private final Options options;
	private final Map<AppXpressOption, String> optMap;
	private final PMProperties properties;
	private final SimpleShutdown shutdown;
	private final List<File> delOnExit;
	private boolean terminatedRegulary;

	public AppXpressContext(CommandInformation app, SimpleShutdown shutdown,
			DirectoryHelper dHelper, Options options, PMProperties properties,
			Map<AppXpressOption, String> optMap) {
		this.app = app;
		this.shutdown = shutdown;
		this.dHelper = dHelper;
		this.optMap = optMap;
		this.properties = properties;
		this.options = options;
		this.delOnExit = new LinkedList<>();
		this.terminatedRegulary = true;
	}

	public CommandInformation getApplicationInfo() {
		return app;
	}

	public Map<AppXpressOption, String> getOptMap() {
		return optMap;
	}
	
	public Path getLibPath() {
		String ld = properties.getProperty(BuilderOption.LOCAL_DIR);
		Path localDir = Paths.get(ld);
		return localDir.getParent().resolve(LIB);
	}

	public PMProperties getPMProperties() throws AppXpressException {
		return dHelper.getPmProperties();
	}

	public String getProperty(AppXpressOption option) {
		return properties.getProperty(option.getLongName());
	}

	public void displayHelpAndExit() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(app.getCommandName(), app.getHelpHeader(), options,
				app.getHelpFooter());
		shutdown();
	}

	public String getHelpHeader() {
		return app.getHelpHeader();
	}

	public String getHelpFooter() {
		return app.getHelpHeader();
	}

	@Override
	public void shutdown() {
		shutdown.shutdown();
	}

	@Override
	public void shutdown(String message) {
		shutdown.shutdown(message);
	}

	@Override
	public String getCommandName() {
		return app.getCommandName();
	}

	@Override
	public <M extends AppXpressOption> Class<M> getContextType() {
		return app.getContextType();
	}

	public void deleteOnExit(File f) {
		delOnExit.add(f);
	}

	public List<File> getFilesToDeleteOnExit() {
		return delOnExit;
	}
	
	public boolean propertiesWereChanged() {
		return properties.haveChanged();
	}
	
	public void presentSaveOption() throws AppXpressException {
		PropertiesPersister sister = new PropertiesPersister(properties);
		sister.presentSaveOption();
	}

	public boolean isTerminatedRegulary() {
		return terminatedRegulary;
	}

	public void setTerminatedRegulary(boolean terminatedRegulary) {
		this.terminatedRegulary = terminatedRegulary;
	}
	
}
