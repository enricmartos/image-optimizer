package functionaltestcodely.v1;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = { "pretty", "html:target/calculator/features" },
        features = "classpath:functionaltestcodely/features/calculator/calculator.feature"
)
public class RunCalculatorTest {
}