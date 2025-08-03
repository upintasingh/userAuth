package com.prac.user.managment.controller;

import com.prac.user.managment.dtos.LoginRequestDTO;
import com.prac.user.managment.dtos.SignupRequestDTO;
import com.prac.user.managment.dtos.UserDTO;
import com.prac.user.managment.models.USer;
import com.prac.user.managment.service.IAuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        USer user = authService.signup(signupRequestDTO.getUsername(), signupRequestDTO.getEmail(), signupRequestDTO.getPassword(), signupRequestDTO.getPhoneNumber());
        return from(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Pair<USer,String> userResponse = authService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        UserDTO userDTO = from(userResponse.a);
        String token = userResponse.b;

        MultiValueMap<String, String> headers = new LinkedMultiValueMap();
        headers.add(HttpHeaders.SET_COOKIE,token);
        return new ResponseEntity<>(userDTO, headers, HttpStatus.OK);
    }

    private UserDTO from(USer user) {
        return UserDTO.builder().id(user.getId()).name(user.getName())
                .email(user.getEmail()).build();
    }
}
