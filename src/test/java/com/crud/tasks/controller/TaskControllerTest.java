package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)

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
        ł.andExpect(jsonPath("$[0].content", is("Search result task")));

    }
}