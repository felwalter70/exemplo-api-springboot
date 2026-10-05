package com.course.cursospring.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.course.cursospring.domain.User;
import com.course.cursospring.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class UserService {
    private final UserRepository userRepository;
    
    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException(String.format("User not found! Id: %", id)));
    }

    @Transactional 
    public User create(User obj) {
        // Garante que o id será gerado pelo DB  
        obj.setId(null);

        obj = this.userRepository.save(obj);
        return obj;
    }
    
    @Transactional
    public User update(User obj) {
        User newUserObj = this.findById(obj.getId());
        newUserObj.setPassword(newUserObj.getPassword());
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
