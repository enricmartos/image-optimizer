package functionaltest.v1.steps;


import com.google.inject.Inject;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import functionaltest.v1.World;
import functionaltest.v1.model.Calculator;
import functionaltest.v1.model.CalculatorArgs;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class CalculatorSteps {

    private World world;

    @Inject
    public CalculatorSteps(World world) {
        this.world = world;
    }

//    @Before
//    public void setUp() {
//        calculator = new Calculator();
//    }

    @Given("^([^ ]+) a calculator$")
    public void i_have_a_calculator(String reference) throws Throwable {
        this.world.addClient(reference, new Calculator());
    }

    @When("^([^ ]+) adds two values")
    public void i_add_and(String reference, List<CalculatorArgs> args) throws Throwable {
        this.world.getClient(reference).sum(args.get(0).getArg1(), args.get(0).getArg2());
    }

    @Then("^The result of ([^ ]+) should be the sum of these 2 values")
    public void the_result_should_be(String reference,  List<CalculatorArgs> args) throws Throwable {
        assertEquals(world.getClient(reference).currentValue(), args.get(0).getSum());
    }



}
