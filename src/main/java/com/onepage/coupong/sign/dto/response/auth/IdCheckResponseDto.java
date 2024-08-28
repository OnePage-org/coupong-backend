package com.onepage.coupong.sign.dto.response.auth;

import com.onepage.coupong.common.ResponseCode;
import com.onepage.coupong.common.ResponseMessage;
import com.onepage.coupong.sign.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class IdCheckResponseDto extends ResponseDto {

    /* 기본 생성자를 부모 생성자 내용을 그대로 가져온다.
    * 즉 기본 생성자의 code와 message는 모두 SUCCESS인 상태이다. */
    private IdCheckResponseDto() {
        super();
    }

    public static ResponseEntity<IdCheckResponseDto> success() {
        IdCheckResponseDto responseBody = new IdCheckResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    /* 아이디 중복이 일어났을 경우 보내주는 응답 */
    public static ResponseEntity<ResponseDto> duplicated() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.DUPLICATED_ID, ResponseMessage.DUPLICATED_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
