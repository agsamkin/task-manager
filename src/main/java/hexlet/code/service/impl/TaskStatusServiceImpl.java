package hexlet.code.service.impl;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.exception.custom.TaskStatusNotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskStatusServiceImpl implements TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;

    @Override
    public List<TaskStatus> getAllTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    @Override
    public TaskStatus getTaskStatusById(long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new TaskStatusNotFoundException("Task status not found"));
    }

    @Override
    public TaskStatus createTaskStatus(TaskStatusDto taskStatusDto) {
        TaskStatus newTaskStatus = TaskStatus.builder()
                .name(taskStatusDto.getName()).build();
        return taskStatusRepository.save(newTaskStatus);
    }

    @Override
    public TaskStatus updateTaskStatus(long id, TaskStatusDto taskStatusDto) {
        return taskStatusRepository.findById(id)
                .map(ts -> {
                    ts.setName(taskStatusDto.getName());
                    return taskStatusRepository.save(ts);
                })
                .orElseThrow(() -> new TaskStatusNotFoundException("Task status not found"));
    }

    @Override
    public void deleteTaskStatus(long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new TaskStatusNotFoundException("Task status not found"));
        taskStatusRepository.delete(taskStatus);
    }
}
