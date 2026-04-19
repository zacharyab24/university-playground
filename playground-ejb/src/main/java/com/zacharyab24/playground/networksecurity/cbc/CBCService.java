package com.zacharyab24.playground.networksecurity.cbc;

import com.zacharyab24.seng2250.crypto.CBC;
import com.zacharyab24.seng2250.crypto.exceptions.CryptoException;
import com.zacharyab24.playground.networksecurity.cbc.model.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CBCService {

    /**
     * Generates a new AES key and IV.
     *
     * @param request the key size (128, 192, or 256)
     * @return base64-encoded key and IV
     */
    public static CBCKeyResponse generateKey(CBCKeyRequest request) throws CryptoException {
        int keySize = request.keySize();
        if (keySize != 128 && keySize != 192 && keySize != 256) {
            throw new IllegalArgumentException("Key size must be 128, 192, or 256");
        }
        SecretKey key = CBC.generateKey(keySize);
        IvParameterSpec iv = CBC.generateIV();
        return new CBCKeyResponse(
                Base64.getEncoder().encodeToString(key.getEncoded()),
                Base64.getEncoder().encodeToString(iv.getIV())
        );
    }

    /**
     * Encrypts plaintext using AES-CBC.
     *
     * @param request the plaintext, base64-encoded key, and base64-encoded IV
     * @return the ciphertext as a base64-encoded string
     */
    public static String encrypt(CBCEncryptRequest request) throws CryptoException {
        SecretKey key = decodeKey(request.key());
        IvParameterSpec iv = decodeIV(request.iv());
        return CBC.encrypt(request.plaintext(), iv, key);
    }

    /**
     * Decrypts ciphertext using AES-CBC.
     *
     * @param request the ciphertext, base64-encoded key, and base64-encoded IV
     * @return the decrypted plaintext
     */
    public static String decrypt(CBCDecryptRequest request) throws CryptoException {
        SecretKey key = decodeKey(request.key());
        IvParameterSpec iv = decodeIV(request.iv());
        String decrypted = CBC.decrypt(request.ciphertext(), iv, key);
        // Library uses null-byte padding; strip trailing \0 characters
        int end = decrypted.indexOf('\0');
        return end >= 0 ? decrypted.substring(0, end) : decrypted;
    }

    private static SecretKey decodeKey(String base64Key) {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(keyBytes, "AES");
    }

    private static IvParameterSpec decodeIV(String base64IV) {
        byte[] ivBytes = Base64.getDecoder().decode(base64IV);
        return new IvParameterSpec(ivBytes);
    }
}