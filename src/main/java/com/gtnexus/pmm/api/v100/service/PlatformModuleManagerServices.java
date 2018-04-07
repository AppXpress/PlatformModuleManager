package com.gtnexus.pmm.api.v100.service;
import com.gtnexus.pmm.commons.ZipService;
import com.gtnexus.pmm.commons.file.FileService;

public interface PlatformModuleManagerServices {
    public ZipService getZipService();
    public FileService getFileService();
    public TemporaryResourceService getTemporaryResourceService();
    public EnvironmentService getEnvironmentService();
    
    //public SelectorService getPromptService(); for Select
    //public CleanupService getCleanupService
}
