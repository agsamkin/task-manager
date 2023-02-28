package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.LabelDto;
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
    private TestUtils testUtils;

    @Autowired
    private LabelRepository labelRepository;

    @BeforeEach
    void clear() {
        testUtils.clear();
    }

    @Test
    void getLabelById() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regDefaultLabel(TEST_USERNAME);
        final var expectedLabel = labelRepository.findAll().get(0);

        final var request = get(BASE_URL + LABEL_CONTROLLER_PATH + ID,
                expectedLabel.getId());
        final var response =
                testUtils.perform(request, TEST_USERNAME)
                        .andExpect(status().isOk()).andReturn().getResponse();

        final Label label = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedLabel.getId(), label.getId());
        assertEquals(expectedLabel.getName(), label.getName());
    }

    @Test
    void getAllLabels() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regDefaultLabel(TEST_USERNAME);

        final var request = get(BASE_URL + LABEL_CONTROLLER_PATH);
        final var response =
                testUtils.perform(request, TEST_USERNAME)
                        .andExpect(status().isOk()).andReturn().getResponse();

        List<Label> labels = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(1, labels.size());
    }

    @Test
    void getLabelByIdFails() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regDefaultLabel(TEST_USERNAME);
        final Label expectedLabel = labelRepository.findAll().get(0);
        Exception exception = assertThrows(
                Exception.class, () -> testUtils.perform(get(BASE_URL + LABEL_CONTROLLER_PATH + ID,
                        expectedLabel.getId()))
        );
        String message = exception.getMessage();
        assertTrue(message.contains("No value present"));
    }


    @Test
    void createLabel() throws Exception {
        assertEquals(0, labelRepository.count());
        testUtils.regDefaultUser();
        testUtils.regDefaultLabel(TEST_USERNAME).andExpect(status().isCreated());
        assertEquals(1, labelRepository.count());
    }

    @Test
    void createLabelTwiceFails() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regDefaultLabel(TEST_USERNAME).andExpect(status().isCreated());
        testUtils.regDefaultLabel(TEST_USERNAME).andExpect(status().isUnprocessableEntity());
        assertEquals(1, labelRepository.count());
    }

    @Test
    void updateLabel() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regDefaultLabel(TEST_USERNAME);

        final Label label = labelRepository.findAll().get(0);

        final var request = put(BASE_URL + LABEL_CONTROLLER_PATH + ID,
                label.getId());

        LabelDto labelDto = LabelDto.builder()
                .name("new label").build();

        final var updateRequest = request
                .content(TestUtils.asJson(labelDto))
                .contentType(APPLICATION_JSON);

        testUtils.perform(updateRequest, TEST_USERNAME);

        assertTrue(labelRepository.existsById(label.getId()));
        assertNotNull(labelRepository.findByName(labelDto.getName()).orElse(null));
        assertNull(labelRepository.findByName(label.getName()).orElse(null));
    }

    @Test
    void deleteLabel() throws Exception {
        assertEquals(0, labelRepository.count());
        testUtils.regDefaultUser();
        testUtils.regDefaultLabel(TEST_USERNAME);

        final Long labelId = labelRepository.findAll().get(0).getId();
        final var request = delete(BASE_URL + LABEL_CONTROLLER_PATH + ID, labelId);
        testUtils.perform(request, TEST_USERNAME)
                .andExpect(status().isOk());
        assertEquals(0, labelRepository.count());
    }

    @Test
    void deleteLabelFails() throws Exception {
        testUtils.regDefaultUser();
        testUtils.regDefaultLabel(TEST_USERNAME);
        final Long labelId = labelRepository.findAll().get(0).getId() + 1;
        final var request = delete(BASE_URL + LABEL_CONTROLLER_PATH + ID, labelId);
        testUtils.perform(request, TEST_USERNAME)
                .andExpect(status().isNotFound());
        assertEquals(1, labelRepository.count());
    }

}
