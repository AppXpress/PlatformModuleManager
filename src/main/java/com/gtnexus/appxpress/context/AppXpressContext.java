package com.gtnexus.appxpress.context;

import static com.gtnexus.appxpress.AppXpressConstants.LIB;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.gtnexus.appxpress.cli.option.CLICommandOption;
import com.gtnexus.appxpress.commons.DirectoryHelper;
import com.gtnexus.appxpress.commons.command.PMMCommandInfo;
import com.gtnexus.appxpress.commons.properties.PMProperties;
import com.gtnexus.appxpress.commons.properties.PropertiesPersister;
import com.gtnexus.appxpress.commons.runtime.SimpleShutdown;
import com.gtnexus.appxpress.exception.AppXpressException;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;

/**
 * @author jdonovan
 *
 * @param <T>
 */
public class AppXpressContext<T extends CLICommandOption>
	implements SimpleShutdown, PMMCommandInfo, TempResourceHolder {

    private final PMMCommandInfo app;
    private final DirectoryHelper dHelper;
    private final Options options;
    private final Map<T, String> optMap;
    private final PMProperties properties;
    private final SimpleShutdown shutdown;
    private final List<File> delOnExit;
    private boolean terminatedRegulary;

    public AppXpressContext(PMMCommandInfo app, SimpleShutdown shutdown, DirectoryHelper dHelper, Options options,
	    PMProperties properties, Map<T, String> optMap) {
	this.app = app;
	this.shutdown = shutdown;
	this.dHelper = dHelper;
	this.optMap = optMap;
	this.properties = properties;
	this.options = options;
	this.delOnExit = new LinkedList<>();
	this.terminatedRegulary = true;
    }

    public PMMCommandInfo getApplicationInfo() {
	return app;
    }

    public Map<T, String> getOptMap() {
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

    public String getProperty(CLICommandOption option) {
	return properties.getProperty(option.getLongName());
    }

    public void displayHelpAndExit() {
	HelpFormatter formatter = new HelpFormatter();
	formatter.printHelp(app.getName(), app.getHelpHeader(), options, app.getHelpFooter());
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
    public String getName() {
	return app.getName();
    }

    @Override
    public Class<?> getContextType() {
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

    @Override
    public <M extends CLICommandOption> Set<M> getOptions() {
	return null;
    }

}
