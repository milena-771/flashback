package co.simplon.flashback.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PosterSizeValidator.class)
@Documented
public @interface PosterSize {

    String message() default "File size must be less or equals 200KB";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    long maxSizeInKB() default 200;
}