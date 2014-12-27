package com.gtnexus.appxpress.commons.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class FileFilterChain implements FileFilter {

	protected List<FileFilter> filters;
	
	public FileFilterChain() {
		filters = new LinkedList<>();
	}

	public FileFilterChain(FileFilter filter) {
		this();
		add(filter);
	}

	public FileFilterChain(List<FileFilter> filters) {
		this();
		filters.addAll(filters);
	}

	public FileFilterChain(FileFilter... filters) {
		this();
		this.filters.addAll(Arrays.asList(filters));
	}

	public void add(FileFilter filter) {
		filters.add(filter);
	}
	
	@Override
	public abstract boolean accept(File file);
}
