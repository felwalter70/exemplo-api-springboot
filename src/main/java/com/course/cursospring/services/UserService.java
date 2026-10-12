package com.course.cursospring.services;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.course.cursospring.domain.User;
import com.course.cursospring.domain.enums.ProfileEnum;
import com.course.cursospring.repositories.UserRepository;
import com.course.cursospring.security.UserSpringSecurity;
import com.course.cursospring.services.exceptions.AuthorizationException;
import com.course.cursospring.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    
    public User findById(Long id) {
        UserSpringSecurity userSpringSecurity = authorized();

        if (!Objects.nonNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId()))
            throw new AuthorizationException("Access denied!");

        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(String.format("User not found! Id: %d", id)));
    }

    @Transactional 
    public User create(User obj) {
        // Garante que o id será gerado pelo DB  
        obj.setId(null);

        obj.setPassword(this.passwordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        
        obj = this.userRepository.save(obj);
        return obj;
    }
    
    @Transactional
    public User update(User obj) {
        User newUserObj = this.findById(obj.getId());

        newUserObj.setPassword(obj.getPassword());
        
        return this.userRepository.save(newUserObj);
    }

    public void delete(Long id) {
        this.findById(id);
        try {
            this.userRepository.deleteById(id);
        }
        catch (Exception e) {
            throw new RuntimeException("User delete is prohibited (Related to another model)");
        }
    }
    
    public static UserSpringSecurity authorized() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}
