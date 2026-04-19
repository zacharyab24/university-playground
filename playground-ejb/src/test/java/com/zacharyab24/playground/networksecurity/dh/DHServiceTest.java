package com.zacharyab24.playground.networksecurity.dh;

import com.zacharyab24.seng2250.crypto.exceptions.CryptoException;
import com.zacharyab24.playground.networksecurity.dh.model.DHKeyPairResponse;
import com.zacharyab24.playground.networksecurity.dh.model.DHSessionRequest;
import com.zacharyab24.playground.networksecurity.dh.model.DHSessionResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DHServiceTest {

    @Test
    void generateKeyPair_returnsKeys() {
        DHKeyPairResponse response = DHService.generateKeyPair();

        assertThat(response.privateKey()).isNotBlank();
        assertThat(response.publicKey()).isNotBlank();
        assertThat(response.privateKey()).isNotEqualTo(response.publicKey());
    }

    @Test
    void deriveSessionKey_bothSidesMatch() throws CryptoException {
        DHKeyPairResponse alice = DHService.generateKeyPair();
        DHKeyPairResponse bob = DHService.generateKeyPair();

        DHSessionResponse aliceSession = DHService.deriveSessionKey(
                new DHSessionRequest(bob.publicKey(), alice.privateKey()));
        DHSessionResponse bobSession = DHService.deriveSessionKey(
                new DHSessionRequest(alice.publicKey(), bob.privateKey()));

        assertThat(aliceSession.sessionKey()).isEqualTo(bobSession.sessionKey());
        assertThat(aliceSession.hashedSessionKey()).isEqualTo(bobSession.hashedSessionKey());
    }

    @Test
    void deriveSessionKey_returnsHashedKey() throws CryptoException {
        DHKeyPairResponse alice = DHService.generateKeyPair();
        DHKeyPairResponse bob = DHService.generateKeyPair();

        DHSessionResponse session = DHService.deriveSessionKey(
                new DHSessionRequest(bob.publicKey(), alice.privateKey()));

        assertThat(session.sessionKey()).isNotBlank();
        assertThat(session.hashedSessionKey()).isNotBlank();
        assertThat(session.hashedSessionKey()).hasSize(64); // SHA-256 hex is 64 chars
    }

    @Test
    void generateKeyPair_differentEachTime() {
        DHKeyPairResponse first = DHService.generateKeyPair();
        DHKeyPairResponse second = DHService.generateKeyPair();

        assertThat(first.privateKey()).isNotEqualTo(second.privateKey());
    }
}