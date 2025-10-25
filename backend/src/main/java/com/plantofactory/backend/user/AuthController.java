package com.plantofactory.backend.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.plantofactory.backend.security.JwtTokenProvider;

import org.springframework.web.bind.annotation.RequestBody;


import lombok.RequiredArgsConstructor;

@RestController
//React 가 요청하는 '/api/auth' 경로
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider; //2. JWT Provider 주입

    //react의 /api/auth/signup post 요청을 처리
    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequestDto requestDto) {        
        try{
            userService.signUp(requestDto);
            return ResponseEntity.ok("회원가입 성공");
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage()); //이미 사용중인 아이디 입니다.
        }
    }

    //3. [핵심] 로그인 API 구현 (POST /api/auth/signin)
    @PostMapping("signin")
    public ResponseEntity<String> signin(@RequestBody SignInRequestDto requestDto){
        try{
            //4. 인증 로직 실행 (UserService 에서 유저 찾기 및 비밀번호 검증)
            User user = userService.authenticatUser(requestDto.getEmail(), requestDto.getPassword());

            //5. 인증 성공! -> JWT 토큰 생성
            String jwt = tokenProvider.generateToken(user.getId());

            //6. 클라이언트에게 반환
            // (실제로는 JSON 객체 형태로 {"token" : jwt} 를 반환하는게 일반적)
            return ResponseEntity.ok(jwt);
            
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    

}
