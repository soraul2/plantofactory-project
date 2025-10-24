package com.plantofactory.backend.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
//final 필드에 대한 생성자를 자동으로 생성
@RequiredArgsConstructor 
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signUp(SignUpRequestDto requestDto){
        
        //1. 이메일 중복 확인
        if(userRepository.findByEmail(requestDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 사용중인 이메일 입니다.");
        }

        //2. 비밀번호 암호화 (가장 중요)
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        
        //3. DTO를 Entity로 변환 (암호화된 비밀번호 사용)
        User newUser = new User(
            requestDto.getName(),
            encodedPassword,
            requestDto.getEmail()
        );

        return userRepository.save(newUser);
    }
}
