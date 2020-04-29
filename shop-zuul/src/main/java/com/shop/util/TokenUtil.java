package com.shop.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenUtil {
    private static final String TOKEN_SECRET = "myTokensa45casv123dsvc";

    public static boolean verify(String token) {
        if (token == null)
            return false;
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }

    public static Integer getId(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asInt();
        } catch (JWTDecodeException e){
            e.printStackTrace();
            return null;
        }
    }
}
