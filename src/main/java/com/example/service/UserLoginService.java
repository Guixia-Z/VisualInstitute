package com.example.service;

import com.example.model.User;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserLoginService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = usersRepository.findUserByEmail(email);
        if (user.isEmpty()) throw new UsernameNotFoundException("Email (username) not found");
        return user.get();
    }

}
