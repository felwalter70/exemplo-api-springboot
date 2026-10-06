package com.course.cursospring.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.course.cursospring.domain.User;
import com.course.cursospring.domain.enums.ProfileEnum;
import com.course.cursospring.repositories.UserRepository;
import com.course.cursospring.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    
    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(String.format("User not found! Id: %d", id)));
    }

    @Transactional 
    public User create(User obj) {
        // Garante que o id será gerado pelo DB  
        obj.setId(null);

        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
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
}
