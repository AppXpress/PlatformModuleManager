package com.gtnexus.appxpress.commons.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.List;

public class ChainedAnd extends FileFilterChain {

    public ChainedAnd() {
	super();
    }

    public ChainedAnd(FileFilter filter) {
	super(filter);
    }

    public ChainedAnd(List<FileFilter> filters) {
	super(filters);
    }

    public ChainedAnd(FileFilter... filters) {
	super(filters);
    }

    public ChainedAnd and(FileFilter filter) {
	super.add(filter);
	return this;
    }

    @Override
    public boolean accept(File file) {
	Iterator<FileFilter> i = filters.iterator();
	boolean accept = true;
	while (i.hasNext() && accept) {
	    accept = i.next().accept(file);
	}
	return accept;
    }

}