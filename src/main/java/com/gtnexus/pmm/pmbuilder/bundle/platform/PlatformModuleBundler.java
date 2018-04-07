package com.gtnexus.pmm.pmbuilder.bundle.platform;

import java.io.File;

import com.gtnexus.pmm.AppXpressConstants;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.api.v100.service.ZipService;
import com.gtnexus.pmm.pmbuilder.bundle.Bundler;
import com.gtnexus.pmm.service.ZipServiceImpl;

public class PlatformModuleBundler implements Bundler {

    private final ZipService zs;
    private final File root;

    public PlatformModuleBundler(File root) {
	this.zs = new ZipServiceImpl(AppXpressConstants.IGNORE_SET);
	this.root = root;
    }

    @Override
    public void bundle(File dir) throws AppXpressException {
	zs.zipDirectory(dir, root);
    }

}
