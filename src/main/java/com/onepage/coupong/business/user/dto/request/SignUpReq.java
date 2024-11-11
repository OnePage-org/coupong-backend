package com.onepage.coupong.business.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpReq {

    private String username;
    private String password;
    private String email;
    private String certification;
}
