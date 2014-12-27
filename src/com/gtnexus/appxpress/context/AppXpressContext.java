package com.gtnexus.appxpress.context;

import java.util.Map;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.commons.DirectoryHelper;
import com.gtnexus.appxpress.commons.PMProperties;
import com.gtnexus.appxpress.commons.SimpleShutdown;

/**
 * 
 * @author jdonovan
 *
 * @param <T>
 */
public class AppXpressContext<T extends Enum<T> & AppXpressOption> implements SimpleShutdown{

	private final String appName;
	@SuppressWarnings("unused")
	private final Class<T> contextType;
	private final DirectoryHelper dHelper;
	private final Options options;
	private final Map<T, String> optMap;
	private final PMProperties properties;
	private final SimpleShutdown shutdown;
	private String helpHeader;
	private String helpFooter; //TODO

	public AppXpressContext(String appName, Class<T> contextType, SimpleShutdown shutdown,
			DirectoryHelper dHelper, Options options, PMProperties properties,
			Map<T, String> optMap) {
		this.appName = appName;
		this.contextType = contextType;
		this.shutdown = shutdown;
		this.dHelper = dHelper;
		this.optMap = optMap;
		this.properties = properties;
		this.options = options;
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
		formatter.printHelp(appName, helpHeader, options, helpFooter);
		shutdown();
	}

	public String getHelpHeader() {
		return helpHeader;
	}

	public void setHelpHeader(String helpHeader) {
		this.helpHeader = helpHeader;
	}

	public String getHelpFooter() {
		return helpFooter;
	}

	public void setHelpFooter(String helpFooter) {
		this.helpFooter = helpFooter;
	}

	@Override
	public void shutdown() {
		shutdown.shutdown();
		
	}

	@Override
	public void shutdown(String message) {
		shutdown.shutdown(message);
	}
	
}
