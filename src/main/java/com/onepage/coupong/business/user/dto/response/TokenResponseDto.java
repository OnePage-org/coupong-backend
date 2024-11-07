package com.onepage.coupong.business.user.dto.response;

import com.onepage.coupong.jpa.user.enums.Logintype;
import com.onepage.coupong.jpa.user.enums.UserRole;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class TokenResponseDto extends ResponseDto {

    private String username;
    private String email;
    private Logintype logintype;
    private UserRole role;

    private TokenResponseDto(String username, String email, Logintype logintype, UserRole role) {
        super();
        this.username = username;
        this.email = email;
        this.logintype = logintype;
        this.role = role;
    }

    public static ResponseEntity<TokenResponseDto> success(String username, String email, Logintype logintype, UserRole role) {
        TokenResponseDto responseBody = new TokenResponseDto(username, email, logintype, role);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
