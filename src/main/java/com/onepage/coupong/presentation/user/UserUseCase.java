package com.onepage.coupong.presentation.user;

import com.onepage.coupong.business.user.dto.IdCheckDto;
import com.onepage.coupong.business.user.dto.request.SignInRequestDto;
import com.onepage.coupong.business.user.dto.SignUpDto;
import com.onepage.coupong.business.user.dto.response.SignInResponseDto;
import com.onepage.coupong.business.user.dto.response.SignUpResponseDto;
import com.onepage.coupong.business.user.dto.response.TokenResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserUseCase {

    /* 아이디 중복 검사 서비스 */
    boolean isAvailableId(IdCheckDto idCheckDto);

    /* 회원가입 서비스*/
    boolean signUp(SignUpDto signUpDto);

    /* 로그인 서비스 */
    ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto);

    /* 요청 헤더로부터 받은 Authorization 복호화 후 유저 정보 반환 서비스 */
    ResponseEntity<? super TokenResponseDto> tokenDecryption(String token);

    /* 요청 헤더로부터 받은 JWT 토큰을 복호화 후 유저 ID로 반환 서비스 */
    Long tokenDecryptionId(String token);
}
