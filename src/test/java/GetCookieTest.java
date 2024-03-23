import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetCookieTest {

    @Test
    public void getCookieTest() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Map<String, String> cookie = response.getCookies();
        assertTrue(cookie.containsKey("HomeWork"), "Cookie with key 'HomeWork' doesn't exist");
        assertEquals("hw_value", response.getCookie("HomeWork"), "Value of cookie HomeWork differs");
    }
}
