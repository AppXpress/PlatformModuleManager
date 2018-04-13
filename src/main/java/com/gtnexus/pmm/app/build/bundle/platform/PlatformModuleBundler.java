package com.gtnexus.pmm.app.build.bundle.platform;

import java.io.File;

import com.gtnexus.pmm.api.v100.command.SubCommandException;
import com.gtnexus.pmm.api.v100.service.ZipService;
import com.gtnexus.pmm.app.AppXpressConstants;
import com.gtnexus.pmm.app.build.bundle.Bundler;
import com.gtnexus.pmm.app.service.ZipServiceImpl;

public class PlatformModuleBundler implements Bundler {

    private final ZipService zs;
    private final File root;

    public PlatformModuleBundler(File root) {
	this.zs = new ZipServiceImpl(AppXpressConstants.IGNORE_SET);
	this.root = root;
    }

    @Override
    public void bundle(File dir) throws SubCommandException {
	zs.zipDirectory(dir, root);
    }

}