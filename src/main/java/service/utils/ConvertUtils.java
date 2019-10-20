package service.utils;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ConvertUtils {

    //Constants
    //Common
    private static final Logger LOGGER = Logger.getLogger(ConvertUtils.class.getName());
    //getTmpFilename
    private static final int RANDOM_UPPER_LIMIT = 100;

    public static StringBuilder getTmpFilename() {
        Random rand = new Random();
        int n = rand.nextInt(RANDOM_UPPER_LIMIT);
        StringBuilder tmpFilename = new StringBuilder();
        tmpFilename.append(ZonedDateTime.now().toInstant().toEpochMilli()).append(n);
        return tmpFilename;
    }

    public static Optional<File> createFile(byte[] image, String path) throws IOException {
        LOGGER.log(Level.INFO, "\"Start creating file with input path:" + path);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(image);
            return Optional.of(new File(path));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File not found error", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO error", e);
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
        return Optional.empty();
    }

    public static void createFolder(String inputPath ) {
        File file = new File(inputPath);
        if (!file.exists()) {
            if (file.mkdir()) {
                LOGGER.log(Level.INFO, "Folder created: " + inputPath);
            } else {
                LOGGER.log(Level.SEVERE, "Failed to create folder " + inputPath);
            }
        }
    }

    public static void compressFolder(String inputDirPath, String zipPath) {
        byte[] buffer = new byte[1024];

        try (FileOutputStream fos = new FileOutputStream(zipPath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            LOGGER.log(Level.INFO, "inputDirPath: " + inputDirPath);
            LOGGER.log(Level.INFO, "zipPath: " + zipPath);

            File fileToZip = new File(inputDirPath);

            File[] children = fileToZip.listFiles();
            LOGGER.info("Number of child files: " + children.length);
            for (File childFile : children) {
                ZipEntry ze = new ZipEntry(inputDirPath + childFile.getName());
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(inputDirPath + childFile.getName());

                int len;
                while ((len = in.read(buffer)) >= 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
            }
        } catch (IOException e){
            LOGGER.log(Level.SEVERE, "IOError compressing folder", e);
        }
    }

    public static boolean convertWithCommand(String command) {
        try {
            LOGGER.log(Level.INFO, "Init conversion");
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", command);
            Process process = processBuilder.start();
            if (process.waitFor() == 0) {
                return true;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IOError executing convert command", e);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Convert command execution interrupted", e);
        }
        return false;
    }
}
