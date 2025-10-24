package com.plantofactory.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity // Spring Security를 활성화합니다
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. CORS 설정 (가장 중요! React와 연결하기 위해)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. CSRF 보호 비활성화 (Stateless API는 비활성화해도 괜찮습니다)
            .csrf(csrf -> csrf.disable())
            
            // 3. API 경로별 접근 권한 설정
            .authorizeHttpRequests(authz -> authz
                // '/api/auth/'로 시작하는 모든 경로는 (signup, signin)
                .requestMatchers("/api/auth/**").permitAll() // 모두 허용
                
                // 그 외의 모든 요청은
                .anyRequest().authenticated() // 인증(로그인)된 사용자만 허용
            );

        return http.build();
    }

    // React(5173)에서 오는 요청을 허용하는 CORS 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // React 앱의 주소
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true); // 쿠키/인증 정보를 함께 보낼 수 있도록
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 이 설정 적용
        return source;
    }
}