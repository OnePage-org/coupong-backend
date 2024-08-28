package com.onepage.coupong.sign.service;

import com.onepage.coupong.sign.dto.request.EmailCertificationRequestDto;
import com.onepage.coupong.sign.dto.response.auth.EmailCertificationResponseDto;
import org.springframework.http.ResponseEntity;

public interface MailService {

    /* 메일 발송 */
    ResponseEntity<? super EmailCertificationResponseDto> sendMessage(EmailCertificationRequestDto dto) throws Exception;

}
