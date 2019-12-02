package functionaltestapi.v1.model;

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

    public void verify(byte[] responseImage, Dimension expectedImageDimension) throws IOException {
        Optional<Dimension> actualResponseImageDimension = getImageDimension(responseImage);
        actualResponseImageDimension.ifPresent(i -> {
            assertEquals(actualResponseImageDimension.get(), expectedImageDimension);
        });
    }


    public Optional<Dimension> getImageDimension(byte[] image) throws IOException {

        ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(image));

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
