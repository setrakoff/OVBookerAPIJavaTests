package core.clients;

import core.settings.APIEndpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class APIClient {

    private final String baseURL;

    public APIClient() {
        this.baseURL = determineBaseURL();
    }

    private String determineBaseURL() {
        String environment = System.getProperty("env", "test");
        String configFileName = "application-" + environment + ".properties";

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new IllegalStateException("File not found: " + configFileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load file: " + configFileName, e);
        }

        return properties.getProperty("baseURL");
    }

    private RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(baseURL)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }

    public Response ping() {
        return getRequestSpec()
                .when()
                .get(APIEndpoints.PING.getPath())
                .then()
                .statusCode(201)
                .extract()
                .response();
    }

    public Response getBooking() {
        return getRequestSpec()
                .when()
                .get(APIEndpoints.BOOKING.getPath())
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}
