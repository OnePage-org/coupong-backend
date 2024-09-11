package com.onepage.coupong.sign.controller;

import com.onepage.coupong.sign.dto.request.auth.IdCheckRequestDto;
import com.onepage.coupong.sign.dto.request.auth.SignInRequestDto;
import com.onepage.coupong.sign.dto.request.auth.SignUpRequestDto;
import com.onepage.coupong.sign.dto.response.auth.IdCheckResponseDto;
import com.onepage.coupong.sign.dto.response.auth.SignInResponseDto;
import com.onepage.coupong.sign.dto.response.auth.SignUpResponseDto;
import com.onepage.coupong.sign.dto.response.auth.TokenResponseDto;
import com.onepage.coupong.sign.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /* @@@@@@@@@@@@@@@ ResponseEntity에 대한 설명 필요 @@@@@@@@@@@@@@@@@@@ */

    /* ID 중복 검사 요청 API */
    @PostMapping("/idCheck")
    public ResponseEntity<? super IdCheckResponseDto> idCheck(
            @RequestBody @Valid IdCheckRequestDto requestBody
    ) {
        ResponseEntity<? super IdCheckResponseDto> response = authService.idCheck(requestBody);
        return response;
    }

    /* 회원가입 요청 API */
    @PostMapping("/signUp")
    public ResponseEntity<? super SignUpResponseDto> signUp(
            @RequestBody @Valid SignUpRequestDto requestBody
    ) {
        ResponseEntity<? super SignUpResponseDto> response = authService.signUp(requestBody);
        return response;
    }

    /* 로그인 요청 API */
    @PostMapping("/signIn")
    public ResponseEntity<? super SignInResponseDto> signIn(
            @RequestBody @Valid SignInRequestDto requestBody
    ) {
        ResponseEntity<? super SignInResponseDto> response = authService.signIn(requestBody);
        return response;
    }

    /* 요청 헤더로부터 받은 Authorization 복호화 후 유저 정보 반환 API */
    @GetMapping("/tokenDecryption")
    public ResponseEntity<? super TokenResponseDto> tokenDecryption(
            @RequestHeader("Authorization") String token
    ) {
        ResponseEntity<? super TokenResponseDto> response = authService.tokenDecryption(token);
        return response;
    }
}
