package com.onepage.coupong.sign.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    /* 규칙을 지정하는 어노테이션 */
//    @Pattern(regexp = "^$(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$")
    private String password;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String certification;
}
