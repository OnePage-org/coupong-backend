package com.onepage.coupong.sign.dto.response.auth;

import com.onepage.coupong.entity.enums.UserRole;
import com.onepage.coupong.sign.common.ResponseCode;
import com.onepage.coupong.sign.common.ResponseMessage;
import com.onepage.coupong.sign.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignInResponseDto extends ResponseDto {

    /* 로그인이 성공할 경우 token과 만료시간, 권한에 대한 데이터도 넘겨주기 위함 */
    private String token;
    private int expirationTime;

    private UserRole role;

    private SignInResponseDto(String token, UserRole role) {
        super();
        this.token = "Bearer " + token;
        /* 만료 시간 1시간을 의미
        * 만료 시간에 대해 수정하고 싶으면 JwtProvider.create() -> expiredDate 수정해주면 됨 */
        expirationTime = 3600;
        this.role = role;
    }

    public static ResponseEntity<SignInResponseDto> success(String token, UserRole role) {
        SignInResponseDto responseBody = new SignInResponseDto(token, role);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> signInFailed() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.SIGN_IN_FAILED, ResponseMessage.SIGN_IN_FAILED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
