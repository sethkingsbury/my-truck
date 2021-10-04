package view;

import java.io.*;

/**
 * Provides utilities for dealing with files and directories
 */
public class Utility {

    public static String configDir = System.getenv("HOME") + "/.config/mytruck/";

    public static String XMLExportDir = configDir + "/xmlExport/";

    public static String imageDir;

    public static void createDirectory(String directoryPath) {
        File files = new File(directoryPath);
        if (!files.exists() && files.mkdirs()) {
            System.out.println("[INFO] Created mytruck config directory in: " + directoryPath);
        }
    }

    public static void createImageDirectory() {
        String homeDir = System.getenv("HOME");
        configDir = homeDir + "/.config/mytruck/";
        imageDir = configDir + "images/";
        createDirectory(imageDir);
    }

    public static String getImageDirectory() {
        return System.getenv("HOME") + "/.config/mytruck/" + "images/";
    }

    public static void createUploadDirectoryForXMLFiles() {
        createDirectory(configDir);
    }

    public static void copyDTDFilesToUploadDirectory() {
        String[] DTDFiles = {"Cash.dtd", "ingredient.dtd", "menu.dtd", "recipe.dtd", "supplier.dtd", "user.dtd"};
        for (int i = 0; i < DTDFiles.length; i++) {
            try {
                copyOneFileToDirectory("/data/filler/" + DTDFiles[i], configDir, true);
            } catch (IOException e) { }
        }
    }

    public static String copyOneFileToDirectory(String filepath, String uploadDir, boolean isResource)
            throws IOException {
        InputStream stream = null;
        OutputStream outStream = null;
        String[] filepathArray = filepath.split("/");
        String filename = filepathArray[filepathArray.length - 1];
        String resourceName = filename;
        String outputFilepath = uploadDir + filename;

        try {
            if (isResource)
                stream = Utility.class.getResourceAsStream(filepath);
            else
                stream = new FileInputStream(filepath);

            if (stream == null) {
                throw new Exception("Failed to get resource from " + resourceName);
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            outStream = new FileOutputStream(outputFilepath);
            while ((readBytes = stream.read(buffer)) > 0) {
                outStream.write(buffer, 0, readBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stream.close();
            outStream.close();
        }

        return outputFilepath;

    }

}
