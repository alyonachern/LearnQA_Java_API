import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckUserAgentTest {

    @ParameterizedTest
    @CsvFileSource(resources = "userAgent.csv", numLinesToSkip = 0, delimiter = ':')
    public void checkUserAgentTest(String userAgent, String platform, String browser, String device) {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", userAgent);

        JsonPath response = RestAssured
                .given()
                .headers(headers)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        String actualPlatform = response.getString("platform");
        String actualBrowser = response.getString("browser");
        String actualDevice = response.getString("device");

        assertEquals(platform, actualPlatform, "Platforms doesn't match");
        assertEquals(browser, actualBrowser, "Browsers doesn't match");
        assertEquals(device, actualDevice, "Devices doesn't match");
    }
}
