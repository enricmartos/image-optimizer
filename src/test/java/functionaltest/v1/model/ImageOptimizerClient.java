package functionaltest.v1.model;

import functionaltest.v1.random.ImageRandomizer;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ImageOptimizerClient {

    private String imageOptimizerEndpoint;

    private ResteasyWebTarget target;
    private MultipartFormDataOutput mdo;
    private Response response;

    private static final String BASE_IMG_PATH = "src/test/resources/images/";
    private static final String RESIZE_IMG_INPUT_PATH = "resizeimage/input/";

    public ImageOptimizerClient(String imageOptimizerEndpoint) {
        this.imageOptimizerEndpoint = imageOptimizerEndpoint;
    }

    public void setRestClient(String mediaConverterPath) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        target = client.target(imageOptimizerEndpoint).path(mediaConverterPath);
    }

    public void setMultipartFormData(ResizeImageRequest resizeImageRequest) throws IOException {
        byte[] fileData;
        if (!resizeImageRequest.getOriginalImage().isEmpty()) {
            fileData = Files.readAllBytes(new File(BASE_IMG_PATH + RESIZE_IMG_INPUT_PATH + resizeImageRequest.getOriginalImage()).toPath());
        } else { //generate random image
            fileData = ImageRandomizer.getRandomImage();
        }
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

    public byte[] getResponseImage() {
        byte[] responseImage = response.readEntity(byte[].class);
        response.close();
        return responseImage;
    }

    public ResteasyWebTarget getTarget() {
        return target;
    }

    public int getResponseStatusCode() {
        return response.getStatus();
    }

}
