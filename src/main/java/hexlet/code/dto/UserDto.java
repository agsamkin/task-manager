package hexlet.code.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "First name should not be empty")
    @Size(min = 2, message = "First name should be greater than 1")
    private String firstName;

    @NotBlank(message = "Last name should not be empty")
    @Size(min = 2, message = "Last name should be greater than 1")
    private String lastName;

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    private Date createdAt;
}
