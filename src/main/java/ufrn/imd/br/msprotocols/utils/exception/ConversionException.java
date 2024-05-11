package ufrn.imd.br.msprotocols.utils.exception;

/**
 * Exception thrown when problem occurs during data conversion.
 * It extends RuntimeException to indicate an unchecked exception.
 */
public class ConversionException extends RuntimeException {

    /**
     * Constructs a new ConversionException with the specified detail message.
     *
     * @param message the detail message.
     */
    public ConversionException(String message) {
        super(message);
    }

}
