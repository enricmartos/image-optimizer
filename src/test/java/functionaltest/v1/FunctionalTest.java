package functionaltest.v1;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = { "pretty", "html:target/calculator/features" },
        tags = {"~@ignore"},
        features = "classpath:functionaltest/features"
)
public class FunctionalTest {
}