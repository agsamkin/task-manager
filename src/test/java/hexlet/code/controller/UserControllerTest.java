package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.config.security.WebSecurityConfig;
import hexlet.code.dto.AuthenticationDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static hexlet.code.utils.TestUtils.BASE_URL;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(SpringConfigForIT.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
class UserControllerTest {
    @Autowired
    private TestUtils testUtils;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clear() {
        testUtils.clear();
    }

    @Test
    void getUserById() throws Exception {
        testUtils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        final var request = MockMvcRequestBuilders
                .get(BASE_URL + UserController.USER_CONTROLLER_PATH + UserController.ID,
                        expectedUser.getId());

        final var response = testUtils.perform(
                request, expectedUser.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final User user = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getFirstName(), user.getFirstName());
        assertEquals(expectedUser.getLastName(), user.getLastName());
    }

    @Test
    void getAllUsers() throws Exception {
        testUtils.regDefaultUser();

        final var request = MockMvcRequestBuilders
                .get(BASE_URL + UserController.USER_CONTROLLER_PATH);

        final var response = testUtils.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<User> users = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(users).hasSize(1);
    }

    @Test
    void createUser() throws Exception {
        assertEquals(0, userRepository.count());
        testUtils.regDefaultUser().andExpect(status().isCreated());
        assertEquals(1, userRepository.count());
    }

    @Test
    void twiceCreateUserFail() throws Exception {
        testUtils.regDefaultUser().andExpect(status().isCreated());
        testUtils.regDefaultUser().andExpect(status().isUnprocessableEntity());

        assertEquals(1, userRepository.count());
    }

    @Test
    void login() throws Exception {
        testUtils.regDefaultUser();

        final AuthenticationDto loginDto = AuthenticationDto.builder()
                .email(testUtils.getTestRegistrationUser().getEmail())
                .password(testUtils.getTestRegistrationUser().getPassword()).build();

        final var loginRequest =
                MockMvcRequestBuilders.post(BASE_URL + WebSecurityConfig.LOGIN)
                        .content(asJson(loginDto))
                        .contentType(APPLICATION_JSON);
        testUtils.perform(loginRequest).andExpect(status().isOk());
    }

    @Test
    void loginFail() throws Exception {
        final AuthenticationDto loginDto = AuthenticationDto.builder()
                .email(testUtils.getTestRegistrationUser().getEmail())
                .password(testUtils.getTestRegistrationUser().getPassword()).build();

        final var loginRequest =
                MockMvcRequestBuilders.post(BASE_URL + WebSecurityConfig.LOGIN)
                        .content(asJson(loginDto))
                        .contentType(APPLICATION_JSON);
        testUtils.perform(loginRequest).andExpect(status().isUnauthorized());
    }

    @Test
    void updateUser() throws Exception {
        testUtils.regDefaultUser();

        final User userUpdate = userRepository.findByEmail(TEST_USERNAME).get();

        final String newFirstName = "New test";
        final String newLastName = "New test";
        final String newEmail = "new_test@test.ru";
        final String newPassword = "456";

        final UserDto newUserDto = UserDto.builder()
                .firstName(newFirstName)
                .lastName(newLastName)
                .email(newEmail)
                .password("456").build();

        final var request = MockMvcRequestBuilders
                .put(BASE_URL + UserController.USER_CONTROLLER_PATH + UserController.ID,
                        userUpdate.getId());

        final var updateRequest =
                        request.content(asJson(newUserDto)).contentType(APPLICATION_JSON);

        testUtils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());

        assertTrue(userRepository.existsById(userUpdate.getId()));
        assertNull(userRepository.findByEmail(TEST_USERNAME).orElse(null));
        assertNotNull(userRepository.findByEmail(newEmail).orElse(null));
    }

    @Test
    void deleteUser() throws Exception {
        testUtils.regDefaultUser();

        final Long userId = userRepository.findByEmail(TEST_USERNAME).get().getId();
        final var request = delete(BASE_URL + UserController.USER_CONTROLLER_PATH + UserController.ID, userId);

        testUtils.perform(request, TEST_USERNAME).andExpect(status().isOk());
        assertEquals(0, userRepository.count());
    }

    @Test
    void deleteUserFails() throws Exception {
        testUtils.regDefaultUser();

        final String newFirstName = "New test";
        final String newLastName = "New test";
        final String newEmail = "new_test@test.ru";
        final String newPassword = "456";

        final UserDto newUserDto = UserDto.builder()
                .firstName(newFirstName)
                .lastName(newLastName)
                .email(newEmail)
                .password("456").build();

        testUtils.regUser(newUserDto);

        final Long userId = userRepository.findByEmail(TEST_USERNAME).get().getId();

        final var request = delete(
                BASE_URL + UserController.USER_CONTROLLER_PATH + UserController.ID, userId);
        testUtils.perform(request, newEmail).andExpect(status().isForbidden());
        assertEquals(2, userRepository.count());
    }
}
