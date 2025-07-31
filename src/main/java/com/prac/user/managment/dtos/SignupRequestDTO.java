package com.prac.user.managment.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDTO {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
}
