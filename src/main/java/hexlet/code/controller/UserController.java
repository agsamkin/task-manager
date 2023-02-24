package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.service.UserService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("${base-url}/users")
public class UserController {

    public static final String USER_CONTROLLER_PATH = "/users";
    private final UserService userService;

    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).getEmail() == authentication.getName()
        """;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public UserDto createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            Map<String, String> mapErrors = new HashMap<>();
//            for (FieldError error : bindingResult.getFieldErrors()) {
//                mapErrors.put(error.getField(), error.getDefaultMessage());
//            }
//            return new ResponseEntity<>(mapErrors, HttpStatus.BAD_REQUEST);
//        }
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public UserDto updateUser(@PathVariable("id") long id,
                              @RequestBody @Valid User user, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            Map<String, String> mapErrors = new HashMap<>();
//            for (FieldError error : bindingResult.getFieldErrors()) {
//                mapErrors.put(error.getField(), error.getDefaultMessage());
//            }
//            return new ResponseEntity<>(mapErrors, HttpStatus.BAD_REQUEST);
//        }
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
    }
}

