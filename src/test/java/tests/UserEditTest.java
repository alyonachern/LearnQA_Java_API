package tests;

import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Work with USER")
@Story("USER-4")
public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String otherUserId = "2";
    String cookie;
    String header;
    String userId;

    @Test
    @DisplayName("Happy path edit user")
    @Severity(SeverityLevel.CRITICAL)
    public void testEditJustCreatedUser() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.createUser(userData);
        userId = responseCreateAuth.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests.loginUser(authData);

        cookie = this.getCookie(responseGetAuth, "auth_sid");
        header = this.getHeader(responseGetAuth, "x-csrf-token");

        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie,
                editData);

        Response responseUserData = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie);

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @DisplayName("Edit user being not authorized")
    @Severity(SeverityLevel.NORMAL)
    @Owner("alyonachern")
    public void testEditUserNotAuthorized() {
        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("lastName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequestWithoutTokenAndCookie(
                "https://playground.learnqa.ru/api/user/" + otherUserId,
                editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonHasField(responseEditUser, "error");
    }

    @Test
    @DisplayName("Edit other user")
    @Severity(SeverityLevel.MINOR)
    @Owner("alyonachern")
    public void testEditOtherUser() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseGetAuth = apiCoreRequests.createAndLoginRandomUser(userData);

        cookie = this.getCookie(responseGetAuth, "auth_sid");
        header = this.getHeader(responseGetAuth, "x-csrf-token");

        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditAuth = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + otherUserId,
                header, cookie,
                editData);

        Assertions.assertResponseCodeEquals(responseEditAuth, 400);
        Assertions.assertJsonHasField(responseEditAuth, "error");
    }

    @Test
    @DisplayName("Edit user with non-valid Email")
    @Severity(SeverityLevel.MINOR)
    @Owner("alyonachern")
    public void testEditUserWithNonValidEmail() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseGetAuth = apiCoreRequests.createAndLoginRandomUser(userData);
        userId = apiCoreRequests.getSameUserId(responseGetAuth);

        cookie = this.getCookie(responseGetAuth, "auth_sid");
        header = this.getHeader(responseGetAuth, "x-csrf-token");

        String email = DataGenerator.getRandomNonValidEmail();
        Map<String, String> editData = new HashMap<>();
        editData.put("email", email);

        Response responseEditAuth = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie,
                editData);

        Assertions.assertResponseCodeEquals(responseEditAuth, 400);
        Assertions.assertJsonHasField(responseEditAuth, "error");
    }

    @Test
    @DisplayName("Edit user with short firstname (1 character)")
    @Severity(SeverityLevel.MINOR)
    @Owner("alyonachern")
    public void testEditUserWithVeryShortFirstname() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseGetAuth = apiCoreRequests.createAndLoginRandomUser(userData);
        userId = apiCoreRequests.getSameUserId(responseGetAuth);

        cookie = this.getCookie(responseGetAuth, "auth_sid");
        header = this.getHeader(responseGetAuth, "x-csrf-token");

        String newName = "A";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditAuth = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie,
                editData);

        Assertions.assertResponseCodeEquals(responseEditAuth, 400);
        Assertions.assertJsonHasField(responseEditAuth, "error");
    }
}
