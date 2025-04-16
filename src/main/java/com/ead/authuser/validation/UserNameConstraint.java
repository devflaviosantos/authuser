package com.ead.authuser.validation;

import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserNameConstraintImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserNameConstraint {
    String message() default "Invalid user name";
    Class<?>[] groups() default {};
    Class <? extends Payload>[] payload() default {};
}
