package functionaltestapi.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

public class ApiSteps {

    private World world;

    @Inject
    public ApiSteps(World world) {
        this.world = world;
    }

    @Given("^I set GET request$")
    public void setGetRequest() {
        this.world.setRestClient();
    }

    @When("^I do GET request$")
    public void doGetRequest() {
        this.world.doRestRequest(world.getTarget());
    }

    @Then("^I receive valid HTTP response code (\\d+)$")
    public void receiveValidResponse(int arg1) {
        assertEquals(arg1, this.world.getResponse().getStatus());
    }
}
