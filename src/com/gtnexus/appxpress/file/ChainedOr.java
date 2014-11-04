package com.gtnexus.appxpress.file;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ChainedOr extends FileFilterChain {

	public ChainedOr() {
		super();
	}

	public ChainedOr(FileFilter filter) {
		super();
		add(filter);
	}

	public ChainedOr(List<FileFilter> filters) {
		super();
		filters.addAll(filters);
	}

	public ChainedOr(FileFilter... filters) {
		super();
		this.filters.addAll(Arrays.asList(filters));
	}
	
	@Override
	public boolean accept(File file) {
		Iterator<FileFilter> i = filters.iterator();
		while(i.hasNext()) {
			if(i.next().accept(file)) {
				return true;
			}
		}
		return false;
	}

}
