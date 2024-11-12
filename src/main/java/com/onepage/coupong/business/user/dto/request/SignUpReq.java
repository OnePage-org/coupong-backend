package com.onepage.coupong.business.user.dto.request;

import lombok.Getter;

@Getter
public class SignUpReq {

    private String username;
    private String password;
    private String passwordCheck;
    private String email;
    private String certification;
}
