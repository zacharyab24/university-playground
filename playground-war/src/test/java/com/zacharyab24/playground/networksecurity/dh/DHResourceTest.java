package com.zacharyab24.playground.networksecurity.dh;

import com.zacharyab24.playground.os.BaseResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DHResourceTest extends BaseResourceTest {

    @Override
    protected Set<Class<?>> getResourceClasses() {
        return Set.of(DHResource.class);
    }

    @Test
    void postGenerate_returnsKeyPair() {
        given()
        .when()
            .post("/crypto/dh/generate")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("privateKey", notNullValue())
            .body("publicKey", notNullValue());
    }

    @Test
    void postSession_deriveSharedKey() {
        // Generate two key pairs
        String alicePrivate = given().post("/crypto/dh/generate").then().extract().path("privateKey");
        String alicePublic = given().post("/crypto/dh/generate").then().extract().path("publicKey");
        // Use a second pair for bob — reuse the generate endpoint
        String bobKeys = given().post("/crypto/dh/generate").then().extract().body().asString();
        String bobPrivate = io.restassured.path.json.JsonPath.from(bobKeys).getString("privateKey");
        String bobPublic = io.restassured.path.json.JsonPath.from(bobKeys).getString("publicKey");

        // Alice derives session key using Bob's public key
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"remotePublicKey": "%s", "localPrivateKey": "%s"}
                """.formatted(bobPublic, alicePrivate))
        .when()
            .post("/crypto/dh/session")
        .then()
            .statusCode(200)
            .body("sessionKey", notNullValue())
            .body("hashedSessionKey", notNullValue());
    }

    @Test
    void postSession_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/crypto/dh/session")
        .then()
            .statusCode(400);
    }
}