package com.gtnexus.appxpress.context;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class TempResourceHolderImpl implements TempResourceHolder {
    
    private final LinkedList<File> delOnExit;

    public TempResourceHolderImpl() {
	this.delOnExit = new LinkedList<File>();
    }

    public void deleteOnExit(File f) {
	delOnExit.add(f);
    }

    public List<File> getFilesToDeleteOnExit() {
	return delOnExit;
    }
}
