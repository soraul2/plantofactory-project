package com.plantofactory.backend.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

//DB에 필드와 컬럼 자동 연결 및 JPA 에게 table에 있는 name을 활용하여 JPA에 사용하라고 등록을 얘기한다.
@Entity
//DB에 users 라는 테이블이 생성됨.
@Table(name = "users")
//getter 자동 생성
@Getter
//setter 자동 생성
@Setter
//생성자 자동 생성 this.name = name 과 같이 자동으로 클래스 안에 있는 타입 변수안에 데이터도 매칭시켜주는 역할을 한다.
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String password;
    private String email;

    public User(String name , String password , String email){
        this.name = name;
        this.password = password;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 이 예제에서는 역할을 사용하지 않으므로 빈 목록을 반환합니다.
        // 역할을 추가하는 경우, 여기서 SimpleGrantedAuthority로 매핑해야 합니다.
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        // Spring Security에서는 username을 식별자로 사용합니다.
        // 우리는 이메일을 식별자로 사용할 것이므로 email을 반환합니다.
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않았음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않았음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호)이 만료되지 않았음
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정이 활성화되었음
    }
}
