package functionaltestapi.v1.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import functionaltestapi.v1.World;
import functionaltestapi.v1.model.ImageOptimizerClient;
import functionaltestapi.v1.model.ResizeUploadForm;
import functionaltestapi.v1.model.ResizedImageVerifier;

import javax.inject.Inject;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class ApiSteps {

    private World world;

    private static final String IMAGE_OPTIMIZER_API_KEY = "d08da773-bae1-4589-bed8-828075c54f5c";
    private static final String RESIZE_IMAGE_ENDPOINT = "/api/v1/image/resize";

    @Inject
    public ApiSteps(World world) {
        this.world = world;
    }

    @Given("^I have an image ([^ ]+)$")
    public void i_have_an_image(String reference) throws Throwable {
        this.world.addClient(reference, new ImageOptimizerClient(this.world.getImageOptimizerEndpoint()));
    }

    @When("^I resize ([^ ]+) with filename ([^ ]+) to width (\\d+) and height (\\d+)")
    public void i_receive_an_image(String reference, String filename, int width, int height) throws Throwable {
        this.world.getClient(reference).setRestClient(this.world.getResizeImageEndpoint());
        ResizeUploadForm resizeUploadForm = new ResizeUploadForm(filename, width, height);
        this.world.getClient(reference).setMultipartFormData(resizeUploadForm);
        this.world.getClient(reference).doPostRequest(world.getClient(reference).getTarget(), this.world.getApiKey());
    }

    @Then("^([^ ]+) is resized with width (\\d+) and height (\\d+)$")
    public void i_check_the_image_is_resized(String reference, int width, int height) throws IOException {
        Dimension expectedImageDimension = new Dimension(width, height);
        ResizedImageVerifier resizedImageVerifier = new ResizedImageVerifier();
        resizedImageVerifier.verify(this.world.getClient(reference).getResponseImage(), expectedImageDimension);
    }



}
