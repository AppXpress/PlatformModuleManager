package com.gtnexus.pmm.api.v100.service;
import java.nio.file.Path;
import java.util.Properties;

public interface EnvironmentService {
    public Path getLocalDir();
    public Path getCWD();
    public Path getAppXDir();
    public Path getLibPath();
    public Properties getProperties();
    public String[] getRawArgs();
}
