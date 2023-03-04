package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.config.security.WebSecurityConfig;
import hexlet.code.dto.AuthenticationDto;
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
import static hexlet.code.utils.TestUtils.TEST_USER_1;
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
    public void clear() {
        testUtils.clear();
    }

    @Test
    public void getUserById() throws Exception {
        testUtils.regDefaultUser();
        User expectedUser = userRepository.findAll().get(0);

        var request = MockMvcRequestBuilders
                .get(BASE_URL + UserController.USER_CONTROLLER_PATH + UserController.ID,
                        expectedUser.getId());

        var response = testUtils.perform(
                request, expectedUser.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        User user = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getFirstName(), user.getFirstName());
        assertEquals(expectedUser.getLastName(), user.getLastName());
    }

    @Test
    public void getAllUsers() throws Exception {
        testUtils.regDefaultUser();

        var request = MockMvcRequestBuilders
                .get(BASE_URL + UserController.USER_CONTROLLER_PATH);

        var response = testUtils.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<User> users = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(users).hasSize(1);
    }

    @Test
    public void createUser() throws Exception {
        assertEquals(0, userRepository.count());

        testUtils.regDefaultUser().andExpect(status().isCreated());

        assertEquals(1, userRepository.count());
    }

    @Test
    public void twiceCreateUserFail() throws Exception {
        testUtils.regDefaultUser().andExpect(status().isCreated());
        testUtils.regDefaultUser().andExpect(status().isUnprocessableEntity());

        assertEquals(1, userRepository.count());
    }

    @Test
    public void login() throws Exception {
        testUtils.regDefaultUser();

        AuthenticationDto loginDto = AuthenticationDto.builder()
                .email(testUtils.getTestRegistrationUser().getEmail())
                .password(testUtils.getTestRegistrationUser().getPassword()).build();

        var request =
                MockMvcRequestBuilders.post(BASE_URL + WebSecurityConfig.LOGIN)
                        .content(asJson(loginDto))
                        .contentType(APPLICATION_JSON);

        testUtils.perform(request).andExpect(status().isOk());
    }

    @Test
    public void loginFail() throws Exception {
        AuthenticationDto loginDto = AuthenticationDto.builder()
                .email(testUtils.getTestRegistrationUser().getEmail())
                .password(testUtils.getTestRegistrationUser().getPassword()).build();

        var request =
                MockMvcRequestBuilders.post(BASE_URL + WebSecurityConfig.LOGIN)
                        .content(asJson(loginDto))
                        .contentType(APPLICATION_JSON);

        testUtils.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void updateUser() throws Exception {
        testUtils.regDefaultUser();

        User userUpdate = userRepository.findByEmail(TEST_USERNAME).get();

        var request = MockMvcRequestBuilders
                .put(BASE_URL + UserController.USER_CONTROLLER_PATH + UserController.ID,
                        userUpdate.getId());

        var updateRequest =
                        request.content(asJson(TEST_USER_1)).contentType(APPLICATION_JSON);

        testUtils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());

        assertTrue(userRepository.existsById(userUpdate.getId()));
        assertNull(userRepository.findByEmail(TEST_USERNAME).orElse(null));
        assertNotNull(userRepository.findByEmail(TEST_USER_1.getEmail()).orElse(null));
    }

    @Test
    public void deleteUser() throws Exception {
        testUtils.regDefaultUser();

        Long userId = userRepository.findByEmail(TEST_USERNAME).get().getId();
        var request = delete(BASE_URL + UserController.USER_CONTROLLER_PATH + UserController.ID, userId);

        testUtils.perform(request, TEST_USERNAME).andExpect(status().isOk());

        assertEquals(0, userRepository.count());
    }

    @Test
    public void deleteUserFails() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regUser(TEST_USER_1);

        Long userId = userRepository.findByEmail(TEST_USERNAME).get().getId();

        var request = delete(
                BASE_URL + UserController.USER_CONTROLLER_PATH + UserController.ID, userId);
        testUtils.perform(request, TEST_USER_1.getEmail()).andExpect(status().isForbidden());

        assertEquals(2, userRepository.count());
    }
}
