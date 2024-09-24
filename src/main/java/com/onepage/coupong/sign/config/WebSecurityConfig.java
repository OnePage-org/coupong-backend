package com.onepage.coupong.sign.config;

import com.onepage.coupong.sign.filter.JwtAuthenticationFilter;
import com.onepage.coupong.sign.service.implement.OAuth2UserServiceImpl;
import com.onepage.coupong.sign.handler.OAuth2SuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.List;

@Configurable
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2UserServiceImpl oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                /* Session을 사용하지 않겠음을 명시 */
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("api/v1/auth/**", "/oauth2/**").permitAll()
                        .requestMatchers("/").hasRole("USER")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        /* 이메일을 보내기 위한 API 허용 */
                        .requestMatchers("/api/v1/mail/**").permitAll()
                        .requestMatchers("/chat/**").permitAll()
                        .requestMatchers("/api/v1/filtering").permitAll()
                        //.requestMatchers("/api/v1/coupon-event/list").permitAll()
                        .anyRequest().authenticated()
                )
                /* OAuth2 관련 */
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/oauth2"))
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return httpSecurity.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        /* 모든 Header, Method, Origin에 대해 허용해주겠다. */
        configuration.setAllowedOrigins(List.of("https://chatstomp1.netlify.app", "http://localhost:3000"));
//        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        /* 모든 패턴에 대해 */
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

            response.setContentType("application/json");
            /* 권한 없음 */
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            /* {"code" : "NP", "message" : "No Permission."}
            * 의 형식으로 응답이 됨 */
            response.getWriter().write("{\"code\": \"NP\", \"message\": \"No Permission.\"}");
        }
    }
}
