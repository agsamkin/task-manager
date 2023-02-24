package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto getUserById(long id);

    UserDto createUser(User user);

    UserDto updateUser(long id, User user);

    void deleteUser(long id);

    String getCurrentUserName();

    User getCurrentUser();
}
