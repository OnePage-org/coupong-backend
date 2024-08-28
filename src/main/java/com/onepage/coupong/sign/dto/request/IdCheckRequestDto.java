package com.onepage.coupong.sign.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IdCheckRequestDto {

    /* 필수값이기 때문에 @NotBlank 어노테이션 사용 */
    @NotBlank
    private String username;
}
