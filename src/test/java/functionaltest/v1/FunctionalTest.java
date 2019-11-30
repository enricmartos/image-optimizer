package functionaltest.v1;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = {"pretty", "json:build/cucumber/v1/cucumber.json", "html:build/reports/cucumber/v1/"},
        strict = true,
        tags = {"~@ignore"},
        features = {"classpath:functionaltest"}
)

public class FunctionalTest extends AbstractTestNGCucumberTests {

}
