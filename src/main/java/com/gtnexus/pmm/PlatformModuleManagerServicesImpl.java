package com.gtnexus.pmm;

import com.gtnexus.pmm.commons.ZipService;
import com.gtnexus.pmm.commons.file.FileService;
import com.gtnexus.pmm.commons.properties.PMProperties;

public class PlatformModuleManagerServicesImpl implements PlatformModuleManagerServices {
   
    private final EnvironmentService es;
    private final FileService fs;
    private final ZipService zs;
    
    public PlatformModuleManagerServicesImpl(PMProperties properties, String[] args) {
	this.fs = new FileService();
	this.zs = new ZipService();
	this.es = new EnvironmentServiceImpl(properties, args);
    }

    @Override
    public ZipService getZipService() {
	return this.zs;
    }

    @Override
    public FileService getFileService() {
	return this.fs;
    }
    
    @Override
    public TemporaryResourceService getTemporaryResourceService() {
	return null;
    }

    @Override
    public EnvironmentService getEnvironmentService() {
	return this.es;
    }

}
