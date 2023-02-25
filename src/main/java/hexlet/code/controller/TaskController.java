package hexlet.code.controller;


import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.service.TaskService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {
    public static final String TASK_CONTROLLER_PATH = "/tasks";

    private static final String ONLY_OWNER_BY_ID = """
            @taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()
        """;

    private final TaskService taskService;

    @GetMapping("/{id}")
    public Task getById(@PathVariable("id") long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping
    public List<Task> getAll() {
        return taskService.getAllTasks();
    }

    @PostMapping
    public Task create(@RequestBody @Valid TaskDto taskDto, BindingResult bindingResult) {
        return taskService.createTask(taskDto);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable("id") long id,
                             @RequestBody @Valid TaskDto taskDto, BindingResult bindingResult) {
        return taskService.updateTask(id, taskDto);
    }

    @PreAuthorize(ONLY_OWNER_BY_ID)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        taskService.deleteTask(id);
    }

}
