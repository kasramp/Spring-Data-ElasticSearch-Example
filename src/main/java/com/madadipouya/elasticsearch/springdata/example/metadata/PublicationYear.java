package com.madadipouya.elasticsearch.springdata.example.metadata;

import com.madadipouya.elasticsearch.springdata.example.validator.PublicationYearValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented
@Retention(RUNTIME)
@Target({FIELD, ANNOTATION_TYPE, PARAMETER})
@Constraint(validatedBy = PublicationYearValidator.class)
public @interface PublicationYear {

    String message() default "Publication year cannot be future year";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}