package com.onepage.coupong.business.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpDto {

    private String username;
    /* 규칙을 지정하는 어노테이션
    * 밑에 적어놓은 정규 표현식을 잘못 적었는지 오류 뜸 ... 보완 필요
    * 영문, 숫자를 포함한 8 ~ 13자리를 규칙으로 뒀는데 오류 뜨넹 */
//  @Pattern(regexp = "^$(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$")
    private String password;
    @Email
    private String email;
    private String certification;
}
