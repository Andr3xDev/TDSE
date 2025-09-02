package edu.escuelaing.tdse.framework.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for mapping HTTP GET requests onto specific handler methods.
 * <p>
 * The value indicates the URI path to be mapped to the annotated method.
 * This annotation should be used on methods that handle GET requests in a web
 * framework.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetMapping {

    String value();

}