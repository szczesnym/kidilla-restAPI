package com.crud.tasks.trello.client;

import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskClient {

    @Autowired
    private DbService dbService;

    @Autowired
    private TaskMapper taskMapper;

    public List<TaskDto> getTasksList() {
        {
            return taskMapper.mapToTaskDtoList(dbService.getAllTasks());
        }
    }
}
