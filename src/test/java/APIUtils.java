import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

public class APIUtils {

    public static String getToken(){
        String endPoint = "https://backend.cashwise.us/api/myaccount/auth/login";

        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "benazir@gmail.com");
        requestBody.put("password", "abc123");

        Response response = RestAssured.given().contentType(ContentType.JSON)
                .body(requestBody.toString()).post(endPoint);

        return response.jsonPath().getString("jwt_token");
    }
}
