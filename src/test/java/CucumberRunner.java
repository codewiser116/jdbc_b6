import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions (
        plugin = {"html:target/cucumberReports/cucumber.html"},
        glue = "",
        features = {"src/test/resources/features"},
        tags = "@Test"

)



public class CucumberRunner {

}

