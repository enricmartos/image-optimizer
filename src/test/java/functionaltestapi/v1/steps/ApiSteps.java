package functionaltestapi.v1.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import functionaltestapi.v1.World;
import functionaltestapi.v1.model.ImageOptimizerClient;
import functionaltestapi.v1.model.ResizeImageRequest;
import functionaltestapi.v1.model.ResizedImageVerifier;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;
import java.io.IOException;

public class ApiSteps {

    private World world;

    @Inject
    public ApiSteps(World world) {
        this.world = world;
    }

    @Given("^([^ ]+) is a client of the media-converter module$")
    public void createClient(String clientReference) throws Throwable {
        this.world.addClient(clientReference, new ImageOptimizerClient(this.world.getImageOptimizerEndpoint()));
    }

    @When("^([^ ]+) requests to resize an image")
    public void getImageResized(String clientReference, List<ResizeImageRequest> resizeImageRequests) throws Throwable {
        this.world.getClient(clientReference).setRestClient(this.world.getResizeImageEndpoint());
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






//    @Given("^I have an image ([^ ]+)$")
//    public void i_have_an_image(String reference) throws Throwable {
//        this.world.addClient(reference, new ImageOptimizerClient(this.world.getImageOptimizerEndpoint()));
//    }

//    @When("^I resize ([^ ]+) with filename ([^ ]+) to width (\\d+) and height (\\d+)")
//    public void i_receive_an_image(String reference, String filename, int width, int height) throws Throwable {
//        this.world.getClient(reference).setRestClient(this.world.getResizeImageEndpoint());
//        ResizeImageRequest resizeImageRequest = new ResizeImageRequest(filename, width, height);
//        this.world.getClient(reference).setMultipartFormData(resizeImageRequest);
//        this.world.getClient(reference).doPostRequest(world.getClient(reference).getTarget(), this.world.getApiKey());
//    }

//    @Then("^([^ ]+) is resized with width (\\d+) and height (\\d+)$")
//    public void i_check_the_image_is_resized(String reference, int width, int height) throws IOException {
//        Dimension expectedImageDimension = new Dimension(width, height);
//        ResizedImageVerifier resizedImageVerifier = new ResizedImageVerifier();
//        resizedImageVerifier.verify(this.world.getClient(reference).getResponseImage(), expectedImageDimension);
//    }



}
