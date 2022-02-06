package ru.practicum;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pekhov A.V.
 * @created 05/02/2022 - 16:41
 */

public class ChangeUserDataTest extends BaseTest {

    User user;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        user = getFakeUser();
        user.register();
    }

    @Test
    public void changeAuthorizedEmailNameTest() throws JsonProcessingException {
        user.login();
        String newEmail = getFakeEmail();
        String newUserData = user.createFullFieldsRequestBody("", newEmail, "");
        Response updateResponse = user.updateUserData(newUserData);

        assertEquals(200, updateResponse.statusCode());
        assertEquals(newEmail, updateResponse.then().extract().jsonPath().getString("user.email"));
    }

    @Test
    public void changeAuthorizedUserPasswordTest() throws JsonProcessingException {
        user.login();
        String newPassword = getFakePassword();
        String newUserData = user.createFullFieldsRequestBody("", newPassword, "");
        Response updateResponse = user.updateUserData(newUserData);

        assertEquals(200, updateResponse.statusCode());
    }

    @Test
    public void changeAuthorizedUserNameTest() throws JsonProcessingException {
        user.login();
        String newName = getFakeName();
        String newUserData = user.createFullFieldsRequestBody("", "", newName);
        Response updateResponse = user.updateUserData(newUserData);

        assertEquals(200, updateResponse.statusCode());
        assertEquals(newName, updateResponse.then().extract().jsonPath().getString("user.name"));
    }

    @Test
    public void changeUnAuthorizedEmailNameTest() throws JsonProcessingException {
        String newEmail = getFakeEmail();
        String newUserData = user.createFullFieldsRequestBody("", newEmail, "");
        Response updateResponse = user.updateUserData(newUserData);

        assertEquals(401, updateResponse.statusCode());
        assertFalse(updateResponse.then().extract().jsonPath().getBoolean("success"));
        assertEquals("You should be authorised", updateResponse.then().extract().jsonPath().getString("message"));
    }

    @Test
    public void changeUnAuthorizedUserPasswordTest() throws JsonProcessingException {
        String newPassword = getFakePassword();
        String newUserData = user.createFullFieldsRequestBody("", newPassword, "");
        Response updateResponse = user.updateUserData(newUserData);

        assertEquals(401, updateResponse.statusCode());
        assertFalse(updateResponse.then().extract().jsonPath().getBoolean("success"));
        assertEquals("You should be authorised", updateResponse.then().extract().jsonPath().getString("message"));
    }

    @Test
    public void changeUnAuthorizedUserNameTest() throws JsonProcessingException {
        String newName = getFakeName();
        String newUserData = user.createFullFieldsRequestBody("", "", newName);
        Response updateResponse = user.updateUserData(newUserData);

        assertEquals(401, updateResponse.statusCode());
        assertFalse(updateResponse.then().extract().jsonPath().getBoolean("success"));
        assertEquals("You should be authorised", updateResponse.then().extract().jsonPath().getString("message"));
    }
}
