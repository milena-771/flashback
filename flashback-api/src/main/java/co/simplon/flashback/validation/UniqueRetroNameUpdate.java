package co.simplon.flashback.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Constraint(validatedBy = UniqueRetroNameUpdateValidator.class)
public @interface UniqueRetroNameUpdate {

    String message() default "This retrospective name already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
