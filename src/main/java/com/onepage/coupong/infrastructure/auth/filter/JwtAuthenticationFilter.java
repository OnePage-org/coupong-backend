package com.onepage.coupong.infrastructure.auth.filter;

import com.onepage.coupong.user.domain.User;
import com.onepage.coupong.infrastructure.auth.provider.JwtProvider;
import com.onepage.coupong.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            String token = parseBearerToken(request);

            /* Authorization이 없거나, Bearer 인증 방식이 아닌 경우에 null이 반환되므로
            * 해당 경우에는 다음 필터로 넘긴 뒤에 return을 해준다. */
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            /* 토큰이 유효한 토큰인지 검증하고 토큰에서 유저의 ID를 꺼내온다. */
            String username = jwtProvider.validate(token);
            if (username == null) {
                filterChain.doFilter(request, response);
                return;
            }

            /* 검증까지 완료했을 경우 토큰에서 꺼내온 userId와 UserRepository를 이용해 User의 정보를 가져오는 작업. */
            User user = userRepository.findUserByUsername(username);
            /* ROLE_USER, ROLE_ADMIN */
            String role = String.valueOf(user.getRole());

            /* GrantedAuthority 는 현재 사용자(principal)가 가지고 있는 권한을 의미한다.
            *  ROLE_ADMIN, ROLE_USER 와 같이 ROLE_*의 형태로 사용한다.
            *  GrantedAuthority 객체는 UserDetailsService에 의해 불러올 수 있고
            *  특정 자원에 대한 권한이 있는지를 검사해 접근 허용 여부를 결정한다. */
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));

            /* 토큰이 들어갈 context 객체를 만들어준다. */
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

            UsernamePasswordAuthenticationToken authenticationToken =
                    /* 첫 번째 위치에는 User의 정보, 두 번째 위치는 Password, 그 이후에는 여기서는 권한을 추가적으로 넣어줬다. */
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            /* Authentication 설정 추가 */
            securityContext.setAuthentication(authenticationToken);

            SecurityContextHolder.setContext(securityContext);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /* 다음 필터로 넘어가도록 함 */
        filterChain.doFilter(request, response);

    }

    private String parseBearerToken(HttpServletRequest request) {

        /* Request 로부터 Header에 있는 Authorization 정보를 가져오는 작업 */
        String authorization = request.getHeader("Authorization");

        /* authorization의 존재 여부 확인
        * StringUtils.hasText(String) 메서드는
        * null인지 , 길이가 0보다 큰지, 공백이 아닌 문자열이 하나라도 포함되었는지 한 번에 검증해준다.
        * 즉, null이 아니어야 하고, 길이가 0보다 커야하고, 공백이 아닌 문자가 포함되어 있어야 true가 반환된다. */
        boolean hasAuthorization = StringUtils.hasText(authorization);

        if (!hasAuthorization) {
            return null;
        }

        /* 해당 인증방식이 Bearer 인증 방식인지 검사를 해주기 위해 startsWith()를 활용해 Bearer로 시작하는지 확인하는 작업 */
        boolean isBearer = authorization.startsWith("Bearer ");
        if (!isBearer) {
            return null;
        }

        /* Bearer를 제외한 8번째 부터의 값이 token 값이므로 해당 값만 반환해준다. */
        return authorization.substring(7);
    }
}
