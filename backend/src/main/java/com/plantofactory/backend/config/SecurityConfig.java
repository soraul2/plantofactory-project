package com.plantofactory.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Spring Security를 활성화합니다
import org.springframework.security.config.http.SessionCreationPolicy; // 세션 정책 설정을 위해 import
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.plantofactory.backend.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Configuration
@EnableWebSecurity // Spring Security 활성화
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성 (Lombok)
public class SecurityConfig {

    // 1. 순환 참조를 해결하기 위해 SecurityConfig는 더 이상 PasswordEncoder를 직접 생성하지 않습니다.
    //    대신, JwtAuthenticationFilter만 주입받습니다.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CORS (Cross-Origin Resource Sharing) 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // CSRF(Cross-Site Request Forgery) 보호 기능 비활성화
            // JWT 토큰을 사용하는 Stateless 인증 방식에서는 CSRF 공격에 비교적 안전하므로 비활성화합니다.
            .csrf(csrf -> csrf.disable())
            
            // 세션 관리 정책을 STATELESS로 설정
            // 서버가 클라이언트의 상태를 저장하지 않는 '무상태(Stateless)' 방식으로 동작하도록 합니다.
            // 이는 JWT 기반 인증의 핵심입니다.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // HTTP 요청에 대한 인가(Authorization) 규칙 설정
            .authorizeHttpRequests(authz -> authz
                // '/api/auth/'로 시작하는 모든 경로(예: 회원가입, 로그인)는 인증 없이 접근 허용
                .requestMatchers("/api/auth/**").permitAll()
                
                // 그 외의 모든 요청은 반드시 인증(로그인)된 사용자만 접근 가능
                .anyRequest().authenticated()
            );

            // 직접 구현한 JwtAuthenticationFilter를 Spring Security의 기본 필터인
            // UsernamePasswordAuthenticationFilter 앞에 추가합니다.
            // 이렇게 함으로써, 모든 요청이 컨트롤러에 도달하기 전에 JWT 토큰을 검사하게 됩니다.
            http.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // React(3000)에서 오는 요청을 허용하는 CORS 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 요청을 허용할 클라이언트의 주소 (여기서는 React 개발 서버)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        // 허용할 HTTP 메서드 (GET, POST 등)
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
        // 허용할 HTTP 헤더
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true); // 자격 증명(쿠키, 인증 헤더 등)을 포함한 요청 허용
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 이 설정 적용
        return source;
    }
}