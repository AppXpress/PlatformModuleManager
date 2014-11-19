package com.gtnexus.appxpress.pmbuilder.bundle.platform;

import java.io.File;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.ZipService;
import com.gtnexus.appxpress.pmbuilder.bundle.Bundler;

public class PlatformModuleBundler implements Bundler {

	private final ZipService zs;
	private final File root;

	public PlatformModuleBundler(File root) {
		this.zs = new ZipService();
		this.root = root;
	}

	@Override
	public void bundle(File dir) throws AppXpressException {
		zs.zipDirectory(dir, root);
	}

}
