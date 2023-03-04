package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.controller.TaskStatusController.ID;
import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.BASE_URL;
import static hexlet.code.utils.TestUtils.TEST_TASK_STATUS_1;
import static hexlet.code.utils.TestUtils.TEST_TASK_STATUS_2;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(SpringConfigForIT.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
class TaskStatusControllerTest {
    @Autowired
    private TestUtils utils;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @BeforeEach
    public void clear() {
        utils.clear();
    }

    @Test
    public void getTaskStatusById() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultTaskStatus(TEST_USERNAME).andExpect(status().isCreated());

        assertEquals(1, taskStatusRepository.count());

        TaskStatus expectedStatus = taskStatusRepository.findAll().get(0);

        var request = get(
                BASE_URL + TASK_STATUS_CONTROLLER_PATH + ID, expectedStatus.getId());
        var response = utils
                .perform(request, TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        TaskStatus status = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedStatus.getId(), status.getId());
        assertEquals(expectedStatus.getName(), status.getName());
    }

    @Test
    public void getTaskStatusByIdFails() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultTaskStatus(TEST_USERNAME);

        TaskStatus expectedStatus = taskStatusRepository.findAll().get(0);
        var request = get(
                BASE_URL + TASK_STATUS_CONTROLLER_PATH + ID, expectedStatus.getId());

        Exception exception = assertThrows(Exception.class, () -> utils.perform(request));

        String message = exception.getMessage();
        assertTrue(message.contains("No value present"));
    }

    @Test
    public void getAllTaskStatuses() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultTaskStatus(TEST_USERNAME);

        var request = get(
                BASE_URL + TASK_STATUS_CONTROLLER_PATH);
        var response = utils.perform(request, TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<TaskStatus> taskStatuses = fromJson(response.getContentAsString(),
                new TypeReference<>() {
        });
        assertThat(taskStatuses).hasSize(1);
    }

    @Test
    public void createTaskStatus() throws Exception {
        assertEquals(0, taskStatusRepository.count());

        utils.regDefaultUser();
        utils.regDefaultTaskStatus(TEST_USERNAME).andExpect(status().isCreated());

        assertEquals(1, taskStatusRepository.count());
    }

    @Test
    public void twiceCreateTaskStatusFail() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultTaskStatus(TEST_USERNAME).andExpect(status().isCreated());
        utils.regDefaultTaskStatus(TEST_USERNAME).andExpect(status().isUnprocessableEntity());

        assertEquals(1, taskStatusRepository.count());
    }

    @Test
    public void updateTaskStatus() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultTaskStatus(TEST_USERNAME);

        long statusID = taskStatusRepository.findAll().get(0).getId();

        var request = put(
                BASE_URL + TASK_STATUS_CONTROLLER_PATH + ID, statusID);
        var updateRequest = request
                        .content(asJson(TEST_TASK_STATUS_2))
                        .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TEST_TASK_STATUS_2.getName()).andExpect(status().isOk());

        assertTrue(taskStatusRepository.existsById(statusID));
        assertTrue(taskStatusRepository.findByName(TEST_TASK_STATUS_1.getName()).isEmpty());
        assertTrue(taskStatusRepository.findByName(TEST_TASK_STATUS_2.getName()).isPresent());
    }

    @Test
    public void deleteTaskStatus() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultTaskStatus(TEST_USERNAME);

        Long statusId = taskStatusRepository.findAll().get(0).getId();
        assertEquals(1, taskStatusRepository.count());

        var request = delete(
                BASE_URL + TASK_STATUS_CONTROLLER_PATH + ID, statusId);

        utils.perform(request, TEST_USERNAME)
                .andExpect(status().isOk());

        assertEquals(0, taskStatusRepository.count());
    }

    @Test
    public void deleteTaskStatusFails() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultTaskStatus(TEST_USERNAME);

        Long statusId = taskStatusRepository.findAll().get(0).getId() + 1;

        var request = delete(BASE_URL + TASK_STATUS_CONTROLLER_PATH + ID, statusId);
        utils.perform(request, TEST_USERNAME)
                .andExpect(status().isNotFound());

        assertEquals(1, taskStatusRepository.count());
    }
}
