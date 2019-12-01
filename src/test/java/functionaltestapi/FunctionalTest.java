package functionaltestapi;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = { "pretty", "html:target/calculator/features" },
        features = "classpath:functionaltestapi/features/api.feature"
)
public class FunctionalTest {
}