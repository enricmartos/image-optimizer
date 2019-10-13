package service;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


//@ApplicationScoped
public class ImageOptimizerServiceImpl implements ImageOptimizerService {

    private static final int RANDOM_UPPER_LIMIT = 100;
    private static final String ROOT_DIR = "/tmp/";
    private static final String SUFFIX_RESIZED_IMAGE_PATTERN = "_%dx%d";
    private static final String SUFFIX_AUTOROTATED_IMAGE_PATTERN = "_autorotated";
    private static final String RESIZE_COMMAND_PATTERN = "convert %s -resize %dx%d! %s";
    private static final String AUTOROTATE_COMMAND_PATTERN = "convert %s -auto-orient %s";

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
        Logger.getLogger(ImageOptimizerServiceImpl.class.getName()).log(Level.INFO, "Init resize service");

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
                Logger.getLogger(ImageOptimizerServiceImpl.class.getName()).log(Level.INFO, "Image resized");
                return imageResized;
            }
//            } else {
//                MetricsManager.registerImageResizedError();
//            }
        } catch (IOException e) {
            Logger.getLogger(ImageOptimizerServiceImpl.class.getName()).log(Level.SEVERE, "IO error", e);
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
            Logger.getLogger(ImageOptimizerServiceImpl.class.getName()).log(Level.SEVERE, "IO error", e);
        }
        return null;
    }

    private Optional<File> createFile(byte[] image, String path) throws IOException {
        Logger.getLogger(ImageOptimizerServiceImpl.class.getName()).log(Level.INFO, "\"Start creating file with input path:", path);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(image);
            return Optional.of(new File(path));
        } catch (FileNotFoundException e) {
            Logger.getLogger(ImageOptimizerServiceImpl.class.getName()).log(Level.SEVERE, "File not found error", e);
        } catch (IOException e) {
            Logger.getLogger(ImageOptimizerServiceImpl.class.getName()).log(Level.SEVERE, "IO error", e);
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
        return Optional.empty();
    }

    private boolean convertWithIM(String imCommand) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", imCommand);

            Process process = processBuilder.start();

            if (process.waitFor() == 0) {
                return true;
            }

        } catch (IOException e) {
            Logger.getLogger(ImageOptimizerServiceImpl.class.getName()).log(Level.SEVERE, "IOError executing convert command", e);
        } catch (InterruptedException e) {
            Logger.getLogger(ImageOptimizerServiceImpl.class.getName()).log(Level.SEVERE, "Convert command execution interrupted", e);
        }
        return false;
    }

}
