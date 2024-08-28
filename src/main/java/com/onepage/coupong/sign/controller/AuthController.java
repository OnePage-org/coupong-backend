package com.onepage.coupong.sign.controller;

import com.onepage.coupong.sign.dto.request.IdCheckRequestDto;
import com.onepage.coupong.sign.dto.response.auth.IdCheckResponseDto;
import com.onepage.coupong.sign.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/idCheck")
    public ResponseEntity<? super IdCheckResponseDto> idCheck(
            @RequestBody @Valid IdCheckRequestDto requestBody
            ){
        ResponseEntity<? super IdCheckResponseDto> response = authService.idCheck(requestBody);
        return response;
    }

}
