package com.gtnexus.appxpress.pmbuilder.bundle.platform;

import java.io.File;

import com.gtnexus.appxpress.ZipService;
import com.gtnexus.appxpress.pmbuilder.bundle.Bundler;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

public class PlatformModuleBundler implements Bundler {

	private final ZipService zipper;
	
	public PlatformModuleBundler() {
		zipper = new ZipService();
	}
	
	@Override
	public void bundle(File root) throws PMBuilderException {
		zipper.zipDirectory(root);
	}

}
