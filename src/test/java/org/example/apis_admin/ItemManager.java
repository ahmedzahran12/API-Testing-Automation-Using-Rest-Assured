package org.example.apis_admin;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.pojos_bodies.request.adminrequests.AddItemPojo;
import org.example.pojos_bodies.request.adminrequests.EditItemPojo;
import org.example.specs_variables.BaseApi;

import static io.restassured.RestAssured.given;

public class ItemManager {
    private static final String ITEM_ENDPOINT="/items";

    public Response addItem( String token, AddItemPojo body){
        return given()
                .spec(BaseApi.getRequestSpecification())
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .post(ITEM_ENDPOINT);
    }
    public Response editItem(String id, String token, EditItemPojo body){
        return given()
                .spec(BaseApi.getRequestSpecification())
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .put(ITEM_ENDPOINT + "/" + id);

    }
    public Response deleteItem(String id, String token){
        return given()
                .spec(BaseApi.getRequestSpecification())
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(ITEM_ENDPOINT + "/" + id);


    }

}
