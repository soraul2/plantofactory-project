package com.plantofactory.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.util.StringUtils; // StringUtils 추가 (토큰 체크용)
import io.jsonwebtoken.security.SignatureException; // SignatureException 추가

import javax.crypto.SecretKey;
import java.util.Date;

@Component//Spring Bean으로 등록
public class JwtTokenProvider {
    //토큰 생성, 유효성 검사 , 정보 추출 로직이 들어감
    //Secret Key , 만료 시간 등의 설정 필요

    //1. application.properties 에서 secret key와 만료 시간을 주입받습니다.
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration-time}")
    private long expirationtime;

    //SecretKey를 Base64 디코딩하여 안전한 SecretKey 객체로 반환
    private SecretKey getSigningKey(){
        //Base64 디코딩하여 Key 객체를 생성합니다.
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


// 2. [핵심 로직] 사용자 ID를 기반으로 JWT 토큰을 생성하는 메서드
    public String generateToken(Long userId) {
        Date now = new Date();
        // 만료 시간 설정: 현재 시간 + 설정된 만료 시간
        Date expiryDate = new Date(now.getTime() + expirationtime);


        return Jwts.builder()
                .setSubject(userId.toString()) // 토큰의 주체 (보통 유저 ID)
                .setIssuedAt(now) // 토큰 발급 시간
                .setExpiration(expiryDate) // 토큰 만료 시간
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 서명 알고리즘 및 Secret Key
                .compact(); // JWT 문자열 생성
    }

// 1. [핵심] 토큰의 유효성을 검사하는 메서드
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        try {
            // Jwts.parser()가 바로 JwtParserBuilder를 반환합니다.
            Jwts.parser() 
                .verifyWith(getSigningKey()) // <--- SecretKey를 사용하여 서명을 검증합니다.
                .build() // JwtParser 객체 생성
                .parseSignedClaims(token); // 토큰 파싱 및 검증
            return true;
        }catch (JwtException ex) { // 모든 JwtException을 처리
        // 토큰 위조, 만료, 형식 오류 등 모든 실패를 여기서 처리
        System.err.println("JWT Validation Failed: " + ex.getMessage()); 
    }
        return false;
    }

    // 2. [ID 추출] getUserIdFromJWT 메서드 수정
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey()) // SecretKey로 서명 검증
                .build()
                .parseSignedClaims(token) // 토큰 파싱
                .getPayload(); // .getBody() 대신 .getPayload() 사용 (최신 방식)

        // Subject에 저장했던 사용자 ID를 Long 타입으로 반환
        return Long.parseLong(claims.getSubject()); 
    }
        

}
