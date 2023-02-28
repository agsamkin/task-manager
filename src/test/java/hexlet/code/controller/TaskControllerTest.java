package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.TaskStatusDto;
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
class TaskControllerTest {
    @Autowired
    private TestUtils testUtils;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @BeforeEach
    void clear() {
        testUtils.clear();
    }

    @Test
    void getTaskStatusById() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regDefaultTaskStatus(TEST_USERNAME).andExpect(status().isCreated());
        assertEquals(1, taskStatusRepository.count());

        final TaskStatus expectedStatus = taskStatusRepository.findAll().get(0);

        final var request = get(BASE_URL + TASK_STATUS_CONTROLLER_PATH + ID, expectedStatus.getId());
        final var response = testUtils
                .perform(request, TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final TaskStatus status = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedStatus.getId(), status.getId());
        assertEquals(expectedStatus.getName(), status.getName());
    }

    @Test
    public void getTaskStatusByIdFails() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regDefaultTaskStatus(TEST_USERNAME);

        final TaskStatus expectedStatus = taskStatusRepository.findAll().get(0);

        final var request = get(BASE_URL + TASK_STATUS_CONTROLLER_PATH + ID, expectedStatus.getId());
        Exception exception = assertThrows(Exception.class, () -> testUtils.perform(request));

        String message = exception.getMessage();
        assertTrue(message.contains("No value present"));
    }

    @Test
    void getAllTaskStatuses() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regDefaultTaskStatus(TEST_USERNAME);

        final var request = get(BASE_URL + TASK_STATUS_CONTROLLER_PATH);
        final var response = testUtils.perform(request, TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<TaskStatus> taskStatuses = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(taskStatuses).hasSize(1);
    }

    @Test
    void createTaskStatus() throws Exception {
        assertEquals(0, taskStatusRepository.count());
        testUtils.regDefaultUser();
        testUtils.regDefaultTaskStatus(TEST_USERNAME).andExpect(status().isCreated());
        assertEquals(1, taskStatusRepository.count());
    }

    @Test
    public void twiceCreateTaskStatusFail() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regDefaultTaskStatus(TEST_USERNAME).andExpect(status().isCreated());
        testUtils.regDefaultTaskStatus(TEST_USERNAME).andExpect(status().isUnprocessableEntity());
        assertEquals(1, taskStatusRepository.count());
    }

    @Test
    void updateTaskStatus() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regDefaultTaskStatus(TEST_USERNAME);

        final long statusID = taskStatusRepository.findAll().get(0).getId();
        final String newTaskStatusName = "new name";
        TaskStatusDto taskStatusDto = TaskStatusDto.builder()
                .name(newTaskStatusName).build();

        final var request = put(BASE_URL + TASK_STATUS_CONTROLLER_PATH + ID, statusID);
        final var updateRequest = request
                        .content(asJson(taskStatusDto))
                        .contentType(APPLICATION_JSON);

        testUtils.perform(updateRequest, newTaskStatusName).andExpect(status().isOk());

        assertTrue(taskStatusRepository.existsById(statusID));
        assertTrue(taskStatusRepository.findByName(testUtils.getTestTaskStatus().getName()).isEmpty());
        assertTrue(taskStatusRepository.findByName(newTaskStatusName).isPresent());
    }

    @Test
    void deleteTaskStatus() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regDefaultTaskStatus(TEST_USERNAME);
        final Long statusId = taskStatusRepository.findAll().get(0).getId();
        assertEquals(1, taskStatusRepository.count());

        final var request = delete(BASE_URL + TASK_STATUS_CONTROLLER_PATH + ID, statusId);
        testUtils.perform(request, TEST_USERNAME)
                .andExpect(status().isOk());

        assertEquals(0, taskStatusRepository.count());
    }

    @Test
    public void deleteTaskStatusFails() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regDefaultTaskStatus(TEST_USERNAME);
        final Long statusId = taskStatusRepository.findAll().get(0).getId() + 1;

        final var request = delete(BASE_URL + TASK_STATUS_CONTROLLER_PATH + ID, statusId);
        testUtils.perform(request, TEST_USERNAME)
                .andExpect(status().isNotFound());
        assertEquals(1, taskStatusRepository.count());
    }

}
