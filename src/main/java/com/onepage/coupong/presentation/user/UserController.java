package com.onepage.coupong.presentation.user;

import com.onepage.coupong.business.user.dto.request.IdCheckReq;
import com.onepage.coupong.business.user.dto.request.SignInReq;
import com.onepage.coupong.business.user.dto.request.SignUpReq;
import com.onepage.coupong.business.user.dto.response.SignInResp;
import com.onepage.coupong.business.user.dto.response.TokenResp;
import com.onepage.coupong.global.presentation.CommonResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.onepage.coupong.global.presentation.CommonResponseEntity.success;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    /* ID 중복 검사 요청 API */
    @PostMapping("/idCheck")
    public CommonResponseEntity<Boolean> isAvailableId(@RequestBody IdCheckReq idCheckReq) {
        return success(userUseCase.isAvailableId(idCheckReq));
    }

    /* 회원가입 요청 API */
    @PostMapping("/signUp")
    public CommonResponseEntity<Boolean> signUp(@RequestBody SignUpReq signUpReq) {
        return success(userUseCase.signUp(signUpReq));
    }

    /* 로그인 요청 API */
    @PostMapping("/signIn")
    public CommonResponseEntity<SignInResp> signIn(@RequestBody SignInReq signInReq) {
        return success(userUseCase.signIn(signInReq));
    }

    /* 요청 헤더로부터 받은 Authorization 복호화 후 유저 정보 반환 API */
    @GetMapping("/tokenDecryption")
    public CommonResponseEntity<TokenResp> tokenDecryption(@RequestHeader("Authorization") String token) {
        return success(userUseCase.tokenDecryption(token));
    }
}
