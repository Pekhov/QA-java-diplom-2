package ru.practicum;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pekhov A.V.
 * @created 05/02/2022 - 15:36
 */

public class UserTest extends BaseTest {

    @Test
    public void registerUserTest() throws JsonProcessingException {
        User user = getFakeUser();
        Response response = user.register();
        assertEquals(200, response.statusCode());
        assertTrue(response.then().extract().jsonPath().getBoolean("success"));
    }

    @Test
    public void alreadyRegisteredUserTest() throws JsonProcessingException {
        User user = getFakeUser();
        user.register();
        Response alreadyResponse = user.register();
        assertEquals(403, alreadyResponse.statusCode());
        assertFalse(alreadyResponse.then().extract().jsonPath().getBoolean("success"));
        assertEquals("User already exists", alreadyResponse.then().extract().jsonPath().getString("message"));
    }

    @Test
    public void registerWithoutNameTest() throws JsonProcessingException {
        User user = new User("", faker.internet().emailAddress(), faker.internet().password());
        Response response = user.register();
        assertEquals(403, response.statusCode());
        assertFalse(response.then().extract().jsonPath().getBoolean("success"));
        assertEquals("Email, password and name are required fields", response.then().extract().jsonPath().getString("message"));
    }
}
