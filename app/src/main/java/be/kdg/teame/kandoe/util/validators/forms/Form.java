package be.kdg.teame.kandoe.util.validators.forms;

import java.util.ArrayList;
import java.util.List;

public class Form {
    List<FormField> formFields;

    public Form() {
        this.formFields = new ArrayList<>();
    }

    public void add(FormField formField) {
        this.formFields.add(formField);
    }

    public boolean validate() {
        boolean valid = true;

        for (FormField formField : formFields)
            if (!formField.validate() && valid)
                valid = false;

        return valid;
    }
}
