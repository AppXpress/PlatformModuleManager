package com.gtnexus.appxpress.pmbuilder;

import com.gtnexus.appxpress.Mapper;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;

import static com.gtnexus.appxpress.AppXpressConstants.*;

public class AppXpressMapper implements Mapper {

    private final File root;
    private final FileFilter dirsOnly;

    public AppXpressMapper(File root) {
        this.root = root;
        this.dirsOnly = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }
        };
    }

    public void doMapping() {
        if (root == null || !root.isDirectory()) {
            System.err.println("Module root cannot be found, or is empty. "
                    + "No mapping can be performed.");
            return;
        }
        prepareFoldersForImport();
        createBundles(root);

    }

    private void prepareFoldersForImport() {
        for (File dir : root.listFiles(dirsOnly)) {
            //TODO this is whack yo. directly ported from original
            String directoryName = dir.getName();
            if (directoryName.endsWith(CUSTOM_LINK_D1)) {
                renameFile(dir, CUSTOM_LINK_D1);
            } else if (directoryName.endsWith(TYPE_EXTENSION_D1)) {
                renameFile(dir, $ + TYPE_EXTENSION_D1);
            } else if (directoryName.endsWith(CUSTOM_OBJECT_MODULE)) {
                fixCOModule(dir);
            } else if (directoryName.endsWith(CUSTOM_UI)) {
                for (File bundle : dir.listFiles()) { //TODO this should verify do dir's only
                    for (File file : bundle.listFiles()) {
                        renameFile(file, $ + file.getName());
                    }
                }
            }
        }
    }


    private void renameFile(File file, String newName) {
        try {
            Files.move(file.toPath(),
                    file.toPath().resolve(newName));
        } catch (IOException e) {
            System.err.println(
                    "Exception when trying to rename "
                            + file.getName());
        }
    }

    private void fixCOModule(File directory) {
        //TODO this method in the original is quite the mess
    }

    private void createBundles(File directory) {

    }

    // handleCODesignScripts
    // handleFeF
    //

    /**
     * @param dir The CustomObject/designs/scripts directory.
     */
    private void handleCustomObjectDesignScripts(File dir) {
        for (File subDir : dir.listFiles(dirsOnly)) {
            handleSingleCODScript(subDir);
        }
    }

    private void handleSingleCODScript(File dir) {
        if (dir.list().length == 1) {

        } else if (dir.list().length > 1) {

        }
    }

    private void handleFef(File dir) {
        for (File file : dir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File current, String fileName) {
                return !fileName.endsWith(ZIP_EXTENSION);
            }

        })) {
            // zip dir and then delete
        }
    }

}
