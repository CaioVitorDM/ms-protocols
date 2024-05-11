package ufrn.imd.br.msprotocols.utils.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ufrn.imd.br.msprotocols.dto.ApiResponseDTO;
import ufrn.imd.br.msprotocols.utils.exception.BusinessException;
import ufrn.imd.br.msprotocols.utils.exception.ConversionException;
import ufrn.imd.br.msprotocols.utils.exception.ErrorDTO;
import ufrn.imd.br.msprotocols.utils.exception.ResourceNotFoundException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for controllers in the application.
 * Handles various types of exceptions and maps them to appropriate error
 * responses.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    private static final String MSG_ERRO = "Error: ";

    /**
     * Handles BusinessException and maps it to a custom error response.
     *
     * @param exception The BusinessException instance.
     * @param request   The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> businessException(BusinessException exception,
                                                                      HttpServletRequest request) {

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                exception.getHttpStatusCode().value(),
                "Business error",
                exception.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO<ErrorDTO>(
                false,
                MSG_ERRO + exception.getMessage(),
                null,
                err));
    }

    /**
     * Handles ResourceNotFoundException and maps it to a custom error response.
     *
     * @param exception The ResourceNotFoundException instance.
     * @param request   The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> notFound(ResourceNotFoundException exception,
            HttpServletRequest request) {

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                exception.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO<ErrorDTO>(
                false,
                MSG_ERRO + exception.getMessage(),
                null,
                err));
    }

    /**
     * Handles ConversionException and maps it to a custom error response.
     *
     * @param exception The ConversionException instance.
     * @param request   The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(ConversionException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> conversionException(ConversionException exception,
            HttpServletRequest request) {

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected problem occurred while converting data.",
                exception.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDTO<ErrorDTO>(
                false,
                MSG_ERRO + exception.getMessage(),
                null,
                err));

    }

    /**
     * Handles TransactionSystemException and maps it to a custom error response.
     *
     * @param ex      The TransactionSystemException instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler({ TransactionSystemException.class })
    protected ResponseEntity<ApiResponseDTO<ErrorDTO>> handlePersistenceException(Exception ex,
            HttpServletRequest request) {
        logger.info(ex.getClass().getName());

        Throwable cause = ((TransactionSystemException) ex).getRootCause();
        if (cause instanceof ConstraintViolationException consEx) {
            final List<String> errors = new ArrayList<>();
            for (final ConstraintViolation<?> violation : consEx.getConstraintViolations()) {
                errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
            }

            final var err = new ErrorDTO(
                    ZonedDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Erro ao salvar dados.",
                    errors.toString(),
                    request.getRequestURI());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDTO<ErrorDTO>(
                    false,
                    MSG_ERRO + ex.getMessage(),
                    null,
                    err));
        }
        return internalErrorException(ex, request);
    }

    /**
     * Handles any other unexpected exception and maps it to a generic error
     * response.
     *
     * @param e       The unexpected Exception instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> internalErrorException(Exception e, HttpServletRequest request) {

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected problem occurred.",
                e.getMessage(),
                request.getRequestURI());

        logger.error("An unexpected problem occurred. ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDTO<ErrorDTO>(
                false,
                MSG_ERRO + e.getMessage(),
                null,
                err));
    }
}
