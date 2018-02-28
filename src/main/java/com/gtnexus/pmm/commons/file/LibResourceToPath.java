package com.gtnexus.pmm.commons.file;

import java.nio.file.Path;

public class LibResourceToPath implements NameToPath {

    private final Path libPath;

    public LibResourceToPath(Path libPath) {
	this.libPath = libPath;
    }

    @Override
    public Path resolve(String name) {
	return libPath.resolve(name);
    }

}
