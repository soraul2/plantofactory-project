package com.plantofactory.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService; // Spring Security 기본 서비스

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) 
                                    throws ServletException, IOException {

        try {
            // 1. 요청 헤더에서 JWT 토큰을 추출합니다.
            String jwt = getJwtFromRequest(request);

            // 2. 토큰이 존재하고 유효한지 검사합니다.
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // 3. 토큰이 유효하면, 토큰에서 사용자 ID를 추출합니다.
                Long userId = tokenProvider.getUserIdFromJWT(jwt);

                // 4. 추출된 ID를 기반으로 데이터베이스에서 사용자 상세 정보를 로드합니다.
                //    (이 로직은 UserDetailsService 구현체인 UserService가 담당합니다.)
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());

                // 5. [핵심] 인증(Authentication) 객체를 생성합니다.
                //    이 객체는 Spring Security가 현재 사용자가 인증되었음을 인식하는 데 사용됩니다.
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. SecurityContextHolder에 인증 정보를 설정합니다.
                //    이로써 현재 요청을 처리하는 동안 사용자는 '인증된' 상태가 됩니다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // JWT 처리 중 발생하는 예외는 로그에 기록하지만, 요청 처리는 계속 진행합니다.
            // 인증이 설정되지 않았으므로, 이후의 Security Filter들이 접근을 거부할 수 있습니다.
            logger.error("Could not set user authentication in security context", ex);
        }

        // 7. 다음 필터 체인으로 요청과 응답을 전달합니다.
        filterChain.doFilter(request, response);
    }

    // HTTP 요청 헤더에서 JWT를 추출하는 헬퍼 메서드
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // "Bearer "로 시작하는지 확인하고, 토큰 부분만 잘라냅니다.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}