package service;

import service.utils.ConvertUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static service.utils.ConvertUtils.getTmpFilename;


public class ImageOptimizerServiceImpl implements ImageOptimizerService {

    //Constants
    //Common
    private static final String ROOT_DIR = "/tmp/";
    private static final Logger LOGGER = Logger.getLogger(ImageOptimizerServiceImpl.class.getName());
    //resizeImage
    private static final String SUFFIX_RESIZED_IMAGE_PATTERN = "_%dx%d";
    private static final String RESIZE_COMMAND_PATTERN = "convert %s -resize %dx%d! %s";
    //autorotate
    private static final String SUFFIX_AUTOROTATED_IMAGE_PATTERN = "_autorotated";
    private static final String AUTOROTATE_COMMAND_PATTERN = "convert %s -auto-orient %s";
    //convertDocToPDF
    private static final String SUFFIX_DOC2PDF_PATTERN = ".pdf";
    private static final String DOC2PDF_COMMAND_PATTERN = "unoconv -f pdf %s";
    //convertPDFToImages
    private static final String SUFFIX_DOC2IMG_FOLDER = "_dir/";
    private static final String SUFFIX_DOC2IMG_PATTERN = ".jpg";
    private static final String PDF2IMG_COMMAND_PATTERN = "convert %s -density 128 %s";
    private static final String SUFFIX_DIR_COMPRESSED = ".zip";

    @Override
    public byte[] resizeImage(byte[] image, Integer width, Integer height) {
        LOGGER.log(Level.INFO, "Init resize service");
        try {
            StringBuilder tmpFilename = ConvertUtils.getTmpFilename();
            String inputPath = ROOT_DIR + tmpFilename;
            String suffixResizedImage = String.format(SUFFIX_RESIZED_IMAGE_PATTERN, width, height);
            String outputPath = ROOT_DIR + tmpFilename + suffixResizedImage;
            ConvertUtils.createFile(image, inputPath);
            String resizeCommand = String.format(RESIZE_COMMAND_PATTERN, inputPath, width, height, outputPath);
            if(ConvertUtils.convertWithCommand(resizeCommand)) {
                byte[] imageResized = Files.readAllBytes(new File(outputPath).toPath());
                Files.delete(new File(outputPath).toPath());
                Files.delete(new File(inputPath).toPath());
                LOGGER.log(Level.INFO, "Image resized");
                return imageResized;
            }
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
            ConvertUtils.createFile(image, inputPath);
            String autorotateCommand = String.format(AUTOROTATE_COMMAND_PATTERN, inputPath, outputPath);
            if(ConvertUtils.convertWithCommand(autorotateCommand)) {
                byte[] imageAutorotated = Files.readAllBytes(new File(outputPath).toPath());
                Files.delete(new File(outputPath).toPath());
                Files.delete(new File(inputPath).toPath());

                return imageAutorotated;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO error", e);
        }
        return null;
    }

    @Override
    public byte[] convertPDFToImages(byte[] pdf) {
        try {
            StringBuilder tmpFilename = getTmpFilename();
            //inputPath = /tmp/1234
            String inputPath = ROOT_DIR + tmpFilename;
            //inputDirPath = /tmp/1234_dir/
            String inputDirPath = inputPath + SUFFIX_DOC2IMG_FOLDER;
            ConvertUtils.createFolder(inputDirPath);
            //outputDirPath = /tmp/1234_dir/1234.jpg
            String outputDirPath = inputDirPath + tmpFilename + SUFFIX_DOC2IMG_PATTERN;
            ConvertUtils.createFile(pdf, inputPath);
            //doc2ImgCommand = convert /tmp/1234 -density 128 %s /tmp/1234_dir/1234.jpg
            String doc2ImgCommand = String.format(PDF2IMG_COMMAND_PATTERN, inputPath, outputDirPath);
            if (ConvertUtils.convertWithCommand(doc2ImgCommand)) {
                LOGGER.info("doc2Img conversion successful");
//                String zipPath = inputDirPath + SUFFIX_DIR_COMPRESSED;
                String zipPath = inputPath + SUFFIX_DIR_COMPRESSED;
                ConvertUtils.compressFolder(inputDirPath, zipPath);
                byte[] zipFromDoc = Files.readAllBytes(new File(zipPath).toPath());
                Files.delete(new File(zipPath).toPath());
                Files.delete(new File(inputPath).toPath());
                Arrays.stream(new File(inputDirPath).listFiles()).forEach(File::delete);
                Files.delete(new File(inputDirPath).toPath());
                return zipFromDoc;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO error (pdf 2 img)", e);
        }
        return null;
    }

    @Override
    public byte[] convertDocToImages(byte[] doc) {
        try {
            doc = convertDocToPDF(doc);
            return convertPDFToImages(doc);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO error (doc 2 img)", e);
        }
        return null;
    }

    private byte[] convertDocToPDF(byte[] doc) throws IOException {
        LOGGER.log(Level.INFO, "DOC, PPT OR XLS file detected");
        StringBuilder tmpFilename = getTmpFilename();
        String inputPath = ROOT_DIR + tmpFilename;
        String outputPath = ROOT_DIR + tmpFilename + SUFFIX_DOC2PDF_PATTERN;
        ConvertUtils.createFile(doc, inputPath);
        String doc2PdfCommand = String.format(DOC2PDF_COMMAND_PATTERN, inputPath);
        if (ConvertUtils.convertWithCommand(doc2PdfCommand)) {
            byte[] pdfFromDoc = Files.readAllBytes(new File(outputPath).toPath());
            Files.delete(new File(outputPath).toPath());
            Files.delete(new File(inputPath).toPath());
            return pdfFromDoc;
        }
        return null;
    }

}
