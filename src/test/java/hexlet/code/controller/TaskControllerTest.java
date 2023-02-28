package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
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

import static hexlet.code.controller.TaskController.ID;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.BASE_URL;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(SpringConfigForIT.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
class TaskControllerTest {
    @Autowired
    private TestUtils testUtils;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void clear() {
        testUtils.clear();
    }

    @Test
    void getTaskById() throws Exception {
        testUtils.regDefaultTask(TEST_USERNAME);
        final var expectedTask = taskRepository.findAll().get(0);

        final var request = get(BASE_URL + TASK_CONTROLLER_PATH + ID, expectedTask.getId());
        final var response = testUtils
                .perform(request, TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Task task = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedTask.getId(), task.getId());
        assertEquals(expectedTask.getName(), task.getName());
        assertEquals(expectedTask.getDescription(), task.getDescription());
    }

    @Test
    void getTaskByIdFails() throws Exception {
        testUtils.regDefaultTask(TEST_USERNAME);
        final Task expectedTask = taskRepository.findAll().get(0);

        final var request = get(BASE_URL + TASK_CONTROLLER_PATH + UserController.ID,
                expectedTask.getId());

        Exception exception = assertThrows(
                Exception.class, () -> testUtils.perform(request)
        );
        String message = exception.getMessage();
        assertTrue(message.contains("No value present"));
    }

    @Test
    void getAllTasks() throws Exception {
        testUtils.regDefaultTask(TEST_USERNAME);

        final var request = get(BASE_URL + TASK_CONTROLLER_PATH);
        final var response = testUtils.perform(request, TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Task> tasks = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(tasks).hasSize(1);
    }

    @Test
    void createTask() throws Exception {
        assertEquals(0, taskRepository.count());
        testUtils.regDefaultTask(TEST_USERNAME).andExpect(status().isCreated());
        assertEquals(1, taskRepository.count());
    }

    @Test
    void updateTask() throws Exception {
        testUtils.regDefaultTask(TEST_USERNAME);
        Task task = taskRepository.findAll().get(0);
        final Long taskId = task.getId();
        final var newTaskDto = new TaskDto(
                "newTask",
                "newDescription",
                task.getTaskStatus().getId(),
                task.getExecutor().getId(),
                null

        );

        final var request = put(BASE_URL + TASK_CONTROLLER_PATH + ID,
                taskId);
        final var updateRequest = request
                .content(asJson(newTaskDto))
                .contentType(APPLICATION_JSON);

        testUtils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());
        assertTrue(taskRepository.existsById(taskId));
        assertNull(taskRepository.findByName(task.getName()).orElse(null));
        assertNotNull(taskRepository.findByName(newTaskDto.getName()).orElse(null));
    }

    @Test
    void deleteTask() throws Exception {
        assertEquals(0, taskRepository.count());
        testUtils.regDefaultTask(TEST_USERNAME);
        assertEquals(1, taskRepository.count());
        final Long taskID = taskRepository.findAll().get(0).getId();

        final var request = delete(BASE_URL + TASK_CONTROLLER_PATH + ID, taskID);
        testUtils.perform(request, TEST_USERNAME)
                .andExpect(status().isOk());
        assertEquals(0, taskRepository.count());
    }

    @Test
    public void deleteTaskFails() throws Exception {
        testUtils.regDefaultTask(TEST_USERNAME);
        final Long taskId = taskRepository.findAll().get(0).getId() + 1;

        final var request = delete(BASE_URL + TASK_CONTROLLER_PATH + UserController.ID, taskId);
        testUtils.perform(request, TEST_USERNAME)
                .andExpect(status().isNotFound());
        assertEquals(1, taskRepository.count());
    }
}
