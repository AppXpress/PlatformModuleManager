package com.gtnexus.appxpress.file.filter;

import java.io.File;
import java.io.FileFilter;

public class FileFilterFactory {

	public static FileFilter fileNameContains(final String string) {
		return containsFilter(true, string);
	}

	public static FileFilter fileNameDoesNotContain(final String string) {
		return containsFilter(false, string);
	}

	private static FileFilter containsFilter(final boolean contains,
			final String string) {
		return new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(contains) {
					return pathname.getName().contains(string);
				} else {
					return !pathname.getName().contains(string);
				}
			}
		};
	}
	
	public static FileFilter directoriesOnly() {
		return new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
		};
	}
	
	public static FileFilter endsWith(final String string) {
		return new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(string); 
			}
		};
	}
	
	public static FileFilter doesNotEndWith(final String string) {
		return new FileFilter() {
			@Override
			public boolean accept(File f) {
				return !f.getName().endsWith(string); 
			}
		};
	}
	
}
