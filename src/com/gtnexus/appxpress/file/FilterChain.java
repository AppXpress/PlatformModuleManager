package com.gtnexus.appxpress.file;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FilterChain implements FileFilter {
	
	private List<FileFilter> filters;
	
	public FilterChain() {
		filters = new LinkedList<>();
	}
	
	public FilterChain(FileFilter filter) {
		this();
		add(filter);
	}
	
	public FilterChain(List<FileFilter>  filters) {
		this();
		filters.addAll(filters);
	}
	
	public FilterChain(FileFilter... filters) {
		this();
		this.filters.addAll(Arrays.asList(filters));
	}
	
	public void add(FileFilter filter) {
		filters.add(filter);
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