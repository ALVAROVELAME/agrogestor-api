package com.agrogestor.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/** BCrypt aceita no máximo 72 bytes UTF-8, não 72 caracteres. */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BcryptPasswordValidator.class)
public @interface BcryptPassword {
    String message() default "Senha deve ter no máximo 72 bytes UTF-8";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
