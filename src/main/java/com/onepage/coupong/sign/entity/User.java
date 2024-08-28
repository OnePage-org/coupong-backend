package com.onepage.coupong.sign.entity;

import com.onepage.coupong.sign.dto.request.SignUpRequestDto;
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

    public User(SignUpRequestDto dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
        type = "WEB";
        role = "ROLE_USER";
    }
}
