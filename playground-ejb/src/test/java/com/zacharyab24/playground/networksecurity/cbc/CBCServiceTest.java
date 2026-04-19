package com.zacharyab24.playground.networksecurity.cbc;

import com.zacharyab24.seng2250.crypto.exceptions.CryptoException;
import com.zacharyab24.playground.networksecurity.cbc.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CBCServiceTest {

    private CBCKeyResponse keyResponse;

    @BeforeAll
    void setUp() throws CryptoException {
        keyResponse = CBCService.generateKey(new CBCKeyRequest(256));
    }

    @Test
    void generateKey_returnsBase64KeyAndIV() {
        assertThat(keyResponse.key()).isNotBlank();
        assertThat(keyResponse.iv()).isNotBlank();
    }

    @Test
    void generateKey_invalidSize_throwsIllegalArgument() {
        assertThatThrownBy(() -> CBCService.generateKey(new CBCKeyRequest(512)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("128, 192, or 256");
    }

    @Test
    void encryptDecrypt_roundTrip() throws CryptoException {
        String message = "Hello, AES-CBC!";
        CBCEncryptRequest encReq = new CBCEncryptRequest(message, keyResponse.key(), keyResponse.iv());
        String ciphertext = CBCService.encrypt(encReq);

        assertThat(ciphertext).isNotBlank();
        assertThat(ciphertext).isNotEqualTo(message);

        CBCDecryptRequest decReq = new CBCDecryptRequest(ciphertext, keyResponse.key(), keyResponse.iv());
        String plaintext = CBCService.decrypt(decReq);

        assertThat(plaintext).isEqualTo(message);
    }

    @Test
    void encryptDecrypt_longerMessage() throws CryptoException {
        String message = "This is a longer message that spans multiple AES blocks to test chaining.";
        CBCEncryptRequest encReq = new CBCEncryptRequest(message, keyResponse.key(), keyResponse.iv());
        String ciphertext = CBCService.encrypt(encReq);

        CBCDecryptRequest decReq = new CBCDecryptRequest(ciphertext, keyResponse.key(), keyResponse.iv());
        String plaintext = CBCService.decrypt(decReq);

        assertThat(plaintext).isEqualTo(message);
    }

    @Test
    void generateKey_128bit() throws CryptoException {
        CBCKeyResponse response = CBCService.generateKey(new CBCKeyRequest(128));
        assertThat(response.key()).isNotBlank();
        assertThat(response.iv()).isNotBlank();
    }
}