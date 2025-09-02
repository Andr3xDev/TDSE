package edu.escuelaing.tdse.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for mapping HTTP POST requests onto specific handler methods.
 * <p>
 * This annotation is used to indicate that the annotated method should handle
 * HTTP POST requests
 * for the specified URI path.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostMapping {

    String value();

}