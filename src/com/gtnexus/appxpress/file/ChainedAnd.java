package com.gtnexus.appxpress.file;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ChainedAnd extends FileFilterChain {

	public ChainedAnd() {
		filters = new LinkedList<>();
	}

	public ChainedAnd(FileFilter filter) {
		super();
		add(filter);
	}

	public ChainedAnd(List<FileFilter> filters) {
		super();
		filters.addAll(filters);
	}

	public ChainedAnd(FileFilter... filters) {
		super();
		this.filters.addAll(Arrays.asList(filters));
	}
	
	@Override
	public boolean accept(File file) {
		Iterator<FileFilter> i = filters.iterator();
		boolean accept = true;
		while(i.hasNext() && accept) {
			accept = i.next().accept(file);
		}
		return accept;
	}
	
}