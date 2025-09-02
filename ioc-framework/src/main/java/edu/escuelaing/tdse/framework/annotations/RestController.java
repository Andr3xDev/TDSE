package edu.escuelaing.tdse.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation is used to mark a class as a RESTful web controller,
 * allowing it to handle HTTP requests in the framework.
 * </p>
 * <p>
 * Classes annotated with {@code @RestController} are detected and registered
 * by the framework at runtime.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RestController {
}