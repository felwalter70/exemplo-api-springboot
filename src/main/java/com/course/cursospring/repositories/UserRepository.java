package com.course.cursospring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.course.cursospring.domain.User;

@Repository 
public interface UserRepository extends JpaRepository<User, Long> {
    
}
