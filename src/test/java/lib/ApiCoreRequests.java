package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make POST-request with body")
    public Response makePostRequestWithBody(String url, Map<String, String> body) {
        return given()
                .filter(new AllureRestAssured())
                .body(body)
                .post(url)
                .andReturn();
    }
}