package com.course.cursospring.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.course.cursospring.domain.Task;
import com.course.cursospring.domain.User;
import com.course.cursospring.domain.enums.ProfileEnum;
import com.course.cursospring.repositories.TaskRepository;
import com.course.cursospring.security.UserSpringSecurity;
import com.course.cursospring.services.exceptions.AuthorizationException;
import com.course.cursospring.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public Task findById(Long id) {
        Optional<Task> optTask = this.taskRepository.findById(id);
        Task task = optTask.orElseThrow(() -> new ObjectNotFoundException(String.format("Task not found. Id: %d", id)));
        
        UserSpringSecurity userSpringSecurity = UserService.authorized();

        if (Objects.isNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task))
            throw new AuthorizationException("Acesso negado!");

        return task;
    }

    public List<Task> findAllByUser() {
        UserSpringSecurity userSpringSecurity = UserService.authorized();

        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        List<Task> tasks = this.taskRepository.findByUser_id(userSpringSecurity.getId());
        return tasks;
    }

    @Transactional 
    public Task create(Task obj) {
        UserSpringSecurity userSpringSecurity = UserService.authorized();

        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        User taskUser = this.userService.findById(userSpringSecurity.getId());

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

    private Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }
}
