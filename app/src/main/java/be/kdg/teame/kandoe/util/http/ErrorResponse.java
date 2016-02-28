package be.kdg.teame.kandoe.util.http;

import java.util.ArrayList;
import java.util.List;

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

    public class FieldError {
        private final String message;
        private final String field;

        public FieldError(String message, String field) {
            this.message = message;
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public String getField() {
            return field;
        }
    }
}
