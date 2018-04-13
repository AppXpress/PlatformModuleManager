package com.gtnexus.pmm.build.bundle;

import java.io.File;

import com.gtnexus.pmm.AppXpressException;

public interface Bundler {

    public void bundle(File directory) throws AppXpressException;

}
