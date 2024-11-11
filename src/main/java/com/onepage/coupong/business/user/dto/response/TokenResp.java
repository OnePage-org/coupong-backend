package com.onepage.coupong.business.user.dto.response;

import com.onepage.coupong.jpa.user.enums.Logintype;
import com.onepage.coupong.jpa.user.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class TokenResp {

    private final String username;
    private final String email;
    private final Logintype logintype;
    private final UserRole role;

    @Builder
    private TokenResp(String username, String email, Logintype logintype, UserRole role) {
        this.username = username;
        this.email = email;
        this.logintype = logintype;
        this.role = role;
    }
}
