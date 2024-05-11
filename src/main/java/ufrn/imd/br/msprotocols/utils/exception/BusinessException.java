package ufrn.imd.br.msprotocols.utils.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception representing a business logic error in the application.
 * It extends RuntimeException to indicate an unchecked exception.
 */
public class BusinessException extends RuntimeException {

    private final HttpStatus httpStatusCode;

    /**
     * Constructs a new BusinessException with the specified detail message and HTTP
     * status code.
     *
     * @param message    the detail message.
     * @param statusCode the HTTP status code associated with the error.
     */
    public BusinessException(String message, HttpStatus statusCode) {
        super(message);
        this.httpStatusCode = statusCode;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }
}
