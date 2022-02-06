package ru.practicum;

import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.internal.shadowed.jackson.databind.node.ArrayNode;
import io.qameta.allure.internal.shadowed.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * @author Pekhov A.V.
 * @created 05/02/2022 - 21:51
 */

public class Order {

    @Step
    public Response createOrder(List<String> ingredients, User user) throws JsonProcessingException {
        RequestSpecification request = given();
        if(!user.accessToken.isEmpty()) { request.header("Authorization", user.accessToken); }
        return request
                .header("Content-type", "application/json")
                .header("Connection", "close")
                .and()
                .body(createOrderRequestBody(ingredients))
                .when()
                .post("/api/orders");
    }

    @Step
    public Response getUserOrders(User user) {
        RequestSpecification request = given();
        if(!user.accessToken.isEmpty()) { request.header("Authorization", user.accessToken); }
        return request.given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/orders");
    }

    private String createOrderRequestBody(List<String> ingredients) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        ArrayNode ingredientsArray = mapper.createArrayNode();
        ingredients.forEach(ingredientsArray::add);
        json.put("ingredients", ingredientsArray);

        return mapper.writeValueAsString(json);
    }
}
