package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequestWithBody(
                "https://playground.learnqa.ru/api/user/",
                userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequestWithBody(
                "https://playground.learnqa.ru/api/user/",
                userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    public void testCreateUserWithNonValidEmail() {
        String email = DataGenerator.getRandomNonValidEmail();
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequestWithBody(
                "https://playground.learnqa.ru/api/user/",
                userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @Test
    public void testCreateUserWithVeryShortName() {
        String lastName = DataGenerator.getNameWithDetectedLength(1);
        Map<String, String> userData = new HashMap<>();
        userData.put("lastName", lastName);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequestWithBody(
                "https://playground.learnqa.ru/api/user/",
                userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'lastName' field is too short");
    }

    @Test
    public void testCreateUserWithVeryLongName() {
        String lastName = DataGenerator.getNameWithDetectedLength(251);
        Map<String, String> userData = new HashMap<>();
        userData.put("lastName", lastName);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequestWithBody(
                "https://playground.learnqa.ru/api/user/",
                userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'lastName' field is too long");
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    public void testCreateUserWithoutKey(String key) {
        Map<String, String> userData = DataGenerator.fillDataWithoutOneField(key);

        Response responseCreateAuth = apiCoreRequests.makePostRequestWithBody(
                "https://playground.learnqa.ru/api/user/",
                userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + key);
    }
}
