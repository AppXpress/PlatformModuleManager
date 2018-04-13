package com.gtnexus.pmm.api.v100.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import com.gtnexus.pmm.api.v100.command.SubCommandException;

public interface ZipService {

    void zipFiles(Collection<File> files, String absPathToDestinationZip) throws SubCommandException;

    void zipDirectory(Path directory) throws SubCommandException;

    /**
     * Packs the given directory into a a zip file named after the directory.
     * 
     * @param directoryPath
     *            - the directory that is going to be packed
     * @throws IOException
     */
    void zipDirectory(File directory) throws SubCommandException;

    /**
     * Packs the given directory into a a zip file named after the directory.
     * 
     * @param directoryPath
     *            - the directory that is going to be packed
     * @throws IOException
     */
    void zipDirectory(File directory, String outputZip) throws SubCommandException;

    /**
     * Packs the given directory into the zip file pointer. If the outputZip does
     * not end with .zip it is appended.
     * 
     * @param directory
     * @param outputZip
     * @throws SubCommandException
     */
    void zipDirectory(File directory, File outputZip) throws SubCommandException;

    void unzip(File source, File destination, boolean recurse) throws SubCommandException;

    void unzip(File source, File destination) throws SubCommandException;

}