package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getUserById(long id);

    User createUser(UserDto userDto);

    User updateUser(long id, UserDto userDto);

    void deleteUser(long id);

    String getCurrentUserName();

    User getCurrentUser();
}
