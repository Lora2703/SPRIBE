package apiTests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import util.PropertyLoader;

public class RequestSpecifications {

    public static RequestSpecification basicSpec = new RequestSpecBuilder()
            .setBaseUri(PropertyLoader.getProperty("URL"))
            .setContentType(ContentType.JSON)
            .setAccept("application/json, text/plain, */*")
            .addHeader("Connection", "keep-alive")
            .build();
}
