package com.prac.user.managment.service;

import com.prac.user.managment.models.USer;
import org.antlr.v4.runtime.misc.Pair;

public interface IAuthService {
    public USer signup(String name, String email, String password, String phoneNumber);
    public Pair<USer, String> login(String email, String password);
    public boolean validateToken(String token, Long userId);
}
