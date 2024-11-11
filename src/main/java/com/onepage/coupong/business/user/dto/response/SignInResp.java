package com.onepage.coupong.business.user.dto.response;

import com.onepage.coupong.jpa.user.enums.UserRole;
import com.onepage.coupong.global.response.ResponseCode;
import com.onepage.coupong.global.response.ResponseMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignInResp {

    /* 로그인이 성공할 경우 token과 만료시간, 권한에 대한 데이터도 넘겨주기 위함 */
    private final String token;
    private final UserRole role;
    private final int expirationTime;

    @Builder
    private SignInResp(String token, UserRole role) {
        this.token = "Bearer " + token;
        this.role = role;

        /* 만료 시간 1시간을 의미
        * 만료 시간에 대해 수정하고 싶으면 JwtProvider.create() -> expiredDate 수정해주면 됨 */
        expirationTime = 3600;
    }

}
