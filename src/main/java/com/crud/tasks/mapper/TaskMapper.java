package com.crud.tasks.mapper;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Component
public class TaskMapper {
    public Task mapToTask(final TaskDto taskDto) {
        return new Task(
                taskDto.getId(),
                taskDto.getTitle(),
                taskDto.getContent()
        );
    }

    public TaskDto mapToTaskDto(final Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getContent()
        );
    }

    public List<TaskDto> mapToTaskDtoList(final List<Task> taskList) {
        return taskList.stream()
                .map(task -> new TaskDto(task.getId(), task.getTitle(), task.getContent()))
                .collect(Collectors.toList());
    }

    public List<Task> mapToTaskList(final List<TaskDto> listTaskDto) {
        return listTaskDto.stream()
                .map(taskDto -> new Task(taskDto.getId(), taskDto.getTitle(), taskDto.getContent()))
                .collect(Collectors.toList());
    }

    public Long mapIdToDtoId(long taskId) {
        Task tempTask = new Task();
        tempTask.setId(taskId);
        return mapToTaskDto(tempTask).getId();
    }

    public Long mapDtoIdToId(long taskId) {
        TaskDto tempTask = new TaskDto();
        tempTask.setId(taskId);
        return mapToTask(tempTask).getId();
    }

}
