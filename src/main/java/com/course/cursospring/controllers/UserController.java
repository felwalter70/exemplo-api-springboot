package com.course.cursospring.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.course.cursospring.domain.User;
import com.course.cursospring.domain.User.CreateUser;
import com.course.cursospring.domain.User.UpdateUser;
import com.course.cursospring.services.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController 
@RequestMapping ("/api/user")
@Validated 
@AllArgsConstructor 
public class UserController {
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user = this.userService.findById(id);
        return ResponseEntity.ok().body(user);
    }
    
    @PostMapping
    @Validated (CreateUser.class)
    public ResponseEntity<Void> create(@Valid @RequestBody User userBody) {
        this.userService.create(userBody);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(userBody.getId()).toUri();
        return ResponseEntity.created(uri).build(); 
    }
    
    @PutMapping ("/{id}")
    @Validated (UpdateUser.class)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody User userBody) {
        userBody.setId(id);
        this.userService.update(userBody);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
