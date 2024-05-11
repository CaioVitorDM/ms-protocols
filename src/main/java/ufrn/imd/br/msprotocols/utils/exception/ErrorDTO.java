package ufrn.imd.br.msprotocols.utils.exception;

import java.time.ZonedDateTime;

/**
 * A data transfer object (DTO) to represent error responses in the application.
 * It encapsulates details about an error, including timestamp, status, error
 * type, message, and request path.
 * This record is immutable and serves to provide a standardized error response
 * structure.
 *
 * @param timestamp The date and time when the error occurred (ZonedDateTime).
 * @param status    The HTTP status code associated with the error.
 * @param error     The type or category of the error.
 * @param message   A detailed error message describing the issue.
 * @param path      The URI path that triggered the error.
 */
public record ErrorDTO(ZonedDateTime timestamp, Integer status, String error, String message, String path) {
}
