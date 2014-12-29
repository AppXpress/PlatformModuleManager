package com.gtnexus.appxpress.context;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.commons.ApplicationInfo;
import com.gtnexus.appxpress.commons.DirectoryHelper;
import com.gtnexus.appxpress.commons.PMProperties;
import com.gtnexus.appxpress.commons.SimpleShutdown;

/**
 * 
 * @author jdonovan
 *
 * @param <T>
 */
public class AppXpressContext<T extends Enum<T> & AppXpressOption> implements
		SimpleShutdown, ApplicationInfo, TempResourceHolder {

	private final ApplicationInfo app;
	private final DirectoryHelper dHelper;
	private final Options options;
	private final Map<T, String> optMap;
	private final PMProperties properties;
	private final SimpleShutdown shutdown;
	private final List<File> delOnExit;

	public AppXpressContext(ApplicationInfo app, SimpleShutdown shutdown,
			DirectoryHelper dHelper, Options options, PMProperties properties,
			Map<T, String> optMap) {
		this.app = app;
		this.shutdown = shutdown;
		this.dHelper = dHelper;
		this.optMap = optMap;
		this.properties = properties;
		this.options = options;
		this.delOnExit = new LinkedList<>();
	}

	public ApplicationInfo getApplicationInfo() {
		return app;
	}

	public Map<T, String> getOptMap() {
		return optMap;
	}

	public PMProperties getPMProperties() throws AppXpressException {
		return dHelper.getPmProperties();
	}

	public String getProperty(AppXpressOption option) {
		return properties.getProperty(option.getLongName());
	}

	/**
	 * Displays the usage information and exits.
	 */
	public void displayHelpAndExit() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(app.getAppName(), app.getHelpHeader(), options,
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
	public String getAppName() {
		return app.getAppName();
	}

	@Override
	public <M extends Enum<M> & AppXpressOption> Class<M> getContextType() {
		return app.getContextType();
	}

	public void deleteOnExit(File f) {
		delOnExit.add(f);
	}

	public List<File> getFilesToDeleteOnExit() {
		return delOnExit;
	}

}
