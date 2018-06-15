package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/task")
public class TaskController {
    @Autowired
    private DbService dbService;
    @Autowired
    private TaskMapper taskMapper;

    @RequestMapping(method = RequestMethod.GET, value = "getTasks")
    public List<TaskDto> getTasks() {
        return taskMapper.mapToTaskDtoList(dbService.getAllTasks());
    }

    @RequestMapping(method = RequestMethod.GET, value = "getTask")
    public TaskDto getTask(@RequestParam("taskId") final long taskID) {
        return taskMapper.mapToTaskDto(dbService.getById(taskID));
        //czy to nie jest za prosto ? A jak dtoTask.id!=task.id ?
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "deleteTask")
    public TaskDto deleteTask(@RequestParam("taskId") long dtoTaskId) {
        Task taskToDelete = dbService.getById(taskMapper.mapDtoIdToId(dtoTaskId));
        //czy kontroler powinien operować na obiektach domenowych ??

        if(!taskToDelete.equals(null)) {
            dbService.delete(taskToDelete);
        }
        return taskMapper.mapToTaskDto(taskToDelete);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "updateTask")
    public TaskDto updateTask(@RequestBody TaskDto taskDto) {
        return taskMapper.mapToTaskDto(dbService.saveTask(taskMapper.mapToTask(taskDto)));
    }

    @RequestMapping(method = RequestMethod.POST, value = "createTask", consumes = APPLICATION_JSON_VALUE)
    public void createTask(@RequestBody  TaskDto taskDto) {
        dbService.saveTask(taskMapper.mapToTask(taskDto));
    }

}
