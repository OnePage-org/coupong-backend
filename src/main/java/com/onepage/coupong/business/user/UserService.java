package com.onepage.coupong.business.user;

import com.onepage.coupong.business.user.dto.request.IdCheckReq;
import com.onepage.coupong.business.user.dto.request.SignUpReq;
import com.onepage.coupong.business.user.dto.request.SignInReq;
import com.onepage.coupong.business.user.dto.response.ResponseDto;
import com.onepage.coupong.business.user.dto.response.SignInResp;
import com.onepage.coupong.business.user.dto.response.TokenResponseDto;
import com.onepage.coupong.implementation.user.UserException;
import com.onepage.coupong.implementation.user.enums.UserExceptionType;
import com.onepage.coupong.implementation.user.manager.SignInManager;
import com.onepage.coupong.implementation.user.manager.SignUpManager;
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
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final SignUpManager signUpManager;
    private final SignInManager signInManager;

    private final UserRepository userRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;

    /* 아이디 중복 검사 확인 */
    @Override
    public boolean isAvailableId(IdCheckReq idCheckReq) {
        return signUpManager.isAvailableId(idCheckReq);
    }

    /* 회원가입
     * 아이디 중복 검사, 비밀번호 검증, 인증번호 검증 등 백엔드에서도 처리를 해줘야 할것 같은데 회원가입 서비스에서 해줘야하는지 의문
     * 일단 현재는 등록하는 기능만 넣어놨음
     * 인증번호 검사 버튼과 API를 추가 구현해놔야 할듯함 따로 구현한다면 certification 정보는 Request에 필요없음 */
    @Override
    public boolean signUp(SignUpReq signUpReq) {
        return signUpManager.registerUser(signUpReq);
    }

    /* 로그인 */
    @Override
    public SignInResp signIn(SignInReq signInReq) {
        return signInManager.signIn(signInReq);
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
            User user = userRepository.findByUsername(getUsername)
                    .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
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
            User user = userRepository.findByUsername(getUsername)
                    .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

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
