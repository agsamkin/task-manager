package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.service.LabelService;

import lombok.RequiredArgsConstructor;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    public static final String ID = "/{id}";

    private final LabelService labelService;

    @GetMapping(ID)
    public Label getById(@PathVariable("id") long id) {
        return labelService.getLabelById(id);
    }

    @GetMapping
    public List<Label> getAll() {
        return labelService.getAllLabels();
    }

    @PostMapping
    public Label create(@RequestBody @Valid LabelDto labelDto, BindingResult bindingResult) {
        return labelService.createLabel(labelDto);
    }

    @PutMapping(ID)
    public Label update(@PathVariable("id") long id,
                       @RequestBody @Valid LabelDto labelDto, BindingResult bindingResult) {
        return labelService.updateLabel(id, labelDto);
    }

    @DeleteMapping(ID)
    public void delete(@PathVariable("id") long id) {
        labelService.deleteLabel(id);
    }
}
