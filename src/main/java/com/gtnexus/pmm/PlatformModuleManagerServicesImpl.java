package com.gtnexus.pmm;

import com.gtnexus.pmm.commons.ZipService;
import com.gtnexus.pmm.commons.file.FileService;

public class PlatformModuleManagerServicesImpl implements PlatformModuleManagerServices {
   
    private final FileService fs;
    private final ZipService zs;
    
    public PlatformModuleManagerServicesImpl() {
	this.fs = new FileService();
	this.zs = new ZipService();
    }

    @Override
    public ZipService getZipService() {
	return this.zs;
    }

    @Override
    public FileService getFileService() {
	return this.fs;
    }

}
