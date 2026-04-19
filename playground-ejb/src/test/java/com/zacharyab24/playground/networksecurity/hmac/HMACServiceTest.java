package com.zacharyab24.playground.networksecurity.hmac;

import com.zacharyab24.seng2250.crypto.exceptions.CryptoException;
import com.zacharyab24.playground.networksecurity.hmac.model.HMACRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HMACServiceTest {

    @Test
    void compute_returnsHexString() throws CryptoException {
        HMACRequest request = new HMACRequest("secret", "Hello, World!");

        String hmac = HMACService.compute(request);

        assertThat(hmac).isNotBlank();
        assertThat(hmac).matches("[0-9a-f]+"); // hex string
    }

    @Test
    void compute_sameInputProducesSameOutput() throws CryptoException {
        HMACRequest request = new HMACRequest("key", "message");

        String first = HMACService.compute(request);
        String second = HMACService.compute(request);

        assertThat(first).isEqualTo(second);
    }

    @Test
    void compute_differentKeyProducesDifferentHmac() throws CryptoException {
        String hmac1 = HMACService.compute(new HMACRequest("key1", "message"));
        String hmac2 = HMACService.compute(new HMACRequest("key2", "message"));

        assertThat(hmac1).isNotEqualTo(hmac2);
    }

    @Test
    void compute_differentMessageProducesDifferentHmac() throws CryptoException {
        String hmac1 = HMACService.compute(new HMACRequest("key", "message1"));
        String hmac2 = HMACService.compute(new HMACRequest("key", "message2"));

        assertThat(hmac1).isNotEqualTo(hmac2);
    }
}