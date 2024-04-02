package tests;

import io.qameta.allure.*;
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
@Story("USER-5")
public class UserDeleteTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String cookie, header;
    int userId, otherUserId;

    @Test
    @DisplayName("Delete super user")
    @Severity(SeverityLevel.NORMAL)
    @Owner("alyonachern")
    public void testDeleteSuperUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.loginUser(authData);

        cookie = this.getCookie(responseGetAuth, "auth_sid");
        header = this.getHeader(responseGetAuth, "x-csrf-token");
        userId = this.getIntFromJson(responseGetAuth, "user_id");

        Response responseDelete = apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie
        );

        Assertions.assertResponseCodeEquals(responseDelete, 400);
        Assertions.assertJsonHasField(responseDelete, "error");
    }

    @Test
    @DisplayName("Happy path delete user")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("alyonachern")
    public void testDeleteUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseGetAuth = apiCoreRequests.createAndLoginRandomUser(userData);

        cookie = this.getCookie(responseGetAuth, "auth_sid");
        header = this.getHeader(responseGetAuth, "x-csrf-token");
        userId = this.getIntFromJson(responseGetAuth, "user_id");

        apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie
        );

        Response responseCheckDeletedUser = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie
        );

        Assertions.assertResponseCodeEquals(responseCheckDeletedUser, 404);
        Assertions.assertResponseTextEquals(responseCheckDeletedUser, "User not found");
    }

    @Test
    @DisplayName("Delete other user")
    @Severity(SeverityLevel.NORMAL)
    @Owner("alyonachern")
    public void testDeleteOtherUser() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseGetAuth = apiCoreRequests.createAndLoginRandomUser(userData);

        cookie = this.getCookie(responseGetAuth, "auth_sid");
        header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseDeleteOtherUser = apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/" + otherUserId,
                header, cookie
        );

        Assertions.assertResponseCodeEquals(responseDeleteOtherUser, 400);
        Assertions.assertJsonHasField(responseDeleteOtherUser, "error");
    }
}
