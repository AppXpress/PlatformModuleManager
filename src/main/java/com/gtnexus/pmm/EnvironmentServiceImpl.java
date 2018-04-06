package com.gtnexus.pmm;

import static com.gtnexus.pmm.AppXpressConstants.LIB;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.gtnexus.pmm.cli.option.options.LocalDirOption;
import com.gtnexus.pmm.commons.properties.PMProperties;

public class EnvironmentServiceImpl implements EnvironmentService {

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
    public Path getLibPath() {
	Path ld = this.getLocalDir();
	return ld.getParent().resolve(LIB);
    }

    @Override
    public PMProperties getProperties() {
	return this.properties;
    }

    @Override
    public String[] getRawArgs() {
	return this.rawArgs;
    }

}
