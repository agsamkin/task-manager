package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String ID = "/{id}";
    private static final String ONLY_OWNER_BY_ID = """
            @taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()
        """;

    private final TaskService taskService;

    @Operation(summary = "Get a task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task was found"),
            @ApiResponse(responseCode = "404", description = "Task with this id wasn`t found")
    })
    @GetMapping(ID)
    public Task getById(@PathVariable("id") long id) {
        return taskService.getTaskById(id);
    }

    @Operation(summary = "Get all tasks if no filtration is set."
            + " Else Retrieves all the elements that match the conditions defined by the specified predicate")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public List<Task> getAll(@QuerydslPredicate final Predicate predicate) {
        if (Objects.isNull(predicate)) {
            return taskService.getAllTasks();
        }
        return taskService.getAllTasks(predicate);
    }

    @Operation(summary = "Create a new task")
    @ApiResponse(responseCode = "201", description = "Task has been created")
    @ResponseStatus(CREATED)
    @PostMapping
    public Task create(@RequestBody @Valid TaskDto taskDto, BindingResult bindingResult) {
        return taskService.createTask(taskDto);
    }

    @Operation(summary = "Update a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task has been updated"),
            @ApiResponse(responseCode = "404", description = "Task with this id wasn`t found")
    })
    @PreAuthorize(ONLY_OWNER_BY_ID)
    @PutMapping(ID)
    public Task update(@PathVariable("id") long id,
                             @RequestBody @Valid TaskDto taskDto, BindingResult bindingResult) {
        return taskService.updateTask(id, taskDto);
    }

    @Operation(summary = "Delete task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task has been deleted"),
            @ApiResponse(responseCode = "404", description = "Task with this id wasn`t found")
    })
    @PreAuthorize(ONLY_OWNER_BY_ID)
    @DeleteMapping(ID)
    public void delete(@PathVariable("id") long id) {
        taskService.deleteTask(id);
    }

}
