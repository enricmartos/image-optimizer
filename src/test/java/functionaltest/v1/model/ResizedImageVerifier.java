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
    private byte[] resizedImage;

    public void setResizedImage(byte[] resizedImage) {
        this.resizedImage = resizedImage;
    }

    public void verify(Dimension expectedImageDimension) throws IOException {
        Optional<Dimension> actualResponseImageDimension = getImageDimension();
        actualResponseImageDimension.ifPresent(i -> {
            assertEquals(actualResponseImageDimension.get(), expectedImageDimension);
        });
    }

    private Optional<Dimension> getImageDimension() throws IOException {

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
