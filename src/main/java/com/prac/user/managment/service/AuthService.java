package com.prac.user.managment.service;

import com.prac.user.managment.Exception.UserAlreadyExistException;
import com.prac.user.managment.models.USer;
import com.prac.user.managment.repository.USerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    final USerRepository uSerRepository;

    public AuthService(USerRepository uSerRepository) {
        this.uSerRepository = uSerRepository;
    }

    @Override
    public USer signup(String name, String email, String password, String phoneNumber) {
        //check user is exist or not
        Optional<USer> user =  uSerRepository.findByEmail(email);
        if(user.isPresent()){
            throw new UserAlreadyExistException("user is already present");
        }

        USer userRequest = new USer();
        userRequest.setName(name);
        userRequest.setEmail(email);
        userRequest.setPassword(password);
        userRequest.setPhoneNumber(phoneNumber);
        return uSerRepository.save(userRequest);
    }
}
