package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @NotBlank(message = "Name should not be empty")
    @Size(min = 1, message = "Name should be greater than 1")
    private String name;

    private String description;

    @NotNull(message = "Task status should not be empty")
    private Long taskStatusId;

    private Long executorId;

    private List<Long> labelIds;
}
