package hexlet.code.service.impl;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(u -> convertToUserDto(u))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long id) {
        User user = userRepository.findById(id);
        return convertToUserDto(user);
    }

    @Override
    public UserDto createUser(User user) {
        User newUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword()).build();
        return convertToUserDto(userRepository.save(newUser));
    }

    @Override
    public UserDto updateUser(long id, User user) {
        User updateUser = userRepository.findById(id);
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setEmail(user.getEmail());
        updateUser.setPassword(user.getPassword());
        return convertToUserDto(userRepository.save(updateUser));
    }

    @Override
    public void deleteUser(long id) {
        User user = userRepository.findById(id);
        userRepository.delete(user);
    }
}
