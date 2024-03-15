import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetPasswordTest {
    List<String> passwordList = new ArrayList<>();

    @Test
    public void getPasswordTest() {
        Map<String, String> cred = new HashMap<>();

        cred.put("login", "super_admin");

        Response responseForPost, responseForGet;
        String firstRequest = "https://playground.learnqa.ru/ajax/api/get_secret_password_homework",
                secondRequest = "https://playground.learnqa.ru/ajax/api/check_auth_cookie";

        for (int i = 0; i < getList().size(); i++) {
            String password = passwordList.get(i);
            cred.put("password", password);
            responseForPost = RestAssured
                    .given()
                    .body(cred)
                    .when()
                    .post(firstRequest)
                    .andReturn();

            String responseCookie = responseForPost.getCookie("auth_cookie");
            Map<String, String> cookie = new HashMap<>();
            if (responseCookie != null) {
                cookie.put("auth_cookie", responseCookie);
            }

            responseForGet = RestAssured
                    .given()
                    .cookies(cookie)
                    .when()
                    .get(secondRequest)
                    .andReturn();

            String message = responseForGet.then()
                    .extract()
                    .asString();
            if (message.equals("You are authorized")) {
                responseForGet.print();
                System.out.println(password);
                break;
            }
        }
    }

    public List<String> getList() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/password.txt"))) {
            passwordList = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return passwordList;
    }
}
