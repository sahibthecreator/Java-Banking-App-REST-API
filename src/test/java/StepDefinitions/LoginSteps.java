package StepDefinitions;

import com.bank.app.restapi.dto.LoginResponseDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class LoginSteps extends BaseStepDefinitions {

    private Response response;
    @Given("the login endpoint is available")
    public void theLoginEndpointIsAvailable() {
        response = RestAssured.given()
                .when()
                .get("/auth/login");
    }

    @When("I send a POST request to {string} with the following credentials:")
    public void iSendAPOSTRequestToWithTheFollowingCredentials(String endpoint, Map<String, String> credentials) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", credentials.get("email"));
        requestBody.put("password", credentials.get("password"));

        response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .post(endpoint);

        int statusCode = response.getStatusCode();
    }

    @Then("The response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        int statusCode = response.getStatusCode();
        assertEquals(expectedStatusCode, statusCode);
    }

    @And("The response body should contain a user id \\{string} and a JWT token \\{string}")
    public void theResponseBodyShouldContainAUserIdStringAndAJWTTokenString(String expectedUserId, String expectedJwtToken) {
        LoginResponseDTO loginResponseDTO = response.getBody().as(LoginResponseDTO.class);
        assertEquals(UUID.fromString(expectedUserId), loginResponseDTO.getUserId());
        assertEquals(expectedJwtToken, loginResponseDTO.getJwtToken());
    }

}
