package com.onepage.coupong.presentation.user;

import com.onepage.coupong.business.user.dto.request.SignInRequestDto;
import com.onepage.coupong.business.user.dto.request.SignUpRequestDto;
import com.onepage.coupong.business.user.dto.response.SignInResponseDto;
import com.onepage.coupong.business.user.dto.response.SignUpResponseDto;
import com.onepage.coupong.business.user.dto.response.TokenResponseDto;
import com.onepage.coupong.global.presentation.CommonResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.onepage.coupong.global.presentation.CommonResponseEntity.success;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    /* ID 중복 검사 요청 API */
    @PostMapping("/idCheck")
    public CommonResponseEntity<Boolean> duplicateCheckId(@RequestBody String username) {
        return success(userUseCase.duplicateCheckId(username));
    }

    /* 회원가입 요청 API */
    @PostMapping("/signUp")
    public ResponseEntity<? super SignUpResponseDto> signUp(
            @RequestBody @Valid SignUpRequestDto requestBody
    ) {
        return userUseCase.signUp(requestBody);
    }

    /* 로그인 요청 API */
    @PostMapping("/signIn")
    public ResponseEntity<? super SignInResponseDto> signIn(
            @RequestBody @Valid SignInRequestDto requestBody
    ) {
        return userUseCase.signIn(requestBody);
    }

    /* 요청 헤더로부터 받은 Authorization 복호화 후 유저 정보 반환 API */
    @GetMapping("/tokenDecryption")
    public ResponseEntity<? super TokenResponseDto> tokenDecryption(
            @RequestHeader("Authorization") String token
    ) {
        return userUseCase.tokenDecryption(token);
    }
}
