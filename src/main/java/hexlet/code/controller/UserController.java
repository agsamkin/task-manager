package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {
    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String ID = "/{id}";

    private final UserService userService;

    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

    @Operation(summary = "Get a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found"),
            @ApiResponse(responseCode = "404", description = "User with this id wasn`t found")
    })
    @GetMapping(ID)
    public User getById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @Operation(summary = "Create new user")
    @ApiResponse(responseCode = "201", description = "User has been created")
    @ResponseStatus(CREATED)
    @PostMapping
    public User create(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        return userService.createUser(userDto);
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User has been updated"),
            @ApiResponse(responseCode = "404", description = "User with this id wasn`t found")
    })
    @PutMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public User update(@PathVariable("id") long id,
                              @RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        return userService.updateUser(id, userDto);
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User has been deleted"),
            @ApiResponse(responseCode = "404", description = "User with this id wasn`t found")
    })
    @DeleteMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void delete(@PathVariable("id") long id) {
        userService.deleteUser(id);
    }
}

