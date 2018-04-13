package com.gtnexus.pmm.common.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

public class FileFilterFactory {

    public static FileFilter fileNameContains(final String string) {
	return containsFilter(true, string);
    }

    public static FileFilter fileNameDoesNotContain(final String string) {
	return containsFilter(false, string);
    }

    private static FileFilter containsFilter(final boolean contains, final String string) {
	return new FileFilter() {
	    @Override
	    public boolean accept(File pathname) {
		if (contains) {
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

    public static FileFilter fileNameStartsWith(final String string) {
	return startsWithFilter(true, string);
    }

    public static FileFilter fileNameDoesNotStartsWith(final String string) {
	return startsWithFilter(false, string);
    }

    private static FileFilter startsWithFilter(final boolean starts, final String string) {
	return new FileFilter() {
	    @Override
	    public boolean accept(File file) {
		if (starts) {
		    return file.getName().startsWith(string);
		} else {
		    return !file.getName().startsWith(string);
		}
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

    public static FileFilter fileNameMatches(final Pattern p) {
	return new FileFilter() {
	    @Override
	    public boolean accept(File f) {
		return p.matcher(f.getName()).matches();
	    }
	};
    }

}
