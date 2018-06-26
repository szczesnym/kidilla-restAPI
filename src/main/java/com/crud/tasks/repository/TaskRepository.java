package com.crud.tasks.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.crud.tasks.domain.Task;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findAll();

    @Query("FROM tasks where name like %:searchPattern%")
    List<Task> findByNameContaining(@Param("searchPattern") String searchPattern);

    @Query("FROM tasks where description like %:searchPattern%")
    List<Task> findByContentContaining(@Param("searchPattern") String searchPattern);

}
