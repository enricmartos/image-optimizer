package functionaltestapi.v1.model;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Optional;

public class ImageOptimizerClient {

    private String imageOptimizerEndpoint;

    private ResteasyWebTarget target;
    private MultipartFormDataOutput mdo;
    private Response response;

    private static final String BASE_IMG_PATH = "src/test/resources/";

    public ImageOptimizerClient(String imageOptimizerEndpoint) {
        this.imageOptimizerEndpoint = imageOptimizerEndpoint;
    }

    public void setRestClient(String mediaConverterPath) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        target = client.target(imageOptimizerEndpoint).path(mediaConverterPath);
    }

    public void setMultipartFormData(ResizeImageRequest resizeImageRequest) throws IOException {
        byte[] fileData = Files.readAllBytes(new File(BASE_IMG_PATH + resizeImageRequest.getOriginalImage()).toPath());
        mdo = new MultipartFormDataOutput();
        mdo.addFormData("selectedFile", fileData, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        mdo.addFormData("width", resizeImageRequest.getWidth(), MediaType.TEXT_PLAIN_TYPE);
        mdo.addFormData("height", resizeImageRequest.getHeight(), MediaType.TEXT_PLAIN_TYPE);
    }

    public void doPostRequest(ResteasyWebTarget target, String mediaConverterApiKey) {

        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(mdo) { };

        response = target.request()
                .header("apiKey", mediaConverterApiKey)
                .post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
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

    public byte[] getResponseImage() {
        byte[] responseImage = response.readEntity(byte[].class);
        response.close();
        return responseImage;
    }

    public ResteasyWebTarget getTarget() {
        return target;
    }

    public Response getResponse() {
        return response;
    }
}
