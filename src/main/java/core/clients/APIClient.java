package core.clients;

import core.settings.APIEndpoints;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class APIClient {

    private final String baseURL;
    private String token;
    private final String admin_username;
    private final String admin_password;

    public String getAdmin_username() {
        return admin_username;
    }

    public String getAdmin_password() {
        return admin_password;
    }

    public APIClient() {
        this.baseURL = takeProperty("baseURL");
        this.admin_username = takeProperty("admin_username");
        this.admin_password = takeProperty("admin_password");
    }

    private String takeProperty(String propertyName) {
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

        return properties.getProperty(propertyName);
    }

    private RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(baseURL)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .filter(addAuthTokenFilter());
    }

    public void createToken(String username, String password) {
        String requestBody = String.format("{ \"username\": \"%s\", \"password\": \"%s\" }", username, password);

        Response response = getRequestSpec()
                .body(requestBody)
                .when()
                .post(APIEndpoints.AUTH.getPath())
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        token = response.jsonPath().getString("token");
    }

    private Filter addAuthTokenFilter() {
        return (FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext context) -> {
            if (token != null) {
                requestSpec.header("Cookie", "token=" + token);
            }
            return context.next(requestSpec, responseSpec);
        };
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

    public Response getBookingIds() {
        return getRequestSpec()
                .when()
                .get(APIEndpoints.BOOKING.getPath())
                .then()
                .log().body()
                .extract()
                .response();
    }

    public Response getBookingById(int bookingId) {
        return getRequestSpec()
                .pathParam("id", bookingId)
                .when()
                .get(APIEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .log().body()
                .extract()
                .response();
    }

    public Response deleteBookingById(int bookingId) {
        return getRequestSpec()
                .pathParam("id", bookingId)
                .when()
                .delete(APIEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .log().body()
                .extract()
                .response();
    }

    public Response createBooking(String body) {
        return getRequestSpec()
                .body(body)
                .when()
                //.log().body()
                .post(APIEndpoints.BOOKING.getPath())
                .then()
                .log().body()
                .extract()
                .response();
    }

    public Response updateBooking(int bookingId, String body) {
        return getRequestSpec()
                .pathParam("id", bookingId)
                .body(body)
                .when()
                .log().body()
                .put(APIEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .log().body()
                .extract()
                .response();
    }

    public Response partialUpdateBooking(int bookingId, String body) {
        return getRequestSpec()
                .pathParam("id", bookingId)
                .body(body)
                .when()
                .log().body()
                .patch(APIEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .log().body()
                .extract()
                .response();
    }
}
