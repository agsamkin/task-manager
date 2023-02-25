package hexlet.code.service;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();

    Task getTaskById(long id);

    Task createTask(TaskDto taskDto);

    Task updateTask(long id, TaskDto taskDto);

    void deleteTask(long id);
}
