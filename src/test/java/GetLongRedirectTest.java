import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class GetLongRedirectTest {

    @Test
    public void getLongRedirectTest() {
        Response response;
        String url = "https://playground.learnqa.ru/api/long_redirect";

        do {
            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .andReturn();

            url = response.getHeader("Location");
            System.out.println(url);
        } while (response.getStatusCode() != 200);
    }
}
