package com.prac.user.managment.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenReq {
    String token;
    Long userId;
}
