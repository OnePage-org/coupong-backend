package com.onepage.coupong.implementation.user.manager;

import com.onepage.coupong.business.user.dto.request.IdCheckReq;
import com.onepage.coupong.business.user.dto.request.SignUpReq;
import com.onepage.coupong.implementation.user.UserException;
import com.onepage.coupong.implementation.user.enums.UserExceptionType;
import com.onepage.coupong.jpa.user.User;
import com.onepage.coupong.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpManager {

    private final UserRepository userRepository;

    /* BCryptPasswordEncoder() -> 스프링 시큐리티 프레임워크에서 제공하는 클래스 중 하나로 비밀번호를 암호화하는데 사용할 수 있는 메서드를 가진 클래스
     * BCrypt 해싱함수를 사용해서 비밀번호를 인코딩해주는 메서드와 사용자에 의해 제출된 원본 비밀번호와 DB에 저장되어 있는 암호화된 비밀번호의 일치 여부를 확인해주는 matches() 메서드를 제공
     * PasswordEncoder interface를 구현한 클래스이다. */
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean isAvailableId(IdCheckReq idCheckReq) {
        if (userRepository.existsByUsername(idCheckReq.getUsername())) {
            throw new UserException(UserExceptionType.UNAVAILABLE_USERNAME);
        }
        return true;
    }

    public boolean registerUser(SignUpReq signUpReq) {

        /* BCryptPasswordEncoder 클래스의 encode() 메서드를 이용해 클라이언트가 입력한 비밀번호를 암호화시킨 후 넣어준다 */
        User user = User.builder()
                .username(signUpReq.getUsername())
                .password(passwordEncoder.encode(signUpReq.getPassword()))
                .email(signUpReq.getEmail())
                .build();

        userRepository.save(user);
        return true;
    }
}
