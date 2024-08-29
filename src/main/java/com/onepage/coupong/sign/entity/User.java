package com.onepage.coupong.sign.entity;

import com.onepage.coupong.sign.dto.request.auth.SignUpRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    /* 웹사이트 / 카카오 / 네이버 중 어디서 가입을 했는지에 대한 정보 */
    private String type;
    /* ROLE_USER / ROLE_ADMIN */
    private String role;

    /* 이메일 인증을 통해 회원가입할 때 사용할 생성자
    * 웹페이지에서 회원가입하기 때문에 type은 WEB으로 저장됨 */
    public User(SignUpRequestDto dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
        type = "WEB";
        role = "ROLE_USER";
    }

    /* OAuth를 활용해서 유저 정보를 저장할 때 사용할 생성자
    * type에는 kakao 또는 naver가 들어감 */
    public User(String username, String email, String type) {
        this.username = username;
        password = "tempPassword";
        this.email = email;
        this.type = type;
        role = "ROLE_USER";
    }
}
