package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AuthenticationDto {
    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;
    private String password;
}
