package com.onepage.coupong.business.user;

import com.onepage.coupong.business.user.dto.IdCheckDto;
import com.onepage.coupong.business.user.dto.SignUpDto;
import com.onepage.coupong.business.user.dto.request.SignInRequestDto;
import com.onepage.coupong.business.user.dto.response.ResponseDto;
import com.onepage.coupong.business.user.dto.response.SignInResponseDto;
import com.onepage.coupong.business.user.dto.response.SignUpResponseDto;
import com.onepage.coupong.business.user.dto.response.TokenResponseDto;
import com.onepage.coupong.implementation.user.manager.SignUpManager;
import com.onepage.coupong.infrastructure.auth.provider.JwtProvider;
import com.onepage.coupong.infrastructure.mail.CertificationRepository;
import com.onepage.coupong.jpa.user.User;
import com.onepage.coupong.jpa.user.enums.Logintype;
import com.onepage.coupong.jpa.user.enums.UserRole;
import com.onepage.coupong.persistence.user.UserRepository;
import com.onepage.coupong.presentation.user.UserUseCase;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final SignUpManager signUpManager;

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /* 아이디 중복 검사 확인 */
    @Override
    public boolean isAvailableId(IdCheckDto idCheckDto) {
        return signUpManager.isAvailableId(idCheckDto);
    }

    /* 회원가입 서비스
     * 아이디 중복 검사, 비밀번호 검증, 인증번호 검증 등 백엔드에서도 처리를 해줘야 할것 같은데 회원가입 서비스에서 해줘야하는지 의문
     * 일단 현재는 등록하는 기능만 넣어놨음
     * 인증번호 검사 버튼과 API를 추가 구현해놔야 할듯함 따로 구현한다면 certification 정보는 Request에 필요없음 */
    @Override
    public boolean signUp(SignUpDto signUpDto) {
        return signUpManager.registerUser(signUpDto);
    }

    /* 로그인 서비스 */
    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {

        /* 로그인이 성공적으로 되면 token과 권한에 대한 정보를 생성해서 보내줘야함. */
        String token = null;
        UserRole role;

        try {
            /* 로그인 요청에 입력한 username이 DB에 존재하지 않으면 로그인 실패 에러를 보내준다. */
            String username = dto.getUsername();
            User user = userRepository.findUserByUsername(username);
            if (user == null) {
                return SignInResponseDto.signInFailed();
            }

            /* matches() 메서드를 통해 요청으로부터 받은 원본 비밀번호와 DB에 저장된 암호화된 비밀번호를 매칭한 결과를 이용해
             * 비밀번호가 다르다면 로그인 실패 에러를 보내준다. */
            String password = dto.getPassword();
            String encodedPassword = user.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if (!isMatched) {
                return SignInResponseDto.signInFailed();
            }

            /* 로그인이 성공한 경우 토큰 생성 */
            token = jwtProvider.create(username);

            role = user.getRole();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        /* 성공 코드, 메시지와 토큰을 함께 보내준다. */
        return SignInResponseDto.success(token, role);
    }

    @Override
    public ResponseEntity<? super TokenResponseDto> tokenDecryption(String token) {

        String username = "";
        String email = "";
        Logintype logintype = null;
        UserRole role = null;

        try {
            boolean isBearer = token.startsWith("Bearer ");
            if (!isBearer) {
                return ResponseDto.validationFailed();
            }
            token = token.substring(7);

            /* jjwt 라이브러리와 개인키(secretKey)를 이용해서 signature를 복호화하는 과정으로
             *  setSigngKey()가 개인키를 복호화해준다. */
            Claims claim =
                    Jwts.parserBuilder().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).build().parseClaimsJws(token).getBody();

            String getUsername = claim.getSubject();
            User user = userRepository.findUserByUsername(getUsername);
            if (user == null) {
                return ResponseDto.validationFailed();
            }

            username = user.getUsername();
            email = user.getEmail();
            role = user.getRole();
            logintype = user.getType();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return TokenResponseDto.success(username, email, logintype, role);
    }

    @Override
    public Long tokenDecryptionId(String token) {
        Long userId = null;
        try {
            boolean isBearer = token.startsWith("Bearer ");
            if (!isBearer) {
                return null;
            }
            token = token.substring(7);

            /* jjwt 라이브러리와 개인키(secretKey)를 이용해서 signature를 복호화하는 과정으로
             *  setSigngKey()가 개인키를 복호화해준다. */
            Claims claim =
                    Jwts.parserBuilder().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).build().parseClaimsJws(token).getBody();

            String getUsername = claim.getSubject();
            User user = userRepository.findUserByUsername(getUsername);

            if (user == null) {
                return null;
            }
            userId = user.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return userId;
    }
}
