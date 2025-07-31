package com.prac.user.managment.repository;

import com.prac.user.managment.models.USer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface USerRepository extends JpaRepository<USer, Long> {

     USer save(USer user);

    Optional<USer> findByEmail(String email);
}
