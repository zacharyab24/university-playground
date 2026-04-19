package com.zacharyab24.playground.networksecurity.hmac;

import com.zacharyab24.playground.os.BaseResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HMACResourceTest extends BaseResourceTest {

    @Override
    protected Set<Class<?>> getResourceClasses() {
        return Set.of(HMACResource.class);
    }

    @Test
    void postHmac_returnsHexString() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"key": "secret", "message": "Hello, World!"}
            """)
        .when()
            .post("/crypto/hmac")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("hmac", notNullValue())
            .body("hmac", matchesPattern("[0-9a-f]+"));
    }

    @Test
    void postHmac_sameInputSameOutput() {
        String hmac1 = given()
            .contentType(ContentType.JSON)
            .body("""
                {"key": "key", "message": "msg"}
            """)
        .when()
            .post("/crypto/hmac")
        .then()
            .statusCode(200)
            .extract().path("hmac");

        String hmac2 = given()
            .contentType(ContentType.JSON)
            .body("""
                {"key": "key", "message": "msg"}
            """)
        .when()
            .post("/crypto/hmac")
        .then()
            .statusCode(200)
            .extract().path("hmac");

        assertThat(hmac1).isEqualTo(hmac2);
    }

    @Test
    void postHmac_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/crypto/hmac")
        .then()
            .statusCode(400);
    }
}