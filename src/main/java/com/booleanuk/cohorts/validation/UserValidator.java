package com.booleanuk.cohorts.validation;

import com.booleanuk.cohorts.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public ValidationError validateExists(User user) {
        if (user == null) {
            return new ValidationError(HttpStatus.NOT_FOUND, "User not found");
        }
        return null;
    }

    public ValidationError validateIsStudent(User user) {
        if (!user.isStudent()) {
            return new ValidationError(
                    HttpStatus.BAD_REQUEST,
                    "Exercises are only available for students"
            );
        }
        return null;
    }

    public ValidationError validateExistingStudent(User user) {
        ValidationError existsError = validateExists(user);
        if (existsError != null) {
            return existsError;
        }

        return validateIsStudent(user);
    }
}
