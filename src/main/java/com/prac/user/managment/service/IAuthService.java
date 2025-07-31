package com.prac.user.managment.service;

import com.prac.user.managment.models.USer;

public interface IAuthService {
    public USer signup(String name, String email, String password, String phoneNumber);
}
