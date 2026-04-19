package com.zacharyab24.playground.networksecurity.rsa;

import com.zacharyab24.playground.os.BaseResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RSAResourceTest extends BaseResourceTest {

    @Override
    protected Set<Class<?>> getResourceClasses() {
        return Set.of(RSAResource.class);
    }

    @Test
    void postGenerate_returnsKeyPair() {
        given()
        .when()
            .post("/crypto/rsa/generate")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("publicKey.modulus", notNullValue())
            .body("publicKey.exponent", is("65537"))
            .body("privateKey.prime1", notNullValue())
            .body("privateKey.prime2", notNullValue())
            .body("privateKey.exponent", notNullValue());
    }

    @Test
    void postEncryptDecrypt_roundTrip() {
        // Generate keys
        var keys = given()
            .post("/crypto/rsa/generate")
        .then()
            .statusCode(200)
            .extract().body().asString();

        // Parse out public and private key JSON
        String publicKey = io.restassured.path.json.JsonPath.from(keys).getString("publicKey");
        String modulus = io.restassured.path.json.JsonPath.from(keys).getString("publicKey.modulus");
        String pubExp = io.restassured.path.json.JsonPath.from(keys).getString("publicKey.exponent");
        String prime1 = io.restassured.path.json.JsonPath.from(keys).getString("privateKey.prime1");
        String prime2 = io.restassured.path.json.JsonPath.from(keys).getString("privateKey.prime2");
        String privExp = io.restassured.path.json.JsonPath.from(keys).getString("privateKey.exponent");

        // Encrypt
        String ciphertext = given()
            .contentType(ContentType.JSON)
            .body("""
                {
                  "plaintext": "Test message",
                  "publicKey": {"modulus": "%s", "exponent": "%s"}
                }
                """.formatted(modulus, pubExp))
        .when()
            .post("/crypto/rsa/encrypt")
        .then()
            .statusCode(200)
            .extract().path("ciphertext");

        // Decrypt
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                  "ciphertext": "%s",
                  "privateKey": {"prime1": "%s", "prime2": "%s", "exponent": "%s"}
                }
                """.formatted(ciphertext, prime1, prime2, privExp))
        .when()
            .post("/crypto/rsa/decrypt")
        .then()
            .statusCode(200)
            .body("plaintext", is("Test message"));
    }

    @Test
    void postEncrypt_invalidJson_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("not json")
        .when()
            .post("/crypto/rsa/encrypt")
        .then()
            .statusCode(400);
    }
}