package com.onepage.coupong.sign.service.implement;

import com.onepage.coupong.sign.entity.CustomOAuth2User;
import com.onepage.coupong.sign.entity.User;
import com.onepage.coupong.sign.repository.UserRepository;
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        User user = null;
        String username = null;
        String email = "temp@email.com";

        if (oauthClientName.equals("kakao")) {
            username = "kakao_" + oAuth2User.getAttributes().get("id");
            user = new User(username, email, "kakao");
        }

        if (oauthClientName.equals("naver")) {
            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
            username = "naver_" + responseMap.get("id").substring(0, 14);
            email = responseMap.get("email");
            user = new User(username, email, "naver");
        }

        userRepository.save(user);
        return new CustomOAuth2User(username);
    }
}
