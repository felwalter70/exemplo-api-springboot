package com.course.cursospring.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.course.cursospring.domain.Task;
import com.course.cursospring.domain.projection.TaskProjection;

@Repository 
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<TaskProjection> findByUser_id(Long id);
}
