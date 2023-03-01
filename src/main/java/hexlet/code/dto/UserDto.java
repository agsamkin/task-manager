package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "First name should not be empty")
    @Size(min = 1, message = "First name should be greater than 1")
    private String firstName;

    @NotBlank(message = "Last name should not be empty")
    @Size(min = 1, message = "Last name should be greater than 1")
    private String lastName;

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password should not be empty")
    @Size(min = 3, message = "Password should be greater than 3")
    private String password;
}
