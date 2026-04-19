package com.zacharyab24.playground.networksecurity.dh;

import com.zacharyab24.seng2250.crypto.DiffieHellman;
import com.zacharyab24.seng2250.crypto.exceptions.CryptoException;
import com.zacharyab24.playground.networksecurity.dh.model.DHKeyPairResponse;
import com.zacharyab24.playground.networksecurity.dh.model.DHSessionRequest;
import com.zacharyab24.playground.networksecurity.dh.model.DHSessionResponse;

import java.math.BigInteger;

public class DHService {

    /**
     * Generates a new Diffie-Hellman key pair.
     *
     * @return the private and public key as decimal strings
     */
    public static DHKeyPairResponse generateKeyPair() {
        BigInteger privateKey = DiffieHellman.genPrivateKey();
        BigInteger publicKey = DiffieHellman.genPublicKey(privateKey);
        return new DHKeyPairResponse(privateKey.toString(), publicKey.toString());
    }

    /**
     * Derives a shared session key from the other party's public key and the local private key.
     *
     * @param request the remote public key and local private key
     * @return the session key and its SHA-256 hash
     */
    public static DHSessionResponse deriveSessionKey(DHSessionRequest request) throws CryptoException {
        BigInteger remotePublic = new BigInteger(request.remotePublicKey());
        BigInteger localPrivate = new BigInteger(request.localPrivateKey());
        BigInteger sessionKey = DiffieHellman.genSessionKey(remotePublic, localPrivate);
        String hashed = DiffieHellman.hashSessionKey(sessionKey);
        return new DHSessionResponse(sessionKey.toString(), hashed);
    }
}