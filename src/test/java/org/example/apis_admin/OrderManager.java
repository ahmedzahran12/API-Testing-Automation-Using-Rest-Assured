package org.example.apis_admin;

import io.restassured.response.Response;
import org.example.pojos_bodies.request.adminrequests.CreateOrderPojo;
import org.example.specs_variables.BaseApi;

import static io.restassured.RestAssured.given;

public class OrderManager {
    private static final String ORDER_ENDPOINT="/orders";

    public Response createOrder( String token, CreateOrderPojo body){
        return given()
                .spec(BaseApi.getRequestSpecification())
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .post(ORDER_ENDPOINT);

    }
    public Response getOrders( String token){
        return given()
                .spec(BaseApi.getRequestSpecification())
                .header("Authorization", "Bearer " + token)
                .when()
                .get(ORDER_ENDPOINT);

    }
    public Response deleteOrder(String id , String token){
        return given()
                .spec(BaseApi.getRequestSpecification())
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(ORDER_ENDPOINT+"/" + id);

    }
    public Response getPaidOrders(String token){
        return given()
                .spec(BaseApi.getRequestSpecification())
                .header("Authorization", "Bearer " + token)
                .when()
                .get(ORDER_ENDPOINT+"/paid");
    }
}
