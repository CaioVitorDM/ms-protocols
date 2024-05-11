package ufrn.imd.br.msprotocols.mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.imd.br.msprotocols.utils.exception.ConversionException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Utility class for converting one object (source) to another object (target)
 * using reflection.
 * Provides a static method to parse and copy fields with matching names and
 * types from source to target.
 */
public class Parser {

    private Parser() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    /**
     * Parses the source object and creates a new instance of the target class,
     * copying matching fields.
     *
     * @param <T>         The type of the source object.
     * @param <U>         The type of the target object.
     * @param source      The source object to be parsed.
     * @param targetClass The class of the target object.
     * @return The target object with copied fields.
     * @throws ConversionException if there is an error during the conversion
     *                             process.
     */
    public static <T, U> U parse(T source, Class<U> targetClass) throws ConversionException {
        U target;
        try {
            target = targetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException
                 | InvocationTargetException | NoSuchMethodException e) {

            logger.error(e.getMessage());
            throw new ConversionException("Erro ao converter dado");
        }

        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = targetClass.getDeclaredFields();

        for (Field sourceField : sourceFields) {
            for (Field targetField : targetFields) {
                if (sourceField.getName().equals(targetField.getName()) &&
                        sourceField.getType().equals(targetField.getType())) {
                    sourceField.setAccessible(true);
                    targetField.setAccessible(true);

                    try {
                        targetField.set(target, sourceField.get(source));
                    } catch (IllegalAccessException e) {
                        logger.error(e.getMessage());
                        throw new ConversionException("Erro ao converter dado");
                    }
                }
            }
        }
        return target;
    }
}
