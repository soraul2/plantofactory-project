package com.plantofactory.backend.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

//jpaRepository를 상속받아 간단하게 select 문을 사용하지 않고도 조회 할 수 있다.
public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);
}
