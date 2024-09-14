import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.Assert;


public class Steps {

    Logger logger = LogManager.getLogger(Steps.class);
    RequestSpecification request;
    JSONObject requestBody = new JSONObject();
    Response response;


    @Given("base url {string}")
    public void base_url(String baseURL) {
        request = RestAssured.given()
                .baseUri(baseURL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    @Given("user has endpoint {string}")
    public void user_has_endpoint(String endpoint) {
        request = request.basePath(endpoint);
    }

    @When("user provides valid token")
    public void user_provides_valid_token() {
        request = request.auth().oauth2(APIUtils.getToken());
    }

    @When("user provides request body with {string} and {string}")
    public void user_provides_request_body_with_and(String key, String value) {
        requestBody.put(key, value);
        request = request.body(requestBody.toString());
    }

    @When("user hits POST request")
    public void user_hits_post_request() {
        response = request.post();
    }

    @Then("verify status code is {int}")
    public void verify_status_code_is(Integer statusCode) {
        System.out.println(response.prettyPrint());
        Assert.assertEquals((int) statusCode, response.statusCode());
    }
}
