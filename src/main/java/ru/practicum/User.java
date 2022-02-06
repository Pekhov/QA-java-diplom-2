package ru.practicum;

import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.internal.shadowed.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * @author Pekhov A.V.
 * @created 05/02/2022 - 15:29
 */

public class User {

    private String name;
    private String email;
    private String password;
    protected String accessToken = "";

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Step
    public Response register() throws JsonProcessingException {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(createFullFieldsRequestBody(password, email, name))
                .when()
                .post("/api/auth/register");
    }

    @Step
    public Response login() throws JsonProcessingException {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(createAuthorizationRequestBody())
                .when()
                .post("/api/auth/login");
        accessToken = response.then().extract().jsonPath().getString("accessToken");
        return response;
    }

    @Step
    public Response getUserData() {
        return given()
                .header("Authorization", accessToken)
                .when()
                .get("/api/auth/user");
    }

    @Step Response getOrders() {
        return new Order().getUserOrders(this);
    }

    @Step
    public Response updateUserData(String newUserData) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .queryParam("accessToken", accessToken)
                .and()
                .body(newUserData)
                .when()
                .patch("/api/auth/user");
    }

    private String createAuthorizationRequestBody() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("password", password);
        json.put("email", email);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

    @Step
    public Response createOrder(List<String> ingredients) throws JsonProcessingException {
        return new Order().createOrder(ingredients, this);
    }



    public String createFullFieldsRequestBody(String password, String email, String name) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        if (password.isEmpty()) {
            json.put("password", this.password);
        } else {
            json.put("password", password);
        }
        if (email.isEmpty()) {
            json.put("email", this.email);
        } else {
            json.put("email", email);
        }
        if (name.isEmpty()) {
            json.put("name", this.name);
        } else {
            json.put("name", name);
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

}
