package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabelDto {
    @NotBlank(message = "Name should not be empty")
    @Size(min = 2, message = "Name should be greater than 1")
    private String name;
}
