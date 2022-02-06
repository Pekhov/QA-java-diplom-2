package ru.practicum;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * @author Pekhov A.V.
 * @created 05/02/2022 - 21:51
 */

public class Ingredient {

    @Step
    public Response getIngredients() {
        return given()
                .when()
                .get("/api/ingredients");
    }

}
