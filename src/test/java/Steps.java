import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.Assert;

import java.sql.*;


public class Steps {

    Logger logger = LogManager.getLogger(Steps.class);
    RequestSpecification request;
    JSONObject requestBody = new JSONObject();
    Response response;
    String id;

    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;


    @Given("base url {string}")
    public void base_url(String baseURL) {
        request = RestAssured.given()
                .baseUri(baseURL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        logger.info("base URL: " + baseURL);
    }

    @Given("user has endpoint {string}")
    public void user_has_endpoint(String endpoint) {
        request = request.basePath(endpoint);
        logger.info("endpoint to test: " + endpoint);
    }

    @When("user provides valid token")
    public void user_provides_valid_token() {
        request = request.auth().oauth2(APIUtils.getToken());
        logger.info("user provided valid token");
    }

    @When("user provides request body with {string} and {string}")
    public void user_provides_request_body_with_and(String key, String value) {
        requestBody.put(key, value);
        logger.info("added " + key + " with " + value + " in the request body");
        request = request.body(requestBody.toString());
    }

    @When("user hits POST request")
    public void user_hits_post_request() {
        response = request.post();
        logger.info("POST request sent");

        id = response.jsonPath().getString("id");
        logger.info("retrieved id: " + id);
    }

    @Then("verify status code is {int}")
    public void verify_status_code_is(Integer statusCode) {
        logger.info(response.prettyPrint());
        Assert.assertEquals((int) statusCode, response.statusCode());
    }

    @Given("user set up connection to database")
    public void user_set_up_connection_to_database() throws SQLException {
        String url = "jdbc:postgresql://18.159.52.24:5434/postgres";
        String username = "cashwiseuser";
        String password = "cashwisepass";

        connection = DriverManager.getConnection(url, username, password);
        logger.info("successfully set up connection to Cashwise database");
    }

    @When("user sends the query {string}")
    public void user_sends_the_query(String query) throws SQLException {
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(id));
        resultSet = preparedStatement.executeQuery();

    }

    @Then("verify result set contains id {string}")
    public void verify_result_set_contains_id(String columnName) throws SQLException {
        boolean hasValue = false;

        logger.info("verifying the " + columnName + " contains " + id);

        try {
            while (resultSet.next()) {
                if (resultSet.getString(columnName).equalsIgnoreCase(id)) {
                    logger.info("verified the " + columnName + " contains " + id);
                    hasValue = true;
                    break;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getStackTrace());
        }

        resultSet.close();

        Assert.assertTrue(hasValue);
    }


    @Then("verify result set contains {string} with {string}")
    public void verify_result_set_contains_with(String columnName, String value) throws SQLException {

        boolean hasValue = false;

        logger.info("checking if the " + columnName + " contains " + value);

        try {
            while (resultSet.previous()) {

                logger.info(resultSet.getString("name_tag"));
                logger.info(resultSet.getDate("creation_date"));
                logger.info(resultSet.getInt("tag_id"));

                if (resultSet.getString(columnName).equalsIgnoreCase(value)) {
                    logger.info(resultSet.getString(columnName));
                    logger.info("verified the " + columnName + " contains " + value);
                    hasValue = true;
                    break;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getStackTrace());
        }

        Assert.assertTrue(hasValue);

    }
}
