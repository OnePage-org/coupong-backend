package com.onepage.coupong.user.service;

import com.onepage.coupong.user.dto.request.IdCheckRequestDto;
import com.onepage.coupong.user.dto.request.SignInRequestDto;
import com.onepage.coupong.user.dto.request.SignUpRequestDto;
import com.onepage.coupong.user.dto.response.IdCheckResponseDto;
import com.onepage.coupong.user.dto.response.SignInResponseDto;
import com.onepage.coupong.user.dto.response.SignUpResponseDto;
import com.onepage.coupong.user.dto.response.TokenResponseDto;
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

    /* 요청 헤더로부터 받은 JWT 토큰을 복호화 후 유저 ID로 반환 서비스 */
    Long tokenDecryptionId(String token);
}
