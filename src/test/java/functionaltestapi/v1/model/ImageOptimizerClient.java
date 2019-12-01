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

    private ResteasyWebTarget target;
    private MultipartFormDataOutput mdo;
    private Response response;

    private static final String IMAGE_OPTIMIZER_ENDPOINT = "http://172.17.0.1:8080/image-optimizer";
    private static final String IMAGE_OPTIMIZER_API_KEY = "d08da773-bae1-4589-bed8-828075c54f5c";
    private static final String TEST_DOCKER_ENDPOINT = "/api/image/testDocker";
    private static final String TEST_RESIZE_IMAGE = "/api/image/resize";

    private static final String TEST_IMG_PATH = "src/test/resources/testimg.jpg";

    public void setRestClientTestDocker() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        target = client.target(IMAGE_OPTIMIZER_ENDPOINT).path(TEST_DOCKER_ENDPOINT);
    }

    public void setRestClientResizeImage() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        target = client.target(IMAGE_OPTIMIZER_ENDPOINT).path(TEST_RESIZE_IMAGE);
    }

    public void setMultipartFormData(String filename, int width, int height) throws IOException {
        byte[] fileData = Files.readAllBytes(new File(TEST_IMG_PATH).toPath());
        mdo = new MultipartFormDataOutput();
        mdo.addFormData("selectedFile", fileData, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        mdo.addFormData("width", width, MediaType.TEXT_PLAIN_TYPE);
        mdo.addFormData("height", height, MediaType.TEXT_PLAIN_TYPE);
    }

    public void doGetRequestTestDocker(ResteasyWebTarget target) {
        response = target.request().get();
    }

    public void doPostRequestResizeImage(ResteasyWebTarget target) {

        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(mdo) { };

        response = target.request()
                .header("apiKey", IMAGE_OPTIMIZER_API_KEY)
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

    public ResteasyWebTarget getTarget() {
        return target;
    }

    public Response getResponse() {
        return response;
    }
}
