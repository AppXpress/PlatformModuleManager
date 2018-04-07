package com.gtnexus.pmm.api.v100.service;
import java.nio.file.Path;

import com.gtnexus.pmm.commons.properties.PMProperties;

public interface EnvironmentService {
    public Path getLocalDir();
    public Path getLibPath();
    public PMProperties getProperties();
    public String[] getRawArgs();
}
