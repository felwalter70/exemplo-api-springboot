package com.course.cursospring.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.course.cursospring.domain.Task;
import com.course.cursospring.domain.User;
import com.course.cursospring.repositories.TaskRepository;
import com.course.cursospring.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new ObjectNotFoundException(String.format("Task not found. Id: %d", id)));
    }

    public List<Task> findAllByUserId(Long userId) {
        List<Task> tasks = this.taskRepository.findByUser_id(userId);
        return tasks;
    }

    @Transactional 
    public Task create(Task obj) {
        User taskUser = userService.findById(obj.getUser().getId());

        obj.setId(null);
        obj.setUser(taskUser);

        Task task = taskRepository.save(obj);

        return task;
    }

    @Transactional 
    public Task update(Task obj) {
        Task newTaskObj = findById(obj.getId());
        newTaskObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newTaskObj);
    }

    public void delete(Long id) {
        this.findById(id);
        try {
            this.taskRepository.deleteById(id);
        }
        catch (Exception e) {
            throw new RuntimeException("Task delete is prohibited (Related to another model)");
        }
    }
}
