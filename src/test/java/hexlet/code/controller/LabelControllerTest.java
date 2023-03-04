package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
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

import static hexlet.code.controller.LabelController.ID;
import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.BASE_URL;
import static hexlet.code.utils.TestUtils.TEST_LABEL_2;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;
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
class LabelControllerTest {
    @Autowired
    private TestUtils utils;

    @Autowired
    private LabelRepository labelRepository;

    @BeforeEach
    public void clear() {
        utils.clear();
    }

    @Test
    public void getLabelById() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME);

        Label expectedLabel = labelRepository.findAll().get(0);

        var request = get(BASE_URL + LABEL_CONTROLLER_PATH + ID,
                expectedLabel.getId());
        var response =
                utils.perform(request, TEST_USERNAME)
                        .andExpect(status().isOk()).andReturn().getResponse();

        Label actualLabel = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedLabel.getId(), actualLabel.getId());
        assertEquals(expectedLabel.getName(), actualLabel.getName());
    }

    @Test
    public void getAllLabels() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME);

        var request = get(BASE_URL + LABEL_CONTROLLER_PATH);
        var response =
                utils.perform(request, TEST_USERNAME)
                        .andExpect(status().isOk()).andReturn().getResponse();

        List<Label> labels = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(1, labels.size());
    }

    @Test
    public void getLabelByIdFails() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME);

        Label expectedLabel = labelRepository.findAll().get(0);
        Exception exception = assertThrows(
                Exception.class, () -> utils.perform(get(
                        BASE_URL + LABEL_CONTROLLER_PATH + ID,
                        expectedLabel.getId()))
        );

        String message = exception.getMessage();

        assertTrue(message.contains("No value present"));
    }


    @Test
    public void createLabel() throws Exception {
        assertEquals(0, labelRepository.count());

        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME).andExpect(status().isCreated());

        assertEquals(1, labelRepository.count());
    }

    @Test
    public void createLabelTwiceFails() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME).andExpect(status().isCreated());
        utils.regDefaultLabel(TEST_USERNAME).andExpect(status().isUnprocessableEntity());

        assertEquals(1, labelRepository.count());
    }

    @Test
    public void updateLabel() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME);

        Label label = labelRepository.findAll().get(0);

        var request = put(
                BASE_URL + LABEL_CONTROLLER_PATH + ID,
                label.getId());

        var updateRequest = request
                .content(TestUtils.asJson(TEST_LABEL_2))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TEST_USERNAME);

        assertTrue(labelRepository.existsById(label.getId()));
        assertNotNull(labelRepository.findByName(TEST_LABEL_2.getName()).orElse(null));
        assertNull(labelRepository.findByName(label.getName()).orElse(null));
    }

    @Test
    public void deleteLabel() throws Exception {
        assertEquals(0, labelRepository.count());
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME);

        Long labelId = labelRepository.findAll().get(0).getId();

        var request = delete(
                BASE_URL + LABEL_CONTROLLER_PATH + ID, labelId);
        utils.perform(request, TEST_USERNAME).andExpect(status().isOk());

        assertEquals(0, labelRepository.count());
    }

    @Test
    public void deleteLabelFails() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME);

        Long labelId = labelRepository.findAll().get(0).getId() + 1;

        var request = delete(
                BASE_URL + LABEL_CONTROLLER_PATH + ID, labelId);
        utils.perform(request, TEST_USERNAME)
                .andExpect(status().isNotFound());

        assertEquals(1, labelRepository.count());
    }
}
