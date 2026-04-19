package com.zacharyab24.playground.networksecurity.cbc;

import com.zacharyab24.playground.os.BaseResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CBCResourceTest extends BaseResourceTest {

    @Override
    protected Set<Class<?>> getResourceClasses() {
        return Set.of(CBCResource.class);
    }

    @Test
    void postGenerateKey_returnsKeyAndIV() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"keySize": 256}
            """)
        .when()
            .post("/crypto/cbc/generate-key")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("key", notNullValue())
            .body("iv", notNullValue());
    }

    @Test
    void postGenerateKey_invalidSize_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"keySize": 512}
            """)
        .when()
            .post("/crypto/cbc/generate-key")
        .then()
            .statusCode(400);
    }

    @Test
    void postEncryptDecrypt_roundTrip() {
        // Generate key
        String keyJson = given()
            .contentType(ContentType.JSON)
            .body("""
                {"keySize": 256}
            """)
        .when()
            .post("/crypto/cbc/generate-key")
        .then()
            .statusCode(200)
            .extract().body().asString();

        String key = io.restassured.path.json.JsonPath.from(keyJson).getString("key");
        String iv = io.restassured.path.json.JsonPath.from(keyJson).getString("iv");

        // Encrypt
        String ciphertext = given()
            .contentType(ContentType.JSON)
            .body("""
                {"plaintext": "Hello, CBC!", "key": "%s", "iv": "%s"}
                """.formatted(key, iv))
        .when()
            .post("/crypto/cbc/encrypt")
        .then()
            .statusCode(200)
            .extract().path("ciphertext");

        // Decrypt
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"ciphertext": "%s", "key": "%s", "iv": "%s"}
                """.formatted(ciphertext, key, iv))
        .when()
            .post("/crypto/cbc/decrypt")
        .then()
            .statusCode(200)
            .body("plaintext", is("Hello, CBC!"));
    }

    @Test
    void postEncrypt_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/crypto/cbc/encrypt")
        .then()
            .statusCode(400);
    }
}