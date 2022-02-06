package ru.practicum;

import com.github.javafaker.Faker;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Locale;

import static io.restassured.RestAssured.*;

/**
 * @author Pekhov A.V.
 * @created 05/02/2022 - 15:34
 */

public class BaseTest {

    Faker faker = new Faker(new Locale("ru"));

    public BaseTest() {
        useRelaxedHTTPSValidation();
        baseURI = "https://stellarburgers.nomoreparties.site";
        enableLoggingOfRequestAndResponseIfValidationFails();
        filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    public User getFakeUser() {
        return new User(getFakeName(), getFakeEmail(), getFakePassword());
    }

    public String getFakePassword() { return faker.internet().password(); }

    public String getFakeEmail() { return faker.internet().emailAddress(); }

    public String getFakeName() { return faker.name().firstName(); }

    public ArrayList<String> addSomeIngredientsHashes() {
        ArrayList<String> ingredientsHashes = new ArrayList<>();
        Response ingredientResponse = new Ingredient().getIngredients();
        ingredientsHashes.add(ingredientResponse.then().extract().jsonPath().getString("data[0]._id"));
        ingredientsHashes.add(ingredientResponse.then().extract().jsonPath().getString("data[1]._id"));
        return ingredientsHashes;
    }

}
