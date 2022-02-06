package ru.practicum;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pekhov A.V.
 * @created 05/02/2022 - 16:20
 */

public class LoginTest extends BaseTest {

    @Test
    public void loginUserTest() throws JsonProcessingException {
        User user = getFakeUser();
        user.register();
        Response response = user.login();
        assertEquals(200, response.statusCode());
        assertTrue(response.then().extract().jsonPath().getBoolean("success"));
    }

    @Test
    public void incorrectEmailAndPasswordloginUserTest() throws JsonProcessingException {
        User user = getFakeUser();
        Response response = user.login();
        assertEquals(401, response.statusCode());
        assertFalse(response.then().extract().jsonPath().getBoolean("success"));
        assertEquals("email or password are incorrect", response.then().extract().jsonPath().getString("message"));
    }
}
