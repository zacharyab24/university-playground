package com.zacharyab24.playground.networksecurity.rsa;

import com.zacharyab24.seng2250.crypto.exceptions.CryptoException;
import com.zacharyab24.playground.networksecurity.rsa.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RSAServiceTest {

    private RSAKeyPairResponse keyPair;

    @BeforeAll
    void setUp() {
        keyPair = RSAService.generateKeyPair();
    }

    @Test
    void generateKeyPair_returnsValidKeys() {
        assertThat(keyPair.publicKey()).isNotNull();
        assertThat(keyPair.privateKey()).isNotNull();
        assertThat(keyPair.publicKey().modulus()).isNotBlank();
        assertThat(keyPair.publicKey().exponent()).isEqualTo("65537");
    }

    @Test
    void encryptDecrypt_roundTrip() throws CryptoException {
        String message = "Hello, RSA!";
        RSAEncryptRequest encReq = new RSAEncryptRequest(message, keyPair.publicKey());
        String ciphertext = RSAService.encrypt(encReq);

        assertThat(ciphertext).isNotBlank();
        assertThat(ciphertext).isNotEqualTo(message);

        RSADecryptRequest decReq = new RSADecryptRequest(ciphertext, keyPair.privateKey());
        String plaintext = RSAService.decrypt(decReq);

        assertThat(plaintext).isEqualTo(message);
    }

    @Test
    void signVerify_validSignature() throws CryptoException {
        String message = "Sign this message";
        RSASignRequest signReq = new RSASignRequest(message, keyPair.privateKey());
        String signature = RSAService.sign(signReq);

        assertThat(signature).isNotBlank();

        RSAVerifyRequest verifyReq = new RSAVerifyRequest(message, signature, keyPair.publicKey());
        boolean valid = RSAService.verify(verifyReq);

        assertThat(valid).isTrue();
    }

    @Test
    void signVerify_tamperedMessage_fails() throws CryptoException {
        String message = "Original message";
        RSASignRequest signReq = new RSASignRequest(message, keyPair.privateKey());
        String signature = RSAService.sign(signReq);

        RSAVerifyRequest verifyReq = new RSAVerifyRequest("Tampered message", signature, keyPair.publicKey());
        boolean valid = RSAService.verify(verifyReq);

        assertThat(valid).isFalse();
    }

    @Test
    void toPublicKeyDto_andBack_roundTrips() {
        var pk = RSAService.fromPublicKeyDto(keyPair.publicKey());
        var dto = RSAService.toPublicKeyDto(pk);

        assertThat(dto.modulus()).isEqualTo(keyPair.publicKey().modulus());
        assertThat(dto.exponent()).isEqualTo(keyPair.publicKey().exponent());
    }

    @Test
    void toPrivateKeyDto_andBack_roundTrips() {
        var sk = RSAService.fromPrivateKeyDto(keyPair.privateKey());
        var dto = RSAService.toPrivateKeyDto(sk);

        assertThat(dto.prime1()).isEqualTo(keyPair.privateKey().prime1());
        assertThat(dto.prime2()).isEqualTo(keyPair.privateKey().prime2());
        assertThat(dto.exponent()).isEqualTo(keyPair.privateKey().exponent());
    }
}