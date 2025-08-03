package com.prac.user.managment.repository;

import com.prac.user.managment.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByTokenAndUserId(String token, Long userId);
}
