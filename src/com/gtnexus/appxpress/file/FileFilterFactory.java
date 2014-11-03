package com.gtnexus.appxpress.file;

import java.io.File;
import java.io.FileFilter;

public class FileFilterFactory {

	public static FileFilter FileNameContains(final String string) {
		return containsFilter(true, string);
	}

	public static FileFilter FileNameDoesNotContain(final String string) {
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
	
}
