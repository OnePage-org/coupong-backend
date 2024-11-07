package com.onepage.coupong.business.user.dto.response;

import com.onepage.coupong.global.response.ResponseCode;
import com.onepage.coupong.global.response.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CheckEmailCertificationResponseDto extends ResponseDto {

    private CheckEmailCertificationResponseDto() {
        super();
    }

    public static ResponseEntity<CheckEmailCertificationResponseDto> success() {
        CheckEmailCertificationResponseDto responseBody = new CheckEmailCertificationResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> certificationFailed() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.CERTIFICATION_FAILED, ResponseMessage.CERTIFICATION_FAILED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
