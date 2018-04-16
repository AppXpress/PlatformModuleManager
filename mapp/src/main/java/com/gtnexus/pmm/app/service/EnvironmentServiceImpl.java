package com.gtnexus.pmm.app.service;

import static com.gtnexus.pmm.app.AppXpressConstants.LIB;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.gtnexus.pmm.api.v100.cli.option.options.LocalDirOption;
import com.gtnexus.pmm.api.v100.service.EnvironmentService;
import com.gtnexus.pmm.app.common.PMProperties;

public class EnvironmentServiceImpl implements EnvironmentService {

    private static final String CWD = "user.dir";
    private final PMProperties properties;
    private final String[] rawArgs;

    public EnvironmentServiceImpl(PMProperties properties, String[] rawArgs) {
	this.properties = properties;
	this.rawArgs = rawArgs;
    }

    @Override
    public Path getLocalDir() {
	String ld = this.properties.getProperty(LocalDirOption.NAME);
	return Paths.get(ld);
    }
    
    @Override
    public Path getCWD() {
	return Paths.get(System.getProperty(CWD)).toAbsolutePath();
    }
    
    @Override
    public Path getAppXDir() {
	throw new RuntimeException("Unimplemtned");
    }

    @Override
    public Path getLibPath() {
	Path ld = this.getLocalDir();
	return ld.getParent().resolve(LIB);
    }

    @Override
    public Properties getProperties() {
	return this.properties.getProperties();
    }

    @Override
    public String[] getRawArgs() {
	return this.rawArgs;
    }

}
