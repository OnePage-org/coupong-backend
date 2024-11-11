package com.onepage.coupong.presentation.user;

import com.onepage.coupong.business.user.dto.request.CertificationCheckReq;
import com.onepage.coupong.business.user.dto.request.EmailCertificationReq;
import com.onepage.coupong.global.presentation.CommonResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.onepage.coupong.global.presentation.CommonResponseEntity.success;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailUseCase mailUseCase;

    /* 메일 전송 API */
    @PostMapping("/sendMail")
    public CommonResponseEntity<Boolean> sendMail(@RequestBody EmailCertificationReq emailCertificationReq) {
        return success(mailUseCase.sendMessage(emailCertificationReq));
    }

    /* 인증번호 검증 API */
    @PostMapping("/checkCertification")
    public CommonResponseEntity<Boolean> checkCertification(@RequestBody CertificationCheckReq certificationCheckReq) {
        return success(mailUseCase.isAvailableCertification(certificationCheckReq));
    }
}
