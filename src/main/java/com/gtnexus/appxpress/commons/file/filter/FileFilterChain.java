package com.gtnexus.appxpress.commons.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

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
	
	public int size(File dir) {
		return dir.listFiles(this).length;
	}
	
	public boolean hasResults(File dir) {
		return this.size(dir) > 0;
	}
	
	public Collection<Path> listPaths(File dir) {
		return Collections2.transform(Lists.newArrayList(dir.listFiles(this)), new Function<File, Path>() {
			@Override
			public Path apply(File input) {
				return input.toPath();
			}
		});
	}
	
	@Override
	public abstract boolean accept(File file);
}
