package com.onepage.coupong.sign.dto.response.auth;

import com.onepage.coupong.common.ResponseCode;
import com.onepage.coupong.common.ResponseMessage;
import com.onepage.coupong.sign.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class EmailCertificationResponseDto extends ResponseDto {

    private EmailCertificationResponseDto() {
        super();
    }

    public static ResponseEntity<EmailCertificationResponseDto> success() {
        EmailCertificationResponseDto responseBody = new EmailCertificationResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> sendFailed() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.MAIL_FAILED, ResponseMessage.MAIL_FAILED);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
}
