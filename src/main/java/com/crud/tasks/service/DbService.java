package com.crud.tasks.service;

import com.crud.tasks.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.crud.tasks.domain.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DbService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return this.taskRepository.findAll();
    }

    public List<Task> searchTasks(String searchPattern) {
        List<Task> resultSearch = new ArrayList<>();

        resultSearch.addAll(this.taskRepository.findByNameContaining(searchPattern));
        resultSearch.addAll(this.taskRepository.findByContentContaining(searchPattern));
        return resultSearch;
    }

    public Task getById(long taskId) {
        return this.taskRepository.findById(taskId).orElse(null);
    }

    public Task saveTask(final Task task) {
        return taskRepository.save(task);
    }

    public void delete(Task task) {
        taskRepository.deleteById(task.getId());
    }

}
