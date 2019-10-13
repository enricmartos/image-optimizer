package service;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


//@ApplicationScoped
public class ImageOptimizerServiceImpl implements ImageOptimizerService {

    private static final int RANDOM_UPPER_LIMIT = 100;
    private static final String ROOT_DIR = "/tmp/";
    private static final String OUTPUT_DIR = ROOT_DIR + "imgs/";
    private static final String SUFFIX_RESIZED_IMAGE_PATTERN = "_%dx%d";
    private static final String SUFFIX_AUTOROTATED_IMAGE_PATTERN = "_autorotated";
    private static final String SUFFIX_DOC2IMG_PATTERN = ".jpg";
    private static final String RESIZE_COMMAND_PATTERN = "convert %s -resize %dx%d! %s";
    private static final String AUTOROTATE_COMMAND_PATTERN = "convert %s -auto-orient %s";
    private static final String DOC2IMG_COMMAND_PATTERN = "convert %s -density 128 %s";
    private static final String IMGS_DIR_COMPRESSED = "dirCompressed.zip";
    private static final Logger LOGGER = Logger.getLogger(ImageOptimizerServiceImpl.class.getName());

    private StringBuilder getTmpFilename() {
        Random rand = new Random();
        int n = rand.nextInt(RANDOM_UPPER_LIMIT);
        StringBuilder tmpFilename = new StringBuilder();
        tmpFilename.append(ZonedDateTime.now().toInstant().toEpochMilli()).append(n);
        return tmpFilename;
    }


    @Override
    public byte[] resizeImage(byte[] image, Integer width, Integer height) {

//        Random rand = new Random();
//        int n = rand.nextInt(RANDOM_UPPER_LIMIT);
//        StringBuilder tmpFilename = new StringBuilder();
//        tmpFilename.append(ZonedDateTime.now().toInstant().toEpochMilli()).append(n);
        LOGGER.log(Level.INFO, "Init resize service");

        try {
            StringBuilder tmpFilename = getTmpFilename();
            String inputPath = ROOT_DIR + tmpFilename;
            String suffixResizedImage = String.format(SUFFIX_RESIZED_IMAGE_PATTERN, width, height);
            String outputPath = ROOT_DIR + tmpFilename + suffixResizedImage;
            createFile(image, inputPath);
            String resizeCommand = String.format(RESIZE_COMMAND_PATTERN, inputPath, width, height, outputPath);
            if(convertWithIM(resizeCommand)) {
                byte[] imageResized = Files.readAllBytes(new File(outputPath).toPath());
                Files.delete(new File(outputPath).toPath());
                Files.delete(new File(inputPath).toPath());

//                MetricsManager.registerImageResized();
                LOGGER.log(Level.INFO, "Image resized");
                return imageResized;
            }
//            } else {
//                MetricsManager.registerImageResizedError();
//            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO error", e);
        }
        return null;
    }

    @Override
    public byte[] autorotateImage(byte[] image) {

        try {
            StringBuilder tmpFilename = getTmpFilename();
            String inputPath = ROOT_DIR + tmpFilename;
            String outputPath = ROOT_DIR + tmpFilename + SUFFIX_AUTOROTATED_IMAGE_PATTERN;
            createFile(image, inputPath);
            String autorotateCommand = String.format(AUTOROTATE_COMMAND_PATTERN, inputPath, outputPath);
            if(convertWithIM(autorotateCommand)) {
                byte[] imageAutorotated = Files.readAllBytes(new File(outputPath).toPath());
                Files.delete(new File(outputPath).toPath());
                Files.delete(new File(inputPath).toPath());

//                MetricsManager.registerimageAutorotated();
                return imageAutorotated;
            }
//            } else {
//                MetricsManager.registerImageAutorotatedError();
//            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO error", e);
        }
        return null;
    }

    @Override
    public byte[] convertDocToImages(byte[] doc) {

        try {
            StringBuilder tmpFilename = getTmpFilename();
            String inputPath = ROOT_DIR + tmpFilename;
            String outputPath = OUTPUT_DIR + tmpFilename + SUFFIX_DOC2IMG_PATTERN;
            createFile(doc, inputPath);
            String doc2ImgCommand = String.format(DOC2IMG_COMMAND_PATTERN, inputPath, outputPath);
            if(convertWithIM(doc2ImgCommand)) {
                compressDirectory();
                String zipPath = ROOT_DIR + IMGS_DIR_COMPRESSED;
                byte[] zipFromDoc = Files.readAllBytes(new File(zipPath).toPath());
                LOGGER.log(Level.INFO, "zipFromDoc first byte: " + zipFromDoc[0] + " , zip Path: " + zipPath);
                //TODO clean files
//                Files.delete(new File(outputPath).toPath());
//                Files.delete(new File(inputPath).toPath());

//                Arrays.stream(new File(OUTPUT_DIR).listFiles()).forEach(File::delete);

                return zipFromDoc;
            }
            LOGGER.log(Level.INFO, "Img Magick conversion failed");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO error (doc 2 img)", e);
        }
        return null;
    }

    private Optional<File> createFile(byte[] image, String path) throws IOException {
        String testLogVar = "testLogVar";
        LOGGER.log(Level.INFO, "\"Start creating file with input path testLogVar:" + testLogVar);
        LOGGER.log(Level.INFO, "\"Start creating file with input path:" + path);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(path);
            LOGGER.log(Level.INFO, "Init write input file to fileOutputStream");
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
        LOGGER.log(Level.INFO, "\"Empty output file");
        return Optional.empty();
    }

    private boolean convertWithIM(String imCommand) {
        try {
            LOGGER.log(Level.INFO, "Init imageMagick conversion");
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", imCommand);

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

    //Iterative
    private static void compressDirectory() throws IOException {


        byte[] buffer = new byte[1024];

        try (FileOutputStream fos = new FileOutputStream("dirCompressed.zip"); ZipOutputStream zos = new ZipOutputStream(fos)) {
            LOGGER.log(Level.INFO, "dir to zip: " + OUTPUT_DIR);
            File fileToZip = new File(OUTPUT_DIR);

            File[] children = fileToZip.listFiles();
            LOGGER.log(Level.INFO, "length of files array: " + children.length);
            int ctr = 0;
            for (File childFile : children) {
                LOGGER.log(Level.INFO, "iteration num: " + ctr);
                ZipEntry ze = new ZipEntry(OUTPUT_DIR + childFile.getName());
                zos.putNextEntry(ze);
                LOGGER.log(Level.INFO, "childfile name: " + OUTPUT_DIR + childFile.getName());
                FileInputStream in = new FileInputStream(OUTPUT_DIR + childFile.getName());

                int len;
                while ((len = in.read(buffer)) >= 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
                //not necessary with try-with-resources
//                zos.close();
//                fos.close();
                ctr++;
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

}
