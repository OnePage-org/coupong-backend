package com.onepage.coupong.infrastructure.auth.service;

import com.onepage.coupong.jpa.user.enums.Logintype;
import com.onepage.coupong.infrastructure.auth.domain.CustomOAuth2User;
import com.onepage.coupong.jpa.user.User;
import com.onepage.coupong.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        /* 클라이언트에 대한 이름을 가져옴 */
        String oauthClientName = userRequest.getClientRegistration().getClientName();

        try {
            /* OAuth2를 통해 로그인 했을 때 받아오는 결과가 궁금하면 밑의 주석을 풀고 로그인
            * 카카오와 네이버 서로 다른 결과를 반환해주기 때문에 어떤것으로 들어왔는지 구분을해서 각각 다르게 처리가 필요함 */
//            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        User user = null;
        String username = null;
        String email = "temp@email.com";

        /* 카카오로 로그인했을 경우 유저명을 받아오는 과정 */
        if (oauthClientName.equals("kakao")) {
            username = "kakao_" + oAuth2User.getAttributes().get("id");
            user = new User(username, email, Logintype.KAKAO);
        }

        /* 네이버로 로그인했을 경우 유저명을 받아오는 과정 + 유저의 이메일 정보 */
        if (oauthClientName.equals("naver")) {
            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
            username = "naver_" + responseMap.get("id").substring(0, 14);
            email = responseMap.get("email");
            user = new User(username, email, Logintype.NAVER);
        }

        /* 새로운 유저 정보를 등록해준다. */
        userRepository.save(user);
        return new CustomOAuth2User(username);
    }
}
