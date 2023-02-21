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
    Kind severity() default Kind.WARNING;

    enum Kind {
        /**
         * Problem which prevents the tool's normal completion.
         */
        ERROR(Diagnostic.Kind.ERROR),
        /**
         * Problem which does not usually prevent the tool from
         * completing normally.
         */
        WARNING(Diagnostic.Kind.WARNING),
        /**
         * Problem similar to a warning, but is mandated by the tool's
         * specification.  For example, the Java&trade; Language
         * Specification mandates warnings on certain
         * unchecked operations and the use of deprecated methods.
         */
        MANDATORY_WARNING(Diagnostic.Kind.MANDATORY_WARNING),
        /**
         * Informative message from the tool.
         */
        NOTE(Diagnostic.Kind.NOTE);
        public final Diagnostic.Kind unwrap;

        Kind(Diagnostic.Kind kind) {
            unwrap = kind;
        }
    }
}
