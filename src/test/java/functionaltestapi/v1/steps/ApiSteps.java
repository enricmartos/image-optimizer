package functionaltestapi.v1.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import functionaltestapi.v1.World;
import functionaltestapi.v1.model.ImageOptimizerClient;

import javax.inject.Inject;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class ApiSteps {

    private World world;

    @Inject
    public ApiSteps(World world) {
        this.world = world;
    }

    @Given("^I have an image ([^ ]+)$")
    public void i_have_an_image(String reference) throws Throwable {
        this.world.addClient(reference, new ImageOptimizerClient());
    }

    @When("^I resize ([^ ]+) with filename ([^ ]+) to width (\\d+) and height (\\d+)")
    public void i_receive_an_image(String reference, String filename, int width, int height) throws Throwable {
        this.world.getClient(reference).setRestClientResizeImage();
        this.world.getClient(reference).setMultipartFormData(filename, width, height);
        this.world.getClient(reference).doPostRequestResizeImage(world.getClient(reference).getTarget());
    }

    @Then("^([^ ]+) is resized with width (\\d+) and height (\\d+)$")
    public void i_check_the_image_is_resized(String reference, int width, int height) throws IOException {
        Dimension expectedDimension = new Dimension(width, height);
        byte[] value = this.world.getClient(reference).getResponse().readEntity(byte[].class);
        this.world.getClient(reference).getResponse().close();
        Optional<Dimension> actualDimension = this.world.getClient(reference).getImageDimension(value);
        assertEquals(expectedDimension, actualDimension.get());
    }

//
//
//
//    @Given("^I set GET request$")
//    public void setGetRequestTestDocker() {
//        this.world.setRestClientTestDocker();
//    }
//
//    @Given("^I set resizeImage POST request$")
//    public void setPostRequest() {
//        this.world.setRestClientResizeImage();
//    }
//
//    @When("^I do GET request$")
//    public void doGetRequestTestDocker() {
//        this.world.doGetRequestTestDocker(world.getTarget());
//    }
//
//    @When("^I set MultipartFormData with image ([^ ]+), width (\\d+) and height (\\d+)$")
//    public void doPostRequestResizeImage(String filename, int width, int height) throws IOException {
//        this.world.setMultipartFormData(filename, width, height);
//    }
//
//    @When("^I do resizeImage POST request$")
//    public void doPostRequestResizeImage() {
//        this.world.doPostRequestResizeImage(world.getTarget());
//    }
//
//    @Then("^I receive valid HTTP response code (\\d+)$")
//    public void receiveValidResponse(int arg1) {
//        assertEquals(arg1, this.world.getResponse().getStatus());
//    }
//
//    @Then("^I receive a valid resized image with width (\\d+) and height (\\d+)$")
//    public void receiveValidResizedImage(int width, int height) throws IOException {
//        Dimension expectedDimension = new Dimension(width, height);
//        byte[] value = this.world.getResponse().readEntity(byte[].class);
//        this.world.getResponse().close();
//        Optional<Dimension> actualDimension = this.world.getImageDimension(value);
//        assertEquals(expectedDimension, actualDimension.get());
//    }


}
