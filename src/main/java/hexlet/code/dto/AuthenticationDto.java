package hexlet.code.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class AuthenticationDto {
    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;
    private String password;
}
