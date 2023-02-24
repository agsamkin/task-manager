package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
public class TaskStatusController {
    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";

    private final TaskStatusService taskStatusService;

    @GetMapping("/{id}")
    public TaskStatusDto getById(@PathVariable("id") long id) {
       return taskStatusService.getTaskStatusById(id);
    }

    @GetMapping
    public List<TaskStatusDto> getAll() {
        return taskStatusService.getAllTaskStatuses();
    }

    @PostMapping
    public TaskStatusDto create(@RequestBody @Valid TaskStatusDto taskStatusDto, BindingResult bindingResult) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @PutMapping("/{id}")
    public TaskStatusDto update(@PathVariable("id") long id,
                              @RequestBody @Valid TaskStatusDto taskStatusDto, BindingResult bindingResult) {
        return taskStatusService.updateTaskStatus(id, taskStatusDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        taskStatusService.deleteTaskStatus(id);
    }
}
