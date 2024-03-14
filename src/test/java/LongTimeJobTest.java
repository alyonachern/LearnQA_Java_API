import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class LongTimeJobTest {

    @Test
    public void longTimeJobTest() {
        String url = "https://playground.learnqa.ru/ajax/api/longtime_job";

        JsonPath firstResponse = RestAssured
                .get(url)
                .jsonPath();

        String token = firstResponse.get("token");
        int seconds = firstResponse.get("seconds");

        JsonPath secondResponse = RestAssured
                .given()
                .queryParam("token", token)
                .when()
                .get(url)
                .jsonPath();

        String status = secondResponse.get("status");

        if (status.equals("Job is NOT ready")) {
            try {
                Thread.sleep(seconds * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        JsonPath thirdResponse = RestAssured
                .given()
                .queryParam("token", token)
                .when()
                .get(url)
                .jsonPath();

        status = thirdResponse.get("status");
        String result = thirdResponse.get("result");
        System.out.println(status);
        System.out.println(result);
    }
}
