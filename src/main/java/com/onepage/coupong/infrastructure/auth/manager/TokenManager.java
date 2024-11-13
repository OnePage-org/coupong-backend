package com.onepage.coupong.infrastructure.auth.manager;

import com.onepage.coupong.implementation.user.UserException;
import com.onepage.coupong.implementation.user.enums.UserExceptionType;
import com.onepage.coupong.infrastructure.auth.exception.AuthException;
import com.onepage.coupong.infrastructure.auth.exception.enums.AuthExceptionType;
import com.onepage.coupong.jpa.user.User;
import com.onepage.coupong.persistence.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenManager {

    private final UserRepository userRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;

    public User tokenToUser(String token) {

        boolean isBearer = token.startsWith("Bearer ");
        if (!isBearer) {
            throw new AuthException(AuthExceptionType.UNAVAILABLE_TOKEN);
        }

        token = token.substring(7);

        /* jjwt 라이브러리와 개인키(secretKey)를 이용해서 signature를 복호화하는 과정으로
         * setSigningKey()가 개인키를 복호화해준다. */
        Claims claim =
                Jwts.parserBuilder()
                        .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

        String username = claim.getSubject();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
    }
}
