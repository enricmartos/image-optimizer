package functionaltestapi.steps;

import cucumber.runtime.java.guice.ScenarioScoped;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ScenarioScoped
public class World {
    private ResteasyWebTarget target;
//    private MultipartFormDataOutput mdo;
    private Response response;

    private static final String IMAGE_OPTIMIZER_ENDPOINT = "http://172.17.0.1:8080/image-optimizer";
    private static final String IMAGE_OPTIMIZER_API_KEY = "d08da773-bae1-4589-bed8-828075c54f5c";
    private static final String TEST_DOCKER_ENDPOINT = "/api/image/testDocker";

    public void setRestClient() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        target = client.target(IMAGE_OPTIMIZER_ENDPOINT).path(TEST_DOCKER_ENDPOINT);
    }

//    public void setMultipartFormData() {
//        mdo = new MultipartFormDataOutput();
//        mdo.addFormData("selectedFile", fileData, MediaType.APPLICATION_OCTET_STREAM_TYPE);
//        mdo.addFormData("width", width, MediaType.TEXT_PLAIN_TYPE);
//        mdo.addFormData("height", height, MediaType.TEXT_PLAIN_TYPE);
//    }

    public void doRestRequest(ResteasyWebTarget target) {
        response = target.request().get();
    }

    public ResteasyWebTarget getTarget() {
        return target;
    }

    public Response getResponse() {
        return response;
    }
}

