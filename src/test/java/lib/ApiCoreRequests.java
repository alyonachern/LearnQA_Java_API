package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
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

    @Step("Make simple GET-request")
    public Response makeGetRequest(String url) {
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

    @Step("Make GET-request with token and cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make PUT-request with token, cookie and body")
    public Response makePutRequest(String url, String token, String cookie, Map<String, String> body) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(body)
                .put(url)
                .andReturn();
    }

    @Step("Make PUT-request without token, cookie and body")
    public Response makePutRequestWithoutTokenAndCookie(String url, Map<String, String> body) {
        return given()
                .filter(new AllureRestAssured())
                .body(body)
                .put(url)
                .andReturn();
    }

    @Step("CreateUser")
    public JsonPath createUser(Map<String, String> body) {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        return given()
                .filter(new AllureRestAssured())
                .body(body)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
    }

    @Step("Login user")
    public Response loginUser(Map<String, String> data) {
        return makePostRequestWithBody("https://playground.learnqa.ru/api/user/login", data);
    }

    public Response createAndLoginRandomUser(Map<String, String> data) {
        createUser(data);

        Map<String, String> authData = new HashMap<>();
        authData.put("email", data.get("email"));
        authData.put("password", data.get("password"));
        return loginUser(authData);
    }

    public String getSameUserId(Response response) {
        JsonPath body = response.jsonPath();
        return body.getString("user_id");
    }
}