package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)

public class TaskControllerTest {

    @MockBean
    DbService dbService;

    @MockBean
    //@Autowired
    private TaskMapper taskMapper;

    @Autowired
    private MockMvc mockMvc;

    //private TaskController taskController;
    private List<TaskDto> listOfTasksDto;
    private List<Task> listOfTasks;
    private TaskDto taskDto1, taskDto2;
    private Task task1, task2;

    @Before
    public void init() {
        //taskMapper = new TaskMapper();
        //taskController = new TaskController();
        listOfTasksDto = new ArrayList<>();
        listOfTasks = new ArrayList<>();
        taskDto1 = new TaskDto(1L, "TestTask1", "Content1");
        taskDto2 = new TaskDto(2L, "Task2", "Content2");
        task1 = new Task(1L, "TestTask1", "Content1");
        task2 = new Task(2L, "Task2", "Content2");

        listOfTasksDto.add(taskDto1);
        listOfTasksDto.add(taskDto2);

        listOfTasks.add(task1);
        listOfTasks.add(task2);
    }

    @Test
    public void shouldGetEmptyTasksList() throws Exception {
        //Given
        List<Task> emptyTaskList = new ArrayList<>();
        when(taskMapper.mapToTaskDtoList(emptyTaskList)).thenReturn(new ArrayList<TaskDto>());
        when(dbService.getAllTasks()).thenReturn(emptyTaskList);

        //When & Then
        mockMvc.perform(get("/v1/tasks"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldReturnTaskList() throws Exception {
        //Given
        when(dbService.getAllTasks()).thenReturn(listOfTasks);
        when(taskMapper.mapToTaskDtoList(listOfTasks)).thenReturn(listOfTasksDto);

        //When & Then
        mockMvc.perform(get("/v1/tasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("TestTask1")));
    }

    @Test
    public void shouldFindNoTasks() throws Exception {
        //Given
        String searchParam = "testSearch";
        List<Task> resultOfSearch = new ArrayList<>();
        when(dbService.searchTasks(searchParam)).thenReturn(resultOfSearch);
        when(taskMapper.mapToTaskDtoList(resultOfSearch)).thenReturn(new ArrayList<TaskDto>());

        //When & Then
        mockMvc.perform(get("/v1/searchTasks").param("searchPattern", searchParam))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldFindTasks() throws Exception {
        //Given
        String searchParam = "test";
        Task searchedTask = new Task(1L, "test Task", "Search result task");
        TaskDto searchedDtoTask = new TaskDto(1L, "test Task", "Search result task");
        List<Task> resultOfSearch = new ArrayList<>();
        List<TaskDto> resultOfDtoSearch = new ArrayList<>();
        resultOfSearch.add(searchedTask);
        resultOfDtoSearch.add(searchedDtoTask);

        when(dbService.searchTasks(searchParam)).thenReturn(resultOfSearch);
        when(taskMapper.mapToTaskDtoList(resultOfSearch)).thenReturn(resultOfDtoSearch);

        //When & Then
        mockMvc.perform(get("/v1/searchTasks").param("searchPattern", searchParam).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("test Task")))
                .andExpect(jsonPath("$[0].content", is("Search result task")));
    }

    @Test
    public void shouldDeleteTask() throws Exception {
        //Given
        Long deleteId = 1L;
        Long deletedDtoId = 1L;
        TaskDto deletedTaskDto = new TaskDto(1L, "test Task", "Delete result task");
        Task deletedTask = new Task(1L, "test Task", "Delete result task");
        when(taskMapper.mapDtoIdToId(deletedDtoId)).thenReturn(deletedDtoId);
        when(taskMapper.mapToTaskDto(deletedTask)).thenReturn(deletedTaskDto);
        when(dbService.getById(deleteId)).thenReturn(deletedTask);

        //When & Then
        mockMvc.perform(delete("/v1/tasks/" + deleteId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("test Task")))
                .andExpect(jsonPath("$.content", is("Delete result task")));
    }

    @Test
    public void shouldUpdateTask() throws Exception {
        //Given
        TaskDto dtoTaskToUpdate = new TaskDto(1L, "test Task - update", "Update result task");
        Task taskToUpdate = new Task(1L, "test Task - update", "Update result task");
        Gson gson = new Gson();
        String jsonTaskToUpdate = gson.toJson(dtoTaskToUpdate);

        when(taskMapper.mapToTask(dtoTaskToUpdate)).thenReturn(taskToUpdate);
        when(taskMapper.mapToTaskDto(taskToUpdate)).thenReturn(dtoTaskToUpdate);
        when(dbService.saveTask(taskToUpdate)).thenReturn(taskToUpdate);

        //When & Then
        mockMvc.perform(put("/v1/tasks").contentType(MediaType.APPLICATION_JSON).content(jsonTaskToUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("test Task - update")))
                .andExpect(jsonPath("$.content", is("Update result task")));
    }

    @Test
    public void shouldCreateTask() throws Exception {
        //When
        TaskDto dtoTaskToCreate = new TaskDto(2L, "Task to create", "Task to create");
        Task taskToCreate = new Task(2L, "Task to create", "Task to create");
        Gson gson = new Gson();
        String jsonTaskToCreate = gson.toJson(dtoTaskToCreate);

        when(taskMapper.mapToTask(dtoTaskToCreate)).thenReturn(taskToCreate);
        when(taskMapper.mapToTaskDto(taskToCreate)).thenReturn(dtoTaskToCreate);
        when(dbService.saveTask(taskToCreate)).thenReturn(taskToCreate);

        //When & Then
        mockMvc.perform(post("/v1/tasks").contentType(MediaType.APPLICATION_JSON).content(jsonTaskToCreate).characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("Task to create")))
                .andExpect(jsonPath("$.content", is("Task to create")));
    }
}