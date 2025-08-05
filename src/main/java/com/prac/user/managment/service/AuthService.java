package com.prac.user.managment.service;

import com.prac.user.managment.Exception.PasswordMismatchException;
import com.prac.user.managment.Exception.USerNOTFoundException;
import com.prac.user.managment.Exception.UserAlreadyExistException;
import com.prac.user.managment.models.Status;
import com.prac.user.managment.models.USer;
import com.prac.user.managment.models.UserSession;
import com.prac.user.managment.repository.SessionRepo;
import com.prac.user.managment.repository.USerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    final USerRepository uSerRepository;

    final BCryptPasswordEncoder bCryptPasswordEncoder;

    final SessionRepo sessionRepo;

    final SecretKey secretKey;

    public AuthService(USerRepository uSerRepository, BCryptPasswordEncoder bCryptPasswordEncoder, SessionRepo sessionRepo, SecretKey secretKey) {
        this.uSerRepository = uSerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRepo = sessionRepo;
        this.secretKey = secretKey;
    }

    @Override
    public USer signup(String name, String email, String password, String phoneNumber) {
        Optional<USer> user =  uSerRepository.findByEmail(email);
        if(user.isPresent()){
            throw new UserAlreadyExistException("user is already present");
        }

        USer userRequest = new USer();
        userRequest.setName(name);
        userRequest.setEmail(email);
        userRequest.setPassword(bCryptPasswordEncoder.encode(password));
        userRequest.setPhoneNumber(phoneNumber);
        return uSerRepository.save(userRequest);
    }

    @Override
    public Pair<USer, String> login(String email, String password) {
        Optional<USer> user = uSerRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new USerNOTFoundException("email is not present in database. please signup first");
        }
        if (!bCryptPasswordEncoder.matches(password, user.get().getPassword())) {
            throw new PasswordMismatchException("password is not correct");
        }

        /*String message = "{\n" +
                "   \"email\": \"abc@gmail.com\",\n" +
                "   \"roles\": [\n" +
                "      \"instructor\",\n" +
                "      \"ta\"\n" +
                "   ],\n" +
                "   \"expirationDate\": \"2ndApril2026\"\n" +
                "}";
        byte[] content = message.getBytes(StandardCharsets.UTF_8);*/

        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("iss", "myorg");
        claims.put("userId", user.get().getId());
        claims.put("gen", System.currentTimeMillis());
        claims.put("exp", System.currentTimeMillis() + 1000000);
        claims.put("roles", user.get().getRoles());

        /*MacAlgorithm mcMacAlgorithm = Jwts.SIG.HS256;
        SecretKey secretKey = mcMacAlgorithm.key().build();*/
        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        UserSession userSession = new UserSession();
        userSession.setToken(token);
        userSession.setUser(user.get());
        userSession.setStatus(Status.ACTIVE);
        sessionRepo.save(userSession);

        return new Pair<>(user.get(), token);
    }

    @Override
    public boolean validateToken(String token, Long userId) {
        Optional<UserSession> optionalUserSession = sessionRepo.findByTokenAndUserId(token, userId);
        if (optionalUserSession.isEmpty()) {
            return false;
        }

        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();

         Long expiry = (Long) claims.get("exp");
         long currentTime = System.currentTimeMillis();

        System.out.println("tokenExpiry = "+expiry);
        System.out.println("currentTime = "+currentTime);

        if(currentTime > expiry) {
            System.out.println("TOKEN HAS EXPIRED");
            UserSession userSession = optionalUserSession.get();
            userSession.setStatus(Status.INACTIVE);
            sessionRepo.save(userSession);
            return false;
        }


        return true;
    }
}
