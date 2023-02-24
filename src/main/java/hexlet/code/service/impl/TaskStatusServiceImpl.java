package hexlet.code.service.impl;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;
    private final ModelMapper modelMapper;

    private TaskStatusDto convertToTaskStatusDto(TaskStatus taskStatus) {
        return modelMapper.map(taskStatus, TaskStatusDto.class);
    }

    @Override
    public List<TaskStatusDto> getAllTaskStatuses() {
        List<TaskStatus> taskStatuses = taskStatusRepository.findAll();
        return taskStatuses.stream()
                .map(this::convertToTaskStatusDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskStatusDto getTaskStatusById(long id) {
        return taskStatusRepository.findById(id)
                .map(this::convertToTaskStatusDto)
                .orElseThrow(() -> new NoSuchElementException("Task status not found"));
    }

    @Override
    public TaskStatusDto createTaskStatus(TaskStatusDto taskStatusDto) {
        TaskStatus newTaskStatus = TaskStatus.builder()
                .name(taskStatusDto.getName()).build();
        return convertToTaskStatusDto(taskStatusRepository.save(newTaskStatus));
    }

    @Override
    public TaskStatusDto updateTaskStatus(long id, TaskStatusDto taskStatusDto) {
        return taskStatusRepository.findById(id)
                .map(ts -> {
                    ts.setName(taskStatusDto.getName());
                    return taskStatusRepository.save(ts);
                }).map(this::convertToTaskStatusDto)
                .orElseThrow(() -> new NoSuchElementException("Task status not found"));
    }

    @Override
    public void deleteTaskStatus(long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task status not found"));
        taskStatusRepository.delete(taskStatus);
    }
}
