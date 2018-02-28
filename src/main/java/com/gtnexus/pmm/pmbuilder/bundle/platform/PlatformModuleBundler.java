package com.gtnexus.pmm.pmbuilder.bundle.platform;

import java.io.File;

import com.gtnexus.pmm.AppXpressConstants;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.commons.ZipService;
import com.gtnexus.pmm.pmbuilder.bundle.Bundler;

public class PlatformModuleBundler implements Bundler {

    private final ZipService zs;
    private final File root;

    public PlatformModuleBundler(File root) {
	this.zs = new ZipService(AppXpressConstants.IGNORE_SET);
	this.root = root;
    }

    @Override
    public void bundle(File dir) throws AppXpressException {
	zs.zipDirectory(dir, root);
    }

}
