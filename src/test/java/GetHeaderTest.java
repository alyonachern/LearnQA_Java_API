import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetHeaderTest {

    @Test
    public void getHeaderTest() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        Headers header = response.getHeaders();
        assertTrue(header.hasHeaderWithName("X-Secret-Homework-Header"));
        assertEquals("Some secret value", response.getHeader("X-Secret-Homework-Header"));
    }
}
