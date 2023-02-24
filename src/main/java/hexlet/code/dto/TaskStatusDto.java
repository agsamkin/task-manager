package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusDto {
    private Long id;

    @NotBlank(message = "Name should not be empty")
    @Size(min = 2, message = "Name should be greater than 1")
    private String name;

    private Date createdAt;
}
