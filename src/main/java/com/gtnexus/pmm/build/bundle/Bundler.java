package com.gtnexus.pmm.build.bundle;

import java.io.File;

import com.gtnexus.pmm.api.v100.command.SubCommandException;

public interface Bundler {

    public void bundle(File directory) throws SubCommandException;

}
