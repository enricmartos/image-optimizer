package functionaltest.v1.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import functionaltest.v1.World;
import functionaltest.v1.model.ImageOptimizerClient;
import functionaltest.v1.model.ResizeImageRequest;
import functionaltest.v1.model.ResizedImageVerifier;
import org.testng.Assert;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;
import java.io.IOException;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

public class ResizeImageSteps {

    private World world;

    private static final String RESIZE_IMAGE_ENDPOINT = "/api/image/resize";

    @Inject
    public ResizeImageSteps(World world) {
        this.world = world;
    }

    @Given("^([^ ]+) is a client of the media-converter module$")
    public void createClient(String clientReference) throws Throwable {
        ImageOptimizerClient imageOptimizerClient = new ImageOptimizerClient(this.world.getImageOptimizerEndpoint());
        this.world.addClient(clientReference, imageOptimizerClient);
    }

    @When("^([^ ]+) requests to resize an image")
    public void getImageResized(String clientReference, List<ResizeImageRequest> resizeImageRequests) throws Throwable {
        this.world.getClient(clientReference).setRestClient(RESIZE_IMAGE_ENDPOINT);
        this.world.setResizeImageRequest(resizeImageRequests.get(0));
        this.world.getClient(clientReference).setMultipartFormData(resizeImageRequests.get(0));
        this.world.getClient(clientReference).doPostRequest(world.getClient(clientReference).getTarget(), this.world.getApiKey());
    }

    @Then("^the media-converter module returns after ([^ ]+) request$")
    public void verifyResizedImage(String clientReference, List<ResizedImageVerifier> verifiers) throws IOException {
        verifiers.get(0).setResizedImage(this.world.getClient(clientReference).getResponseImage());
        Dimension expectedImageDimension = new Dimension(this.world.getResizeImageRequest().getWidth(),
                this.world.getResizeImageRequest().getHeight());
        verifiers.get(0).verify(expectedImageDimension);
    }

    @Then("^the media-converter module returns a bad request after ([^ ]+) request$")
    public void badRequest(String clientReference) {
        Assert.assertEquals(this.world.getClient(clientReference).getResponseStatusCode(), BAD_REQUEST.getStatusCode());
    }

}
