package com.course.cursospring.services;

import java.util.Objects;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.course.cursospring.domain.User;
import com.course.cursospring.repositories.UserRepository;
import com.course.cursospring.security.UserSpringSecurity;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        
        if (Objects.isNull(user))
            throw new UsernameNotFoundException("User of username " + username + " was not found");

        return new UserSpringSecurity(user.getId(), user.getUsername(), user.getPassword(), user.getProfiles());
    }
    
}
