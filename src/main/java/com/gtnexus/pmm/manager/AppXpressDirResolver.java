package com.gtnexus.pmm.manager;

import static com.gtnexus.pmm.AppXpressConstants.DIR_TEMPLATE;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppXpressDirResolver {

    private static final String USER_HOME = "user.home";
    private static final String CURRENT_DIR = "user.dir";
    private final String homeDir;
    private final File appXpressDir;

    public AppXpressDirResolver() {
	this.homeDir = System.getProperty(USER_HOME);
	this.appXpressDir = new File(String.format(DIR_TEMPLATE, this.homeDir));
    }

    public Path resolveAppXpressDir() {
	return appXpressDir.toPath();
    }

    public Path resovleCurrentDirectory() {
	String cwd = System.getProperty(CURRENT_DIR);
	return Paths.get(cwd).toAbsolutePath();
    }

}
