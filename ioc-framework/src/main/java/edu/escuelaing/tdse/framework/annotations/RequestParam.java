package edu.escuelaing.tdse.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to indicate that a method parameter should be bound to a web
 * request parameter.
 * <p>
 * Used in controller methods to extract query parameters from the request.
 * </p>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {

    String value();

    String defaultValue();

}