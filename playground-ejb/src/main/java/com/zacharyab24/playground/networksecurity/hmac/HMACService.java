package com.zacharyab24.playground.networksecurity.hmac;

import com.zacharyab24.seng2250.crypto.HMAC;
import com.zacharyab24.seng2250.crypto.exceptions.CryptoException;
import com.zacharyab24.playground.networksecurity.hmac.model.HMACRequest;

public class HMACService {

    /**
     * Computes an HMAC-SHA256 for the given key and message.
     *
     * @param request the key and message
     * @return the HMAC as a hex string
     */
    public static String compute(HMACRequest request) throws CryptoException {
        return HMAC.hmac(request.key(), request.message());
    }
}