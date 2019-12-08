package functionaltest.v1.model;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Optional;

import static org.testng.Assert.assertEquals;

public class ResizedImageVerifier {

    private static final String BASE_IMG_PATH = "src/test/resources/images/";
    private static final String RESIZE_IMG_GROUND_TRUTH_PATH = "resizeimage/groundtruth/";

    private Integer expectedWidth;
    private Integer expectedHeight;
    private String expectedResizedImage;
    private ResizedImage resizedImage;

    public Integer getExpectedWidth() {
        return expectedWidth;
    }

    public void setExpectedWidth(Integer expectedWidth) {
        this.expectedWidth = expectedWidth;
    }

    public Integer getExpectedHeight() {
        return expectedHeight;
    }

    public void setExpectedHeight(int expectedHeight) {
        this.expectedHeight = expectedHeight;
    }

    public String getExpectedResizedImage() {
        return expectedResizedImage;
    }

    public void setExpectedResizedImage(String expectedResizedImage) {
        this.expectedResizedImage = expectedResizedImage;
    }

    public ResizedImage getResizedImage() {
        return resizedImage;
    }

    public void setResizedImage(ResizedImage resizedImage) {
        this.resizedImage = resizedImage;
    }


    public void verifyImageDimension(ResizedImage resizedImage) throws IOException {
        if (expectedWidth != null && expectedHeight != null) {
            Dimension expectedImageDimension = new Dimension(expectedWidth, expectedHeight);
            Optional<Dimension> actualResponseImageDimension = getImageDimension(resizedImage.getResizedImageBytes());
            actualResponseImageDimension.ifPresent(i -> {
                assertEquals(actualResponseImageDimension.get(), expectedImageDimension);
            });
        }

    }

    public void verifyImage(ResizedImage resizedImage) throws IOException {
        if (expectedResizedImage != null) {
            byte[] expectedImage = Files.readAllBytes(new File(BASE_IMG_PATH + RESIZE_IMG_GROUND_TRUTH_PATH
                    + expectedResizedImage).toPath());
            assertEquals(resizedImage.getResizedImageBytes(), expectedImage);
        }
    }

        private Optional<Dimension> getImageDimension(byte[] resizedImage) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(resizedImage));

        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        if (readers.hasNext()) {
            ImageReader reader = readers.next();
            try {
                reader.setInput(iis);
                return Optional.of(new Dimension(reader.getWidth(0), reader.getHeight(0)));
            } finally {
                reader.dispose();
            }
        }
        return Optional.empty();
    }

}
