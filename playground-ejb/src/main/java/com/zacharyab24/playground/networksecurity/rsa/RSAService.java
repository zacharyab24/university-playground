package com.zacharyab24.playground.networksecurity.rsa;

import com.zacharyab24.seng2250.crypto.RSA;
import com.zacharyab24.seng2250.crypto.exceptions.CryptoException;
import com.zacharyab24.seng2250.model.KeyPair;
import com.zacharyab24.seng2250.model.PrivateKey;
import com.zacharyab24.seng2250.model.PublicKey;
import com.zacharyab24.playground.networksecurity.rsa.model.*;

import java.math.BigInteger;

public class RSAService {

    /**
     * Generates a new RSA key pair.
     *
     * @return the generated key pair as DTOs
     */
    public static RSAKeyPairResponse generateKeyPair() {
        KeyPair kp = RSA.genRSA();
        return new RSAKeyPairResponse(
                toPublicKeyDto(kp.publicKey()),
                toPrivateKeyDto(kp.privateKey())
        );
    }

    /**
     * Encrypts plaintext with an RSA public key.
     *
     * @param request the plaintext and public key
     * @return the ciphertext as a decimal string
     */
    public static String encrypt(RSAEncryptRequest request) throws CryptoException {
        PublicKey pk = fromPublicKeyDto(request.publicKey());
        return RSA.encryptRSA(request.plaintext(), pk);
    }

    /**
     * Decrypts ciphertext with an RSA private key.
     *
     * @param request the ciphertext and private key
     * @return the decrypted plaintext
     */
    public static String decrypt(RSADecryptRequest request) {
        PrivateKey sk = fromPrivateKeyDto(request.privateKey());
        return RSA.decryptRSA(request.ciphertext(), sk);
    }

    /**
     * Signs a message with an RSA private key.
     *
     * @param request the message and private key
     * @return the signature as a decimal string
     */
    public static String sign(RSASignRequest request) throws CryptoException {
        PrivateKey sk = fromPrivateKeyDto(request.privateKey());
        return RSA.genSignature(request.message(), sk).toString();
    }

    /**
     * Verifies an RSA signature against a message and public key.
     *
     * @param request the message, signature, and public key
     * @return true if the signature is valid
     */
    public static boolean verify(RSAVerifyRequest request) throws CryptoException {
        PublicKey pk = fromPublicKeyDto(request.publicKey());
        BigInteger sig = new BigInteger(request.signature());
        return RSA.verifySignature(request.message(), sig, pk);
    }

    /**
     * Converts a library PublicKey to the API DTO representation.
     *
     * @param pk the library public key
     * @return the public key DTO with BigInteger fields as decimal strings
     */
    static RSAPublicKeyDto toPublicKeyDto(PublicKey pk) {
        return new RSAPublicKeyDto(pk.modulus().toString(), pk.exponent().toString());
    }

    /**
     * Converts a library PrivateKey to the API DTO representation.
     *
     * @param sk the library private key
     * @return the private key DTO with BigInteger fields as decimal strings
     */
    static RSAPrivateKeyDto toPrivateKeyDto(PrivateKey sk) {
        return new RSAPrivateKeyDto(sk.prime1().toString(), sk.prime2().toString(), sk.exponent().toString());
    }

    /**
     * Converts a public key DTO back to the library PublicKey type.
     *
     * @param dto the public key DTO with decimal string fields
     * @return the library PublicKey
     */
    static PublicKey fromPublicKeyDto(RSAPublicKeyDto dto) {
        return new PublicKey(new BigInteger(dto.modulus()), new BigInteger(dto.exponent()));
    }

    /**
     * Converts a private key DTO back to the library PrivateKey type.
     *
     * @param dto the private key DTO with decimal string fields
     * @return the library PrivateKey
     */
    static PrivateKey fromPrivateKeyDto(RSAPrivateKeyDto dto) {
        return new PrivateKey(new BigInteger(dto.prime1()), new BigInteger(dto.prime2()), new BigInteger(dto.exponent()));
    }
}