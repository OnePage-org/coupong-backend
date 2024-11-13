package com.onepage.coupong.presentation.user;

import com.onepage.coupong.business.user.dto.request.CertificationCheckReq;
import com.onepage.coupong.business.user.dto.request.EmailCertificationReq;

public interface MailUseCase {

    /* 메일 발송 */
    boolean sendMessage(EmailCertificationReq emailCertificationReq);

    /* 인증 번호 검증 */
    boolean isAvailableCertification(CertificationCheckReq certificationCheckReq);
}
