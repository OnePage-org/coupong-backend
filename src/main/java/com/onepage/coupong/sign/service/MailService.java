package com.onepage.coupong.sign.service;

import com.onepage.coupong.sign.dto.request.CheckEmailCertificationRequestDto;
import com.onepage.coupong.sign.dto.request.EmailCertificationRequestDto;
import com.onepage.coupong.sign.dto.response.auth.CheckEmailCertificationResponseDto;
import com.onepage.coupong.sign.dto.response.auth.EmailCertificationResponseDto;
import org.springframework.http.ResponseEntity;

public interface MailService {

    /* 메일 발송 */
    ResponseEntity<? super EmailCertificationResponseDto> sendMessage(EmailCertificationRequestDto dto) throws Exception;

    /* 인증 번호 검증 */
    ResponseEntity<? super CheckEmailCertificationResponseDto> verifyCode(CheckEmailCertificationRequestDto dto);
}
