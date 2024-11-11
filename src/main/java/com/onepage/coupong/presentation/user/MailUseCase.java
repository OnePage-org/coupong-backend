package com.onepage.coupong.presentation.user;

import com.onepage.coupong.business.user.dto.request.CheckEmailCertificationRequestDto;
import com.onepage.coupong.business.user.dto.request.EmailCertificationReq;
import com.onepage.coupong.business.user.dto.response.CheckEmailCertificationResponseDto;
import com.onepage.coupong.business.user.dto.response.EmailCertificationResponseDto;
import org.springframework.http.ResponseEntity;

public interface MailUseCase {

    /* 메일 발송 */
    boolean sendMessage(EmailCertificationReq emailCertificationReq);

    /* 인증 번호 검증 */
    ResponseEntity<? super CheckEmailCertificationResponseDto> verifyCode(CheckEmailCertificationRequestDto dto);
}
