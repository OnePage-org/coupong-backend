package com.onepage.coupong.sign.controller;

import com.onepage.coupong.sign.dto.request.auth.CheckEmailCertificationRequestDto;
import com.onepage.coupong.sign.dto.request.auth.EmailCertificationRequestDto;
import com.onepage.coupong.sign.dto.response.auth.CheckEmailCertificationResponseDto;
import com.onepage.coupong.sign.dto.response.auth.EmailCertificationResponseDto;
import com.onepage.coupong.sign.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class    MailController {

    private final MailService mailService;

    /* 메일 전송 API */
    @PostMapping("/sendMail")
    ResponseEntity<? super EmailCertificationResponseDto> sendMail(
            @RequestBody @Valid EmailCertificationRequestDto requestBody
    ) throws Exception {
        ResponseEntity<? super EmailCertificationResponseDto> response = mailService.sendMessage(requestBody);
        return response;
    }

    /* 인증번호 검증 API */
    @PostMapping("/checkCertification")
    ResponseEntity<? super CheckEmailCertificationResponseDto> checkCertification(
            @RequestBody @Valid CheckEmailCertificationRequestDto requestBody
            ) {
        ResponseEntity<? super CheckEmailCertificationResponseDto> response = mailService.verifyCode(requestBody);
        return response;
    }
}
