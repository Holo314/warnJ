package org.holo.warn.compiletime.annotations;

import javax.tools.Diagnostic;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface Warning {
    String message() default  "Component is marked as 'warning', users are advised to use with caution";
    Diagnostic.Kind severity() default Diagnostic.Kind.WARNING;
}
