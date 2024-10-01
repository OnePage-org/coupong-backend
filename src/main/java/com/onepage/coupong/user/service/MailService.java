package com.onepage.coupong.user.service;

import com.onepage.coupong.user.dto.request.CheckEmailCertificationRequestDto;
import com.onepage.coupong.user.dto.request.EmailCertificationRequestDto;
import com.onepage.coupong.user.dto.response.CheckEmailCertificationResponseDto;
import com.onepage.coupong.user.dto.response.EmailCertificationResponseDto;
import org.springframework.http.ResponseEntity;

public interface MailService {

    /* 메일 발송 */
    ResponseEntity<? super EmailCertificationResponseDto> sendMessage(EmailCertificationRequestDto dto) throws Exception;

    /* 인증 번호 검증 */
    ResponseEntity<? super CheckEmailCertificationResponseDto> verifyCode(CheckEmailCertificationRequestDto dto);
}
