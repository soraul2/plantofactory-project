package com.plantofactory.backend.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

@RestController
//React 가 요청하는 '/api/auth' 경로
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

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

}
