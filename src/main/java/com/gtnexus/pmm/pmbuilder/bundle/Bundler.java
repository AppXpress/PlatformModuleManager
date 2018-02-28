package com.gtnexus.pmm.pmbuilder.bundle;

import java.io.File;

import com.gtnexus.pmm.AppXpressException;

public interface Bundler {

    public void bundle(File directory) throws AppXpressException;

}
