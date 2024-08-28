package com.onepage.coupong.sign.service;

import com.onepage.coupong.sign.dto.request.IdCheckRequestDto;
import com.onepage.coupong.sign.dto.response.auth.IdCheckResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    /* 상속받은 부모의 경우 모두 가져온다. */
    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);
}
