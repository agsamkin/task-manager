package hexlet.code.exception.custom;

import javax.persistence.EntityNotFoundException;

public class TaskStatusNotFoundException extends EntityNotFoundException {
    public TaskStatusNotFoundException(String message) {
        super(message);
    }
}
