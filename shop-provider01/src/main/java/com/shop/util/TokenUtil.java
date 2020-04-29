package com.shop.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shop.bean.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {
    private static final long EXPIRE_TIME = 12 * 60 * 60 * 1000;
    private static final String TOKEN_SECRET = "myTokensa45casv123dsvc";

    public static String sign(Integer id, String name) {
        try {
            // 设置过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HMAC256");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withClaim("id", id)
                    .withClaim("name", name)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verify(String token) throws JWTVerificationException {
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
