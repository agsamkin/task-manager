package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;

import java.util.List;

public interface TaskStatusService {
    List<TaskStatusDto> getAllTaskStatuses();

    TaskStatusDto getTaskStatusById(long id);

    TaskStatusDto createTaskStatus(TaskStatusDto taskStatusDto);

    TaskStatusDto updateTaskStatus(long id, TaskStatusDto taskStatusDto);

    void deleteTaskStatus(long id);
}
