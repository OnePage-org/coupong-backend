package com.onepage.coupong.business.user.dto.response;

import com.onepage.coupong.global.response.ResponseCode;
import com.onepage.coupong.global.response.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/* 기본적으로 SUCCESS(성공), DATABASE_ERROR(DB 오류), VALIDATION_FAILED(클라이언트 요구 불충족)의 경우는
* 모든 기능에 대해 공통적으로 발생하는 경우이므로 최상위 부모로써 ResponseDto를 생성하고 새로운 기능에 대해서는
* ResponseDto를 상속받고 해당 기능에서만 발생하는 경우에 대해서는 추가적으로 작성해준다. */
@Getter
@AllArgsConstructor
public class ResponseDto {

    /* 요청에 대한 응답을 보낼 때 사용할 code와 message */
    private String code;
    private String message;

    /* 기본 생성자는 성공으로 설정해둠 -> 성공 외의 경우에는 common/ResponseCode | ResponseMessage 에 지정한 정보를 직접 지정해주면 된다. */
    public ResponseDto() {
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> validationFailed() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
