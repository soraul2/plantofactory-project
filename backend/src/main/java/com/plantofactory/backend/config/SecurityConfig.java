package com.plantofactory.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


//1.설정파일 등록
@Configuration
public class SecurityConfig {

    @Bean //이 메서드에서 반환하는 객체 (BCryptPasswordEncoder)를 Spring이 관리하게 함
    public PasswordEncoder passwordEncoder() {
        // BCrypt는 현존하는 가장 안전한 암호화 방식 중 하나입니다.
        return new BCryptPasswordEncoder();
    }
}
