package com.onepage.coupong.infrastructure.auth.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    /* JWT를 만들어주는 메서드 */
    public String create(String username) {

        Date expiredDate = Date.from(Instant.now().plus(2, ChronoUnit.HOURS));
        /* JWT에서 VERIFY SIGNATURE에 해당하는 key값을 만들어준다 */
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        String jwt = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                /* setSubject() -> JWT PAYLOAD에서 "sub"에 해당하는 내용
                 * setIssuedAt() -> JWT PAYLOAD에서 "iat"에 해당하는 내용
                 * setExpiration() -> 만료 시간 */
                .setSubject(username).setIssuedAt(new Date()).setExpiration(expiredDate)
                .compact();

        return jwt;
    }

    /* JWT 검증 메서드 */
    public String validate(String jwt) {

        String subject = null;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            subject = claims.getSubject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return subject;
    }
}
