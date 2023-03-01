package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
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

import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
public class TaskStatusController {
    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";

    private final TaskStatusService taskStatusService;

    @Operation(summary = "Get task status by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status was found"),
            @ApiResponse(responseCode = "404", description = "Task status with this id wasn`t found")
    })
    @GetMapping(ID)
    public TaskStatus getById(@PathVariable("id") long id) {
       return taskStatusService.getTaskStatusById(id);
    }

    @Operation(summary = "Get all task statuses")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public List<TaskStatus> getAll() {
        return taskStatusService.getAllTaskStatuses();
    }

    @Operation(summary = "Create new task status")
    @ApiResponse(responseCode = "201", description = "Task status has been created")
    @ResponseStatus(CREATED)
    @PostMapping
    public TaskStatus create(@RequestBody @Valid TaskStatusDto taskStatusDto, BindingResult bindingResult) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @Operation(summary = "Update task status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task status has been updated"),
            @ApiResponse(responseCode = "404", description = "Task status with this id wasn`t found")
    })
    @PutMapping(ID)
    public TaskStatus update(@PathVariable("id") long id,
                              @RequestBody @Valid TaskStatusDto taskStatusDto, BindingResult bindingResult) {
        return taskStatusService.updateTaskStatus(id, taskStatusDto);
    }

    @Operation(summary = "Delete task status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task status has been deleted"),
            @ApiResponse(responseCode = "404", description = "Task status with this id wasn`t found")
    })
    @DeleteMapping(ID)
    public void delete(@PathVariable("id") long id) {
        taskStatusService.deleteTaskStatus(id);
    }
}
