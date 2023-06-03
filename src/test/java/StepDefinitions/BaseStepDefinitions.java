package StepDefinitions;

import io.cucumber.junit.CucumberOptions;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberOptions(features = "src/test/resources/features", plugin = {"pretty", "html:target/cucumber"})
public class BaseStepDefinitions {

}
