package ru.practicum;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pekhov A.V.
 * @created 05/02/2022 - 21:52
 */

public class OrderTest extends BaseTest {

    private User user;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        user = getFakeUser();
        user.register();
    }

    @Test
    public void orderWithAuthorizationAndIngredientsTest() throws JsonProcessingException {
        ArrayList<String> ingredientsHashes = addSomeIngredientsHashes();
        user.login();
        Response orderResponse = user.createOrder(ingredientsHashes);

        assertEquals(200, orderResponse.statusCode());
        assertTrue(orderResponse.then().extract().jsonPath().getBoolean("success"));
        assertEquals("Флюоресцентный бессмертный бургер",orderResponse.then().extract().jsonPath().getString("name"));
    }

    @Test
    public void orderUnAuthorizationAndIngredientsTest() throws JsonProcessingException {
        ArrayList<String> ingredientsHashes = addSomeIngredientsHashes();
        Response orderResponse = user.createOrder(ingredientsHashes);

        assertEquals(403, orderResponse.statusCode());
    }

    @Test
    public void orderWithAuthorizationAndWithoutIngredientsTest() throws JsonProcessingException {
        ArrayList<String> ingredientsHashes = new ArrayList<>();
        user.login();
        Response orderResponse = user.createOrder(ingredientsHashes);

        assertEquals(400, orderResponse.statusCode());
        assertFalse(orderResponse.then().extract().jsonPath().getBoolean("success"));
        assertEquals("Ingredient ids must be provided",orderResponse.then().extract().jsonPath().getString("message"));
    }

    @Test
    public void orderWithAuthorizationAndInvalidIngredientsTest() throws JsonProcessingException {
        ArrayList<String> ingredientsHashes = new ArrayList<>(Arrays.asList("invalidHash"));
        user.login();
        Response orderResponse = user.createOrder(ingredientsHashes);

        assertEquals(500, orderResponse.statusCode());
    }

    @Test
    public void getAuthorizeUserOrders() throws JsonProcessingException {
        ArrayList<String> ingredientsHashes = addSomeIngredientsHashes();
        user.login();
        user.createOrder(ingredientsHashes);
        Response orders = user.getOrders();

        assertEquals(200, orders.statusCode());
        assertTrue(orders.then().extract().jsonPath().getBoolean("success"));
        assertEquals("done",orders.then().extract().jsonPath().getString("orders[0].status"));
    }

    @Test
    public void getUnAuthorizeUserOrders() {
        Response orders = user.getOrders();

        assertEquals(401, orders.statusCode());
        assertFalse(orders.then().extract().jsonPath().getBoolean("success"));
        assertEquals("You should be authorised",orders.then().extract().jsonPath().getString("message"));
    }
}
