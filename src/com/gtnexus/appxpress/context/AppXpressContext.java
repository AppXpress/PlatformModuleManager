package com.gtnexus.appxpress.context;

import java.util.Map;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.DirectoryHelper;
import com.gtnexus.appxpress.PMProperties;
import com.gtnexus.appxpress.cli.option.AppXpressOption;

/**
 * 
 * @author jdonovan
 *
 * @param <T>
 */
public class AppXpressContext<T extends Enum<T> & AppXpressOption> {
	
	
	public enum Type {
		Extractor, Option;
	}
	
	private final String appName;
	private final Class<T> contextType;
	private final DirectoryHelper dHelper;
	private final Map<T, String> optMap;

	public AppXpressContext(String appName, Class<T> contextType, DirectoryHelper dHelper, Map<T, String> optMap) {
		this.appName = appName;
		this.contextType = contextType;
		this.dHelper = dHelper;
		this.optMap = optMap;
	}
	
	public void shutdown() {
		//TODO register shutdown hook
		System.exit(0);
	}
	
	public Map<T, String> getOptMap() {
		return optMap;
	}
	
	public PMProperties getPMProperties() throws AppXpressException {
		return dHelper.getPmProperties();
	}
	
	public String getAppName() {
		return appName;
	}
	
}
