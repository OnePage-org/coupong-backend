package com.onepage.coupong.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 회원가입시 인증번호를 검증할 때 활용할 데이터
* 인증번호 메일을 보낼 때 생성하게 됨 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Certification {

    @Id
    private String username;

    private String email;
    /* 인증번호 */
    private String certification;

}
