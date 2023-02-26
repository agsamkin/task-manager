package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;

import java.util.List;

public interface LabelService {
    List<Label> getAllLabels();

    Label getLabelById(long id);

    Label createLabel(LabelDto labelDto);

    Label updateLabel(long id, LabelDto labelDto);

    void deleteLabel(long id);
}
