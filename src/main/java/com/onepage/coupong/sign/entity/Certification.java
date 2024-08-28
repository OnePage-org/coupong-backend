package com.onepage.coupong.sign.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 회원가입시 인증번호를 요청하게 되면 log 형식처럼 데이터가 쌓이는 구조로 생각 */

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
