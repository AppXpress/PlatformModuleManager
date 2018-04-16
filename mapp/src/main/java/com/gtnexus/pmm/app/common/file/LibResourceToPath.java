package com.gtnexus.pmm.app.common.file;

import java.nio.file.Path;

import com.gtnexus.pmm.api.v100.NameToPath;

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
