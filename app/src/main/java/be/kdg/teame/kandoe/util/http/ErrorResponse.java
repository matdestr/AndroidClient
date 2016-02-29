package be.kdg.teame.kandoe.util.http;

import java.util.ArrayList;
import java.util.List;

import lombok.Value;

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

    @Value
    public class FieldError {
        private String message;
        private String field;
    }
}
