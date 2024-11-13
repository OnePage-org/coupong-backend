package com.onepage.coupong.implementation.user.manager;

import com.onepage.coupong.business.user.dto.request.SignInReq;
import com.onepage.coupong.business.user.dto.response.SignInResp;
import com.onepage.coupong.implementation.user.UserException;
import com.onepage.coupong.implementation.user.enums.UserExceptionType;
import com.onepage.coupong.infrastructure.auth.provider.JwtProvider;
import com.onepage.coupong.jpa.user.User;
import com.onepage.coupong.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInManager {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SignInResp signIn(SignInReq signInReq) {

        User user = userRepository.findByUsername(signInReq.getUsername())
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        /* matches() 메서드를 통해 요청으로부터 받은 원본 비밀번호와 DB에 저장된 암호화된 비밀번호를 매칭한 결과를 이용해
         * 비밀번호가 다르다면 로그인 실패 에러를 보내준다. */
        boolean isAvailable = passwordEncoder.matches(signInReq.getPassword(), user.getPassword());
        if (!isAvailable) {
            throw new UserException(UserExceptionType.PASSWORD_INCORRECT);
        }

        /* 로그인이 성공한 경우 토큰 생성 */
        String token = jwtProvider.create(user.getUsername());

        /* 성공 코드, 메시지와 토큰을 함께 보내준다. */
        return SignInResp.builder()
                .token(token)
                .role(user.getRole())
                .build();
    }
}
