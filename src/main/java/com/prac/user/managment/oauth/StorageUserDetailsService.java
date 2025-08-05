package com.prac.user.managment.oauth;

import com.prac.user.managment.models.USer;
import com.prac.user.managment.repository.USerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StorageUserDetailsService implements UserDetailsService {
    final USerRepository userRepository;

    public StorageUserDetailsService(USerRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<USer> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        return new StorageUserDetails(user.get());
    }
}
