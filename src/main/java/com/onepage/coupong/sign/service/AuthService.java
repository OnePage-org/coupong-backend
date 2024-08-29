package com.onepage.coupong.sign.service;

import com.onepage.coupong.sign.dto.request.auth.IdCheckRequestDto;
import com.onepage.coupong.sign.dto.request.auth.SignUpRequestDto;
import com.onepage.coupong.sign.dto.response.auth.IdCheckResponseDto;
import com.onepage.coupong.sign.dto.response.auth.SignUpResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    /* ? super -> 상속받은 부모(ResponseDto)까지 모두 가져온다. */
    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);

    ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto);
}
