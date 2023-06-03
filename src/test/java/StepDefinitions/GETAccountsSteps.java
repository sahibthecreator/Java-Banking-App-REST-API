package StepDefinitions;

import io.cucumber.cienvironment.internal.com.eclipsesource.json.Json;
import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonArray;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.springframework.http.HttpHeaders;

public class GETAccountsSteps extends BaseStepDefinitions{

    Response response;
    HttpHeaders headers = new HttpHeaders();

    @Given("The login endpoint is available")
    public void theLoginEndpointIsAvailable() {
        response = RestAssured.given()
                .when()
                .get("http://localhost:8080/auth/login");
        String BearerToken = response.jsonPath().getString("jwtToken");
        headers.set("Authorization", "Bearer " + BearerToken);

    }

    @When("I send a GET request to {string} with the bearer token")
    public void iSendAGETRequestToWithTheBearerToken(String endpoint) {
        response = RestAssured.given()
                .headers(headers)
                .when()
                .get(endpoint);

        int statusCode = response.getStatusCode();
    }

    @Then("I should get a {int} status code")
    public void iShouldGetAStatusCode(int expectedStatusCode) {
        int statusCode = response.getStatusCode();
        Assert.assertEquals(expectedStatusCode, statusCode);
    }

    @Then("Get a list of all accounts")
    public void getAListOfAllAccounts() {
        String responseBody = response.getBody().asString();
        try {
            JsonArray jsonArray = Json.parse(responseBody).asArray();
            Assert.assertTrue(jsonArray.size() > 0);
        } catch (Exception e) {
            Assert.fail("Response body is not a JSON array");
        }
    }
}
