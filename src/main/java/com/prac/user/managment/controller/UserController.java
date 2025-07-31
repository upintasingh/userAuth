package com.prac.user.managment.controller;

import com.prac.user.managment.dtos.SignupRequestDTO;
import com.prac.user.managment.dtos.UserDTO;
import com.prac.user.managment.models.USer;
import com.prac.user.managment.service.IAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    final IAuthService authService;

    public UserController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public UserDTO createUser(@RequestBody SignupRequestDTO signupRequestDTO) {
        USer user = authService.signup(signupRequestDTO.getUsername(), signupRequestDTO.getPassword(), signupRequestDTO.getEmail(), signupRequestDTO.getPhoneNumber());
        return from(user);
    }

    private UserDTO from(USer user) {
        return UserDTO.builder().id(user.getId()).name(user.getName())
                .email(user.getEmail()).build();
    }
}
