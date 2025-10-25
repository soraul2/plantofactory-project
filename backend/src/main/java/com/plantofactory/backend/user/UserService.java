package com.plantofactory.backend.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
//final 필드에 대한 생성자를 자동으로 생성
@RequiredArgsConstructor 
public class UserService implements UserDetailsService{
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

    public User authenticatUser(String email , String password){
        //1. 이메일로 User 엔티티를 찾습니다.
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        
        //2. 비밀번호 검증 (가장 중요)
        //matches(사용자가 입력한 원본 비밀번호 , DB에 저장된 암호화된 비밀번호)
        if(!passwordEncoder.matches(password, user.getPassword())){
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return user;    
    }

@Override
    public UserDetails loadUserByUsername(String idString) throws UsernameNotFoundException {
        // ID가 문자열로 넘어오므로 Long으로 변환
        Long id = Long.parseLong(idString); 
        
        // 1. userRepository.findById(id)는 Optional<User>를 반환합니다.
        // 2. orElseThrow()를 통해 User 객체를 얻거나 예외를 던집니다.
        // 3. User 클래스는 UserDetails를 구현했으므로, 반환 시 UserDetails 타입으로 간주됩니다.
        return userRepository.findById(id) // <--- 여기서 User 객체를 반환합니다.
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + idString));
    }




}
