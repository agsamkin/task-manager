package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.TIMESTAMP;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name should not be empty")
    @Size(min = 1, message = "First name should be greater than 1")
    @Column(name = "firstName")
    private String firstName;

    @NotBlank(message = "Last name should not be empty")
    @Size(min = 1, message = "Last name should be greater than 1")
    @Column(name = "lastName")
    private String lastName;

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    @Column(name = "email", unique = true)
    private String email;

    @JsonIgnore
    @NotBlank(message = "Password should not be empty")
    @Size(min = 3, message = "Password should be greater than 3")
    @Column(name = "password")
    private String password;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @Column(name = "createdAt")
    private Date createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "author")
    private List<Task> authorTasks;

    @JsonIgnore
    @OneToMany(mappedBy = "executor")
    private List<Task> executorTasks;
}
