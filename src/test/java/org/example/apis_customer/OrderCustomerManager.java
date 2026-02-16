package org.example.apis_customer;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.specs_variables.BaseApi;

import static io.restassured.RestAssured.given;

public class OrderCustomerManager {
    private static final String ORDERS_ENDPOINT="/orders";

    public Response getOrders(String token){
        return given()
                .spec(BaseApi.getRequestSpecification())
                .header("Authorization", "Bearer " + token)
                .when()
                .get(ORDERS_ENDPOINT);
    }

    public Response checkoutOrder(String id, String token){
        return given()
                .spec(BaseApi.getRequestSpecification())
                .header("Authorization", "Bearer " + token)
                .when()
                .post(ORDERS_ENDPOINT + "/" + id + "/" + "checkout" );
    }
}
