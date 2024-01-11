package com.lbq.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

/**
 * jwt工具类
 *
 * @Author: lbq
 * @Date: 2024/1/11
 * @Version: 1.0
 */
public class JwtUtils {
    private static final long EXPIRE_TIME = 10 * 1000;
    private static final String SECRET_KEY = "sasjhduedhbsh!2";
    private static final String CLAIMS_KEY = "key";

    /**
     * 生成token
     *
     * @param claims
     * @return
     */
    public static String sign(Map<String, Object> claims) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create().withClaim(CLAIMS_KEY, claims).withExpiresAt(date).sign(algorithm);
    }

    /**
     * 验证token
     *
     * @param token
     * @return
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取token数据
     *
     * @param token
     * @return
     */
    public static Map<String, Object> decode(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            Claim claim = decodedJWT.getClaim(CLAIMS_KEY);
            return claim.asMap();
        } catch (Exception e) {
            return null;
        }
    }
}
