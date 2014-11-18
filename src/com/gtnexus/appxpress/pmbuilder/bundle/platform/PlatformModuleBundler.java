package com.gtnexus.appxpress.pmbuilder.bundle.platform;

import java.io.File;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.ZipService;
import com.gtnexus.appxpress.pmbuilder.bundle.Bundler;

public class PlatformModuleBundler implements Bundler {

	private final ZipService zipper;
	
	public PlatformModuleBundler() {
		zipper = new ZipService();
	}
	
	@Override
	public void bundle(File root) throws AppXpressException {
		zipper.zipDirectory(root);
	}

}
