package org.example.apis_customer;

import io.restassured.response.Response;
import org.example.pojos_bodies.request.adminrequests.AdminLoginPojo;
import org.example.pojos_bodies.request.customerrequests.CustomerLoginPojo;
import org.example.specs_variables.BaseApi;

import static io.restassured.RestAssured.given;

public class CustomerLogin {
    private static final String LOGIN_ENDPOINT = "/login";
    public Response login(CustomerLoginPojo credentials){
        return given()
                .spec(BaseApi.getRequestSpecification())
                .body(credentials)
                .when()
                .post(LOGIN_ENDPOINT);
    }
}
