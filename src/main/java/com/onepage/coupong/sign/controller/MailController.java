package com.onepage.coupong.sign.controller;

import com.onepage.coupong.sign.dto.request.EmailCertificationRequestDto;
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
public class MailController {

    private final MailService mailService;

    @PostMapping("/sendMail")
    ResponseEntity<? super EmailCertificationResponseDto> sendMail(
            @RequestBody @Valid EmailCertificationRequestDto requestBody
            ) throws Exception{
        ResponseEntity<? super EmailCertificationResponseDto> response = mailService.sendMessage(requestBody);
        return response;
    }

}
