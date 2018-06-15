package com.crud.tasks.repository;

import org.springframework.data.repository.CrudRepository;
import com.crud.tasks.domain.Task;
import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findAll();
}
