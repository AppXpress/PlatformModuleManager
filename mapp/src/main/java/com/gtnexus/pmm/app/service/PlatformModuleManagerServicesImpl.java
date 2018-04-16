package com.gtnexus.pmm.app.service;

import com.gtnexus.pmm.api.v100.PMProperties;
import com.gtnexus.pmm.api.v100.service.EnvironmentService;
import com.gtnexus.pmm.api.v100.service.FileService;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.api.v100.service.TemporaryResourceService;
import com.gtnexus.pmm.api.v100.service.ZipService;

public class PlatformModuleManagerServicesImpl implements PlatformModuleManagerServices {
   
    private final EnvironmentService es;
    private final FileService fs;
    private final TemporaryResourceService ts;
    private final ZipService zs;
    
    public PlatformModuleManagerServicesImpl(PMProperties properties, String[] args) {
	this.fs = new FileServiceImpl();
	this.zs = new ZipServiceImpl();
	this.es = new EnvironmentServiceImpl(properties, args);
	this.ts = new TemporaryResourceServiceImpl();
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
	return ts;
    }

    @Override
    public EnvironmentService getEnvironmentService() {
	return this.es;
    }

}
