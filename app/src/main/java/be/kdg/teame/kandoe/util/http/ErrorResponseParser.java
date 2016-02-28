package be.kdg.teame.kandoe.util.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ErrorResponseParser {
    public static ErrorResponse parseErrorResponseJson(String json) throws JSONException {
        ErrorResponse errorResponse = new ErrorResponse();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonFieldErrorsArray = jsonObject.getJSONArray("fieldErrors");

        if (jsonFieldErrorsArray.length() == 0)
            throw new JSONException("No field errors found");

        for (int i = 0; i < jsonFieldErrorsArray.length(); i++) {
            JSONObject jsonFieldErrorObject = jsonFieldErrorsArray.getJSONObject(i);

            String message = jsonFieldErrorObject.getString("message");
            String field = jsonFieldErrorObject.getString("field");

            ErrorResponse.FieldError fieldError = errorResponse.new FieldError(message, field);
            errorResponse.getFieldErrors().add(fieldError);
        }

        return errorResponse;
    }
}
