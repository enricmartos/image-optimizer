package functionaltest.v1.model;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

import static org.testng.Assert.assertEquals;

public class ResizedImageVerifier {
//    private byte[] resizedImage;

    private Integer expectedWidth;
    private Integer expectedHeight;
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

    public ResizedImage getResizedImage() {
        return resizedImage;
    }

    public void setResizedImage(ResizedImage resizedImage) {
        this.resizedImage = resizedImage;
    }

//    public void setResizedImage(byte[] resizedImage) {
//        this.resizedImage = resizedImage;
//    }

    //new method verifyDimension that receives the actual image
    public void verifyImageDimension(ResizedImage resizedImage) throws IOException {
        if (expectedWidth != null && expectedHeight != null) {
            Dimension expectedImageDimension = new Dimension(expectedWidth, expectedHeight);
            Optional<Dimension> actualResponseImageDimension = getImageDimension(resizedImage.getResizedImageBytes());
            actualResponseImageDimension.ifPresent(i -> {
                assertEquals(actualResponseImageDimension.get(), expectedImageDimension);
            });
        }

    }

//    public void verify(Dimension expectedImageDimension) throws IOException {
//        Optional<Dimension> actualResponseImageDimension = getImageDimension();
//        actualResponseImageDimension.ifPresent(i -> {
//            assertEquals(actualResponseImageDimension.get(), expectedImageDimension);
//        });
//    }

//    private Optional<Dimension> getImageDimension() throws IOException {
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
