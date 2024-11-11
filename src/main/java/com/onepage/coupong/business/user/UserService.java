package com.onepage.coupong.business.user;

import com.onepage.coupong.business.user.dto.request.IdCheckReq;
import com.onepage.coupong.business.user.dto.request.SignInReq;
import com.onepage.coupong.business.user.dto.request.SignUpReq;
import com.onepage.coupong.business.user.dto.response.SignInResp;
import com.onepage.coupong.business.user.dto.response.TokenResp;
import com.onepage.coupong.implementation.user.manager.SignInManager;
import com.onepage.coupong.implementation.user.manager.SignUpManager;
import com.onepage.coupong.infrastructure.auth.manager.TokenManager;
import com.onepage.coupong.jpa.user.User;
import com.onepage.coupong.presentation.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final SignUpManager signUpManager;
    private final SignInManager signInManager;
    private final TokenManager tokenManager;

    /* 아이디 중복 검사 확인 */
    @Override
    public boolean isAvailableId(IdCheckReq idCheckReq) {
        return signUpManager.isAvailableId(idCheckReq);
    }

    /* 회원가입
     * 아이디 중복 검사, 비밀번호 검증, 인증번호 검증 등 프론트말고도 백엔드에서도 처리를 해줘야 할것 같은데 회원가입 서비스에서 해줘야하는지 의문
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
    public TokenResp tokenDecryption(String token) {
        User user = tokenManager.tokenToUser(token);

        return TokenResp.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .logintype(user.getType())
                .build();
    }
}
