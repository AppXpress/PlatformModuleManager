import java.util.zip.*;
import java.io.*;
import java.nio.file.*;

public class ZipUtility{
	public String packDirectoryPath;
	/*
	 * Zips up folder depicted by path folder
	 */
	public ZipUtility(String folder){
		try {
			//packDirectoryPath = ( new File(folder).getName() );
			packDirectoryPath = folder;
			System.out.println( "Directory -> " + packDirectoryPath);
			System.out.println(" Zipping up...");
			zipDirectory();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

/**
 * Packs the given directory.
 * @param directoryPath - the directory that is going to be packed
 * @throws IOException
 */
public void zipDirectory(  ) throws IOException {
    // The output zip file name
    String outputFile = packDirectoryPath + ".zip";
     
    // Open streams to write the ZIP contents to
    FileOutputStream fos = new FileOutputStream(outputFile);
    ZipOutputStream zos = new ZipOutputStream(fos); 
    // iterate directory structure recursively and add zip entries
    zipfiles(packDirectoryPath, zos);
     
    // Close the streams
    zos.closeEntry();
    zos.close();
    fos.close();
}
 
/**
 * Recursively pack directory contents.
 * @param directoryPath - current directory path that is visited recursively
 * @param zos - ZIP output stream reference to add elements to
 * @throws IOException
 */
private void zipfiles(String directoryPath, ZipOutputStream zos) throws IOException {
    // Iterate through the directory elements
    for (String dirElement: new File(directoryPath).list()) {
        String dirElementPath = directoryPath+"/"+dirElement;    
        //String dirElementPath = directoryPath+ File.separator +dirElement;  
        //System.out.println(dirElementPath);      
        File f = new File(dirElementPath);
        //System.out.println("Adding..." + f.getName());
        // For directories - go down the directory tree recursively
        if (f.isDirectory()) {
            zipfiles(dirElementPath, zos);
             
        } else {
            ZipEntry ze= new ZipEntry(dirElementPath.replace(packDirectoryPath+"/", ""));
            //ZipEntry ze= new ZipEntry(dirElementPath.replaceAll(packDirectoryPath + File.separator, ""));
            zos.putNextEntry(ze);
             
            // Open input stream to packed file
            FileInputStream fis = new FileInputStream(dirElementPath);
 
            // An array to which will hold byte being read from the packed file
            byte[] bytesRead = new byte[512];
             
            // Read bytes from packed file and store them in the ZIP output stream
            int bytesNum;
            while ((bytesNum = fis.read(bytesRead)) > 0) {
                zos.write(bytesRead, 0, bytesNum);
            }
             
            // Close the stream
            fis.close();
        }
    }
     
}
}