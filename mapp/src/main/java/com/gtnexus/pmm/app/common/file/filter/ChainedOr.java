package com.gtnexus.pmm.app.common.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.List;

public class ChainedOr extends FileFilterChain {

    public ChainedOr() {
	super();
    }

    public ChainedOr(FileFilter filter) {
	super(filter);
    }

    public ChainedOr(List<FileFilter> filters) {
	super(filters);
    }

    public ChainedOr(FileFilter... filters) {
	super(filters);
    }

    @Override
    public boolean accept(File file) {
	if (filters.size() == 0)
	    return true;
	Iterator<FileFilter> i = filters.iterator();
	while (i.hasNext()) {
	    if (i.next().accept(file)) {
		return true;
	    }
	}
	return false;
    }

}
