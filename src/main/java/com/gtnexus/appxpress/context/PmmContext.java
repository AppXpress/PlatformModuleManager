package com.gtnexus.appxpress.context;

import static com.gtnexus.appxpress.AppXpressConstants.LIB;

import java.nio.file.Path;
import java.nio.file.Paths;
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
public class PmmContext<T extends CLICommandOption> {

    private final PMMCommandInfo app;
    private final DirectoryHelper dHelper;
    private final Options options;
    private final Map<CLICommandOption, String> optMap;
    private final PMProperties properties;
    private final SimpleShutdown shutdown;
    private final TempResourceHolderImpl tempResourceHolder;
    private boolean terminatedRegulary;

    public PmmContext(PMMCommandInfo app, SimpleShutdown shutdown, DirectoryHelper dHelper, Options options,
	    PMProperties properties, Map<CLICommandOption, String> optMap) {
	this.app = app;
	this.shutdown = shutdown;
	this.dHelper = dHelper;
	this.optMap = optMap;
	this.properties = properties;
	this.options = options;
	this.terminatedRegulary = true;
	this.tempResourceHolder = new TempResourceHolderImpl();
    }

    public TempResourceHolderImpl getTempResourceHolder() {
        return tempResourceHolder;
    }

    public PMMCommandInfo getApplicationInfo() {
	return app;
    }

    public Map<CLICommandOption, String> getOptMap() {
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
	shutdown.shutdown();
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

    public <M extends CLICommandOption> Set<M> getOptions() {
	return null;
    }

}
