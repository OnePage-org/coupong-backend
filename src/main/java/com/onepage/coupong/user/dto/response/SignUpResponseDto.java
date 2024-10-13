package com.onepage.coupong.user.dto.response;

import com.onepage.coupong.global.response.ResponseCode;
import com.onepage.coupong.global.response.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignUpResponseDto extends ResponseDto {

    private SignUpResponseDto() {
        super();
    }

    public static ResponseEntity<SignUpResponseDto> success() {
        SignUpResponseDto responseDto = new SignUpResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> duplicated() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.DUPLICATED_ID, ResponseMessage.DUPLICATED_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> certificationFailed() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.CERTIFICATION_FAILED, ResponseMessage.CERTIFICATION_FAILED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
