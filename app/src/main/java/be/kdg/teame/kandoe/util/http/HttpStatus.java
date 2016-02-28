package be.kdg.teame.kandoe.util.http;

public interface HttpStatus {

    // section 2xx
    int OK = 200;
    int CREATED = 201;

    // section 4xx
    int BAD_REQUEST = 400;
    int UNAUTHORIZED = 401;
    int FORBIDDEN = 403;
    int UNPROCESSABLE_ENTITY = 422;

    // section 5xx
    int INTERNAL_SERVER_ERROR = 500;
}
