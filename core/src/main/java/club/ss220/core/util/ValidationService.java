package club.ss220.core.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

@Service
public class ValidationService {

    private final Validator validator;

    public ValidationService() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public <T> T validate(T value) {
        Set<ConstraintViolation<T>> violations = validator.validate(value);
        if (!violations.isEmpty()) {
            String details = Arrays.toString(violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .toArray(String[]::new));
            String message = "Object of type " + value.getClass().getName() + " failed validation: " + details;
            throw new ConstraintViolationException(message, violations);
        }
        return value;
    }
}
