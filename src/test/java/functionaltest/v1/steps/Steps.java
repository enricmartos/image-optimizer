package functionaltest.v1.steps;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import functionaltest.v1.World;
import functionaltest.v1.model.ImageOptimizerClient;
import functionaltest.v1.model.ResponseImage;
import functionaltest.v1.model.request.AutorotateImageRequest;
import functionaltest.v1.model.request.ImageOptimizerRequest;
import functionaltest.v1.model.request.ResizeImageRequest;
import functionaltest.v1.model.verifier.AutorotatedImageVerifier;
import functionaltest.v1.model.verifier.ResizedImageVerifier;
import org.testng.Assert;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

public class Steps {

    private World world;

    private static final String IMAGE_OPTIMIZER_API_KEY = "d08da773-bae1-4589-bed8-828075c54f5c";
    private static final String RESIZE_IMAGE_ENDPOINT = "/api/image/resize";
    private static final String AUTOROTATE_IMAGE_ENDPOINT = "/api/image/autorotate";
    private static final Integer API_KEY_LENGTH = 36;

    @Inject
    public Steps(World world) {
        this.world = world;
    }

    @Given("^([^ ]+) is a client of the image-optimizer module$")
    public void createClient(String clientReference) throws Throwable {
        ImageOptimizerClient imageOptimizerClient = new ImageOptimizerClient(this.world.getImageOptimizerEndpoint());
        this.world.addClient(clientReference, imageOptimizerClient);
        this.world.setApiKey(clientReference, IMAGE_OPTIMIZER_API_KEY);
    }

    @Given("^([^ ]+) has an invalid api key$")
    public void createInvalidApiKey(String clientReference) {
        Random rand = new Random();
        int randomInt = rand.nextInt(API_KEY_LENGTH);
        String apiKeyRandom = Integer.toString(randomInt);
        //investigate RegularExpressionRandomizer implementation in commons-test project
//        String apiKeyRandom = new RegularExpressionRandomizer(randomizer, "[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}").getRandomValue();
        world.setApiKey(clientReference, apiKeyRandom);
    }

    @When("^([^ ]+) requests to resize an image")
    public void getImageResized(String clientReference, List<ResizeImageRequest> resizeImageRequests) throws Throwable {
        ImageOptimizerClient imageOptimizerClient = this.world.getClient(clientReference);
        imageOptimizerClient.setRestClient(RESIZE_IMAGE_ENDPOINT);
        imageOptimizerClient.setMultipartFormData(resizeImageRequests.get(0));
        imageOptimizerClient.doPostRequest(imageOptimizerClient.getTarget(),
                this.world.getApiKey(clientReference));
        if (imageOptimizerClient.getResponseStatusCode() == BAD_REQUEST.getStatusCode()) {
            Exception badRequestException = new BadRequestException();
            this.world.addException(badRequestException);
        } else {
            world.setResponseImage(new ResponseImage(imageOptimizerClient.getResponseImage()));
        }
    }

    @When("^([^ ]+) requests to auto-rotate an image")
    public void getImageAutorotated(String clientReference, List<AutorotateImageRequest> autorotateImageRequests) throws Throwable {
        ImageOptimizerClient imageOptimizerClient = this.world.getClient(clientReference);
        imageOptimizerClient.setRestClient(AUTOROTATE_IMAGE_ENDPOINT);
        imageOptimizerClient.setMultipartFormData(autorotateImageRequests.get(0));
        imageOptimizerClient.doPostRequest(imageOptimizerClient.getTarget(),
                this.world.getApiKey(clientReference));
        if (imageOptimizerClient.getResponseStatusCode() == BAD_REQUEST.getStatusCode()) {
            Exception badRequestException = new BadRequestException();
            this.world.addException(badRequestException);
        } else {
            world.setResponseImage(new ResponseImage(imageOptimizerClient.getResponseImage()));
        }
    }

    @Then("^the image-optimizer module returns the image resized$")
    public void verifyImageResized(List<ResizedImageVerifier> verifiers) throws IOException {
        if (verifiers.get(0).getExpectedResponseImage() == null) {
            verifiers.get(0).verifyImageDimension(world.getResponseImage());
        } else {
            verifiers.get(0).verifyImage(world.getResponseImage());
        }
    }

    @Then("^the image-optimizer module returns the image auto-rotated$")
    public void verifyImageAutorotated(List<AutorotatedImageVerifier> verifiers) throws IOException, ImageProcessingException, MetadataException {
        if (verifiers.get(0).getExpectedResponseImage() == null) {
            verifiers.get(0).verifyImageOrientation(world.getResponseImage());
        } else {
            verifiers.get(0).verifyImage(world.getResponseImage());
        }
    }

    @Then("^the request fails with a bad request$")
    public void badRequest() {
        Assert.assertTrue(world.hasException(BadRequestException.class));
    }


}
