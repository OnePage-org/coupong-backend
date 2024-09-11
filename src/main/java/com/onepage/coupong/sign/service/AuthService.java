package com.onepage.coupong.sign.service;

import com.onepage.coupong.sign.dto.request.auth.IdCheckRequestDto;
import com.onepage.coupong.sign.dto.request.auth.SignInRequestDto;
import com.onepage.coupong.sign.dto.request.auth.SignUpRequestDto;
import com.onepage.coupong.sign.dto.response.auth.IdCheckResponseDto;
import com.onepage.coupong.sign.dto.response.auth.SignInResponseDto;
import com.onepage.coupong.sign.dto.response.auth.SignUpResponseDto;
import com.onepage.coupong.sign.dto.response.auth.TokenResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    /* ? super -> 상속받은 부모(ResponseDto)까지 모두 가져온다. */

    /* 아이디 중복 검사 서비스 */
    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);

    /* 회원가입 서비스*/
    ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto);

    /* 로그인 서비스 */
    ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto);

    /* 요청 헤더로부터 받은 Authorization 복호화 후 유저 정보 반환 서비스 */
    ResponseEntity<? super TokenResponseDto> tokenDecryption(String token);
}
