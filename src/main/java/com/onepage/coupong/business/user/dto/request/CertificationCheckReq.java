package com.onepage.coupong.business.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CertificationCheckReq {

    private String username;
    private String email;
    private String certification;

}
