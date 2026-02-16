package org.example.apis_customer;

import io.restassured.response.Response;
import org.example.specs_variables.BaseApi;

import static io.restassured.RestAssured.given;

public class ItemViewer {
    private static final String ITEMS_ENDPOINT="/items";

    public Response getItems(String token){
        return given()
                .spec(BaseApi.getRequestSpecification())
                .header("Authorization", "Bearer " + token)
                .when()
                .get(ITEMS_ENDPOINT);
    }
}
