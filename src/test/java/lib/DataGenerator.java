package lib;

import com.github.javafaker.Faker;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DataGenerator {
    public static String getRandomValidEmail() {
        Faker faker = new Faker(new Locale("en-GB"));
        return faker.internet().emailAddress();
    }

    public static String getRandomNonValidEmail() {
        String timestamp = new SimpleDateFormat("yyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "example.com";
    }

    public static String getNameWithDetectedLength(int length) {
        Faker faker = new Faker(new Locale("en-GB"));
        return faker.lorem().fixedString(length);
    }

    public static Map<String, String> getRegistrationData() {
        Faker faker = new Faker(new Locale("en-GB"));
        Map<String, String> data = new HashMap<>();
        data.put("email", getRandomValidEmail());
        data.put("password", faker.internet().password());
        data.put("username", faker.name().username());
        data.put("firstName", faker.name().firstName());
        data.put("lastName", faker.name().lastName());

        return data;
    }

    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues) {
        Map<String, String> defaultValues = getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key : keys) {
            if (nonDefaultValues.containsKey(key)) {
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }

    public static Map<String, String> fillDataWithoutOneField(String field) {
        Map<String, String> defaultValues = getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key : keys) {
            if (field.equals(key)) {
                userData.put(key, null);
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }
}
