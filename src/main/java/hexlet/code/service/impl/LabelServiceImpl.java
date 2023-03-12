package hexlet.code.service.impl;

import hexlet.code.dto.LabelDto;
import hexlet.code.exception.custom.LabelNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;

    @Override
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @Override
    public Label getLabelById(long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new LabelNotFoundException("Label not found"));
    }

    @Override
    public Label createLabel(LabelDto labelDto) {
        Label newLabel = Label.builder()
                .name(labelDto.getName()).build();
        return labelRepository.save(newLabel);
    }

    @Override
    public Label updateLabel(long id, LabelDto labelDto) {
        Label label = getLabelById(id);
        label.setName(labelDto.getName());
        return labelRepository.save(label);
    }

    @Override
    public void deleteLabel(long id) {
        Label label = getLabelById(id);
        labelRepository.delete(label);
    }
}
