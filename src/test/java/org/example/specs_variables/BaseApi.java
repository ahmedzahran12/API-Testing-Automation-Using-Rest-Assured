package org.example.specs_variables;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.utils.ConfigManager;

public class BaseApi {

    public static RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getProperty("base.url"))
                .setContentType(ContentType.JSON)
                .build();
    }
}