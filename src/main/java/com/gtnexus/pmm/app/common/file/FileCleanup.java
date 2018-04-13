package com.gtnexus.pmm.app.common.file;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import com.gtnexus.pmm.api.v100.service.FileService;
import com.gtnexus.pmm.app.service.FileServiceImpl;

/**
 * 
 * @author jdonovan
 *
 */
public class FileCleanup {

    private final FileService fs;

    public FileCleanup() {
	this.fs = new FileServiceImpl();
    }

    public void cleanup(Collection<File> files) {
	if (files.isEmpty())
	    return;
	Iterator<File> fIter = files.iterator();
	while (fIter.hasNext()) {
	    File f = fIter.next();
	    try {
		del(f);
		fIter.remove();
	    } catch (IOException e) {
	    }
	}
    }

    private void del(File f) throws IOException {
	if (f.isDirectory()) {
	    fs.emptyDir(f, true);
	} else {
	    f.delete();
	}
    }
}
