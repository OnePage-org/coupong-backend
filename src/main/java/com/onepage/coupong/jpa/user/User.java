package com.onepage.coupong.jpa.user;

import com.onepage.coupong.jpa.user.enums.Logintype;
import com.onepage.coupong.jpa.user.enums.UserRole;
import com.onepage.coupong.business.user.dto.SignUpDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String username;

    private String password;

    private String email;

    /* 웹사이트 / 카카오 / 네이버 중 어디서 가입을 했는지에 대한 정보 */
    @Enumerated(EnumType.STRING)
    private Logintype type;

    /* ROLE_USER / ROLE_ADMIN */
    @Enumerated(EnumType.STRING)
    private UserRole role;

    /*
    @OneToMany(mappedBy ="winner_id", cascade = CascadeType.ALL)
    List<CouponEvent> eventList = new ArrayList<>();
     */

    /* 이메일 인증을 통해 회원가입할 때 사용할 생성자
    * 웹 페이지에서 회원가입하기 때문에 type은 WEB으로 저장됨 */
    @Builder
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        type = Logintype.WEB;
        role = UserRole.ROLE_USER;
    }

    /* OAuth를 활용해서 유저 정보를 저장할 때 사용할 생성자
    * type에는 kakao 또는 naver가 들어감 */
    public User(String username, String email, Logintype type) {
        this.username = username;
        password = "tempPassword";
        this.email = email;
        this.type = type;
        role = UserRole.ROLE_USER;
    }
}
