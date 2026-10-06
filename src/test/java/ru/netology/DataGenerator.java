package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder().setBaseUri("http://localhost").setPort(9999).setAccept(ContentType.JSON).setContentType(ContentType.JSON).log(LogDetail.ALL).build();

    private DataGenerator() {
    }

    private static void sendRequest(RegistrationDto user) {
        given().spec(requestSpec).body(user).when().post("/api/system/users").then().statusCode(200); // Ожидаем успешный статус 200 OK
    }

    public static String generateLogin() {
        Faker faker = new Faker(new Locale("en"));
        return faker.name().username();
    }

    public static String generatePassword() {
        Faker faker = new Faker(new Locale("en"));
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        // 1. Создает активного пользователя и сразу регистрирует в системе через API
        public static RegistrationDto generateUser(String status) {
            var user = new RegistrationDto(generateLogin(), generatePassword(), status);
            sendRequest(user);
            return user;
        }

        // 2. Генерирует пользователя, но НЕ регистрирует его по API
        public static RegistrationDto getRegisteredUser(String status) {
            return generateUser(status);
        }

        public static RegistrationDto getUser(String status) {
            return new RegistrationDto(generateLogin(), generatePassword(), status);
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
