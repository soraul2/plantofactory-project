package com.plantofactory.backend.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {

    //1. react가 보낸 JSON의 key name과 정확하게 일치해야 한다.
    private String name;
    private String email;
    private String password;

}
