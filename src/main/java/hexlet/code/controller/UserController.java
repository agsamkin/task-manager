package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            Map<String, String> mapErrors = new HashMap<>();
//            for (FieldError error : bindingResult.getFieldErrors()) {
//                mapErrors.put(error.getField(), error.getDefaultMessage());
//            }
//            return new ResponseEntity<>(mapErrors, HttpStatus.BAD_REQUEST);
//        }
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id,
                              @RequestBody @Valid User user, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            Map<String, String> mapErrors = new HashMap<>();
//            for (FieldError error : bindingResult.getFieldErrors()) {
//                mapErrors.put(error.getField(), error.getDefaultMessage());
//            }
//            return new ResponseEntity<>(mapErrors, HttpStatus.BAD_REQUEST);
//        }
        return new ResponseEntity<>(userService.updateUser(id, user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
    }
}

