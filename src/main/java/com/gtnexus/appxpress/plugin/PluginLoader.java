package com.gtnexus.appxpress.plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.commons.file.filter.FileFilterFactory;

public class PluginLoader {
	
	public void findPlugins() {
		Path appxDirPath = AppXpressDirResolver.Helper.resolveAppxpressDir();
		File appxDir = appxDirPath.toFile();
		if(appxDir.exists()) { //TODO:we have this somewhere already ...
			File[] jars = appxDir.listFiles(FileFilterFactory.isJar());
			
		}
	}
	
	protected void asPayload(File jar) {
		
	}
	
	protected void load() {
		URL[] urls = new URL[]{};
		String canonicalName = null;
		ClassLoader loader = URLClassLoader.newInstance(urls);
		try {
			Class<?> plugin = loader.loadClass(canonicalName);
			Object newInstance = plugin.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class Payload {
		private boolean isEligible;
		private String canonicalName;
		private File jar;
	}
}

