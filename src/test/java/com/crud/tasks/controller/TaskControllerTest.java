package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
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
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)

//Czemu pokrycie testów TaskControllr pokazuje 0% ?

public class TaskControllerTest {
    @MockBean
    private TaskController taskController;

    @Autowired
    private MockMvc mockMvc;

    private TaskMapper taskMapper;
    private List<TaskDto> listOfTasksDto;
    private List<Task> listOfTasks;
    private TaskDto task1, task2;

    @Before
    public void init() {
        taskMapper = new TaskMapper();
        listOfTasksDto = new ArrayList<>();
        listOfTasks = new ArrayList<>();
        task1 = new TaskDto(1L, "TestTask1", "Content1");
        task2 = new TaskDto(2L, "Task2", "Content2");
        listOfTasksDto.add(task1);
        listOfTasksDto.add(task2);
    }

    @Test
    public void shouldGetEmptyTasksList() throws Exception {
        //Given
        when(taskController.getTasks()).thenReturn(new ArrayList<>());
        //When & Then
        mockMvc.perform(get("/v1/task/getTasks"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldReturnTaskList() throws Exception {
        //Given
        when(taskController.getTasks()).thenReturn(listOfTasksDto);
        //When & Then
        mockMvc.perform(get("/v1/task/getTasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("TestTask1")));
    }

    @Test
    public void shouldFindNoTasks() throws Exception {
        //Given
        String searchParam = "testSearch";
        List<TaskDto> resultOfSearch = new ArrayList<>();
        when(taskController.searchTasks(searchParam)).thenReturn(resultOfSearch);
        //When & Then
        mockMvc.perform(get("/v1/task/searchTasks").param("searchPattern", searchParam))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldFindTasks() throws Exception {
        //Given
        String searchParam = "test";
        TaskDto searchTask = new TaskDto(1L, "test Task", "Search result task");
        List<TaskDto> resultOfSearch = new ArrayList<>();
        resultOfSearch.add(searchTask);
        when(taskController.searchTasks(searchParam)).thenReturn(resultOfSearch);

        //When & Then
        mockMvc.perform(get("/v1/task/searchTasks").param("searchPattern", searchParam).contentType(MediaType.APPLICATION_JSON))
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
        TaskDto deletedTask = new TaskDto(1L, "test Task", "Delete result task");

        when(taskController.deleteTask(deleteId)).thenReturn(deletedTask);
        //When & Then
        mockMvc.perform(delete("/v1/task/deleteTask").param("taskId", deleteId.toString()))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$", hasSize(1))); - czemu to nie działa ?
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("test Task")))
                .andExpect(jsonPath("$.content", is("Delete result task")));
    }

    @Test
    public void shouldUpdateTask() throws Exception {
        //Given
        TaskDto taskToUpdate = new TaskDto(1L, "test Task - update", "Update result task");
        Gson gson = new Gson();
        String jsonTaskToUpdate = gson.toJson(taskToUpdate);
        when(taskController.updateTask(any(TaskDto.class))).thenReturn(taskToUpdate); //czemu nie działa updateTask(taskToUpdate) ?
        //When & Then
        mockMvc.perform(put("/v1/task/updateTask").contentType(MediaType.APPLICATION_JSON).content(jsonTaskToUpdate))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$", hasSize(1)));
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("test Task - update")))
                .andExpect(jsonPath("$.content", is("Update result task")));
    }

    @Test
    public void shouldCreateTask() throws Exception {
        //When
        TaskDto taskToCreate = new TaskDto(2L, "Task to create", "Task to create");
        Gson gson = new Gson();
        String jsonTaskToCreate = gson.toJson(taskToCreate);
        when(taskController.createTask(any(TaskDto.class))).thenReturn(taskToCreate); //czemu nie działa createTask(taskToCerate) ?

        //When & Then
        mockMvc.perform(post("/v1/task/createTask").contentType(MediaType.APPLICATION_JSON).content(jsonTaskToCreate).characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("Task to create")))
                .andExpect(jsonPath("$.content", is("Task to create")));
    }
}