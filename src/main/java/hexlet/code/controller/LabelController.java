package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.service.LabelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "label-controller", description = "Label crud")
@RequiredArgsConstructor
@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    public static final String ID = "/{id}";

    private final LabelService labelService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get label by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label was found"),
            @ApiResponse(responseCode = "404", description = "Label with this id wasn`t found")
    })
    @GetMapping(ID)
    public Label getById(@PathVariable("id") long id) {
        return labelService.getLabelById(id);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get all labels")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public List<Label> getAll() {
        return labelService.getAllLabels();
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create new label")
    @ApiResponse(responseCode = "201", description = "Label has been created")
    @ResponseStatus(CREATED)
    @PostMapping
    public Label create(@RequestBody @Valid LabelDto labelDto) {
        return labelService.createLabel(labelDto);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Update label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label has been updated"),
            @ApiResponse(responseCode = "404", description = "Label with this id wasn`t found")
    })
    @PutMapping(ID)
    public Label update(@PathVariable("id") long id,
                       @RequestBody @Valid LabelDto labelDto) {
        return labelService.updateLabel(id, labelDto);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label has been deleted"),
            @ApiResponse(responseCode = "404", description = "Label with this id wasn`t found")
    })
    @DeleteMapping(ID)
    public void delete(@PathVariable("id") long id) {
        labelService.deleteLabel(id);
    }
}
