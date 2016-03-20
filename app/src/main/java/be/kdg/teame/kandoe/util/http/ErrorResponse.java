package be.kdg.teame.kandoe.util.http;

import java.util.ArrayList;
import java.util.List;

import lombok.Value;

/**
 * Holds a list containing one or more {@link FieldError}s.
 */
public class ErrorResponse {
    private List<FieldError> fieldErrors;

    public ErrorResponse() {
        this.fieldErrors = new ArrayList<>();
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    /**
     * Class can hold an error with a field an the message itself.
     */
    @Value
    public class FieldError {
        private String message;
        private String field;
    }
}
