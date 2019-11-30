package functionaltestcodely.v1.steps;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import functionaltestcodely.v1.model.calculator.Calculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CalculatorSteps {

    private Calculator calculator;

    @Before
    public void setUp() {
        calculator = new Calculator();
    }

    @Given("^I have a calculator$")
    public void i_have_a_calculator() throws Throwable {
        assertNotNull(calculator);
    }

    @When("^I add (\\d+) and (\\d+)$")
    public void i_add_and(int arg1, int arg2) throws Throwable {
        calculator.sum(arg1, arg2);
    }

    @Then("^the result should be (\\d+)$")
    public void the_result_should_be(int arg1) throws Throwable {
        assertEquals(arg1, calculator.currentValue());
    }

    @When("^I subtract (\\d+) and (\\d+)$")
    public void i_subtract_and(int arg1, int arg2) throws Throwable {
        calculator.subtract(arg1, arg2);
    }

    @When("^I multiply (\\d+) and (\\d+)$")
    public void i_multiply_and(int arg1, int arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        calculator.multiply(arg1, arg2);
    }

    @When("^I divide (\\d+) and (\\d+)$")
    public void i_divide_and(int arg1, int arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        calculator.divide(arg1, arg2);
    }


}
