package com.zacharyab24.playground.networksecurity;

import com.zacharyab24.playground.networksecurity.cbc.CBCResource;
import com.zacharyab24.playground.networksecurity.dh.DHResource;
import com.zacharyab24.playground.networksecurity.hmac.HMACResource;
import com.zacharyab24.playground.networksecurity.rsa.RSAResource;
import com.zacharyab24.playground.os.BaseResourceTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * End-to-end test simulating the full client-server crypto handshake:
 * 1. Server generates RSA key pair
 * 2. Client signs a message with RSA, server verifies
 * 3. Both sides generate DH key pairs and exchange public keys
 * 4. Both sides derive the same session key
 * 5. Client generates AES key/IV, encrypts a message with CBC
 * 6. Client computes HMAC over the plaintext using the hashed session key
 * 7. Server decrypts the message and verifies the HMAC
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CryptoE2ETest extends BaseResourceTest {

    @Override
    protected Set<Class<?>> getResourceClasses() {
        return Set.of(RSAResource.class, DHResource.class, CBCResource.class, HMACResource.class);
    }

    @Test
    void fullHandshake_encryptDecryptAndVerify() {
        // === Phase 1: RSA key generation ===
        JsonPath serverRSA = given()
            .post("/crypto/rsa/generate")
        .then()
            .statusCode(200)
            .extract().jsonPath();

        String rsaModulus = serverRSA.getString("publicKey.modulus");
        String rsaPubExp = serverRSA.getString("publicKey.exponent");
        String rsaPrime1 = serverRSA.getString("privateKey.prime1");
        String rsaPrime2 = serverRSA.getString("privateKey.prime2");
        String rsaPrivExp = serverRSA.getString("privateKey.exponent");

        // === Phase 2: RSA sign + verify ===
        String serverId = "Server001";
        String signature = given()
            .contentType(ContentType.JSON)
            .body("""
                {
                  "message": "%s",
                  "privateKey": {"prime1": "%s", "prime2": "%s", "exponent": "%s"}
                }
                """.formatted(serverId, rsaPrime1, rsaPrime2, rsaPrivExp))
        .when()
            .post("/crypto/rsa/sign")
        .then()
            .statusCode(200)
            .extract().path("signature");

        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                  "message": "%s",
                  "signature": "%s",
                  "publicKey": {"modulus": "%s", "exponent": "%s"}
                }
                """.formatted(serverId, signature, rsaModulus, rsaPubExp))
        .when()
            .post("/crypto/rsa/verify")
        .then()
            .statusCode(200)
            .body("valid", is(true));

        // === Phase 3: DH key exchange ===
        JsonPath clientDH = given()
            .post("/crypto/dh/generate")
        .then()
            .statusCode(200)
            .extract().jsonPath();

        JsonPath serverDH = given()
            .post("/crypto/dh/generate")
        .then()
            .statusCode(200)
            .extract().jsonPath();

        String clientDHPrivate = clientDH.getString("privateKey");
        String clientDHPublic = clientDH.getString("publicKey");
        String serverDHPrivate = serverDH.getString("privateKey");
        String serverDHPublic = serverDH.getString("publicKey");

        // === Phase 4: Both sides derive session key ===
        JsonPath clientSession = given()
            .contentType(ContentType.JSON)
            .body("""
                {"remotePublicKey": "%s", "localPrivateKey": "%s"}
                """.formatted(serverDHPublic, clientDHPrivate))
        .when()
            .post("/crypto/dh/session")
        .then()
            .statusCode(200)
            .extract().jsonPath();

        JsonPath serverSession = given()
            .contentType(ContentType.JSON)
            .body("""
                {"remotePublicKey": "%s", "localPrivateKey": "%s"}
                """.formatted(clientDHPublic, serverDHPrivate))
        .when()
            .post("/crypto/dh/session")
        .then()
            .statusCode(200)
            .extract().jsonPath();

        // Session keys must match
        assertThat(clientSession.getString("sessionKey"))
                .isEqualTo(serverSession.getString("sessionKey"));
        assertThat(clientSession.getString("hashedSessionKey"))
                .isEqualTo(serverSession.getString("hashedSessionKey"));

        String hashedSessionKey = clientSession.getString("hashedSessionKey");

        // === Phase 5: Client encrypts message with CBC ===
        JsonPath aesKey = given()
            .contentType(ContentType.JSON)
            .body("""
                {"keySize": 256}
            """)
        .when()
            .post("/crypto/cbc/generate-key")
        .then()
            .statusCode(200)
            .extract().jsonPath();

        String key = aesKey.getString("key");
        String iv = aesKey.getString("iv");

        String secretMessage = "The wormhole is open at coordinates 47.3, -122.0";

        String ciphertext = given()
            .contentType(ContentType.JSON)
            .body("""
                {"plaintext": "%s", "key": "%s", "iv": "%s"}
                """.formatted(secretMessage, key, iv))
        .when()
            .post("/crypto/cbc/encrypt")
        .then()
            .statusCode(200)
            .extract().path("ciphertext");

        assertThat(ciphertext).isNotEqualTo(secretMessage);

        // === Phase 6: Client computes HMAC using hashed session key ===
        String hmac = given()
            .contentType(ContentType.JSON)
            .body("""
                {"key": "%s", "message": "%s"}
                """.formatted(hashedSessionKey, secretMessage))
        .when()
            .post("/crypto/hmac")
        .then()
            .statusCode(200)
            .extract().path("hmac");

        // === Phase 7: Server decrypts and verifies HMAC ===
        String decrypted = given()
            .contentType(ContentType.JSON)
            .body("""
                {"ciphertext": "%s", "key": "%s", "iv": "%s"}
                """.formatted(ciphertext, key, iv))
        .when()
            .post("/crypto/cbc/decrypt")
        .then()
            .statusCode(200)
            .extract().path("plaintext");

        assertThat(decrypted).isEqualTo(secretMessage);

        // Server verifies HMAC
        String serverHmac = given()
            .contentType(ContentType.JSON)
            .body("""
                {"key": "%s", "message": "%s"}
                """.formatted(hashedSessionKey, decrypted))
        .when()
            .post("/crypto/hmac")
        .then()
            .statusCode(200)
            .extract().path("hmac");

        assertThat(serverHmac).isEqualTo(hmac);
    }
}