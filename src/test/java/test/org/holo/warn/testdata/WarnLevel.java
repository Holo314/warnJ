package test.org.holo.warn.testdata;

import org.holo.warn.compiletime.annotations.Warning;

import javax.tools.Diagnostic;

public class WarnLevel {
    @Warning // default values
    public static void foo() {}

    @Warning(severity = Warning.Kind.WARNING, message = "Explicit severity and custom message")
    public static void bar() {}

    @Warning(severity = Warning.Kind.NOTE, message = "A note")
    public static void qux() {}

    @Warning(severity = Warning.Kind.MANDATORY_WARNING, message = "o7")
    public static void corge() {}

    public static void waldo() {
        foo(); // a warning, with message "Component is marked as 'warning', users are advised to use with caution"
        bar(); // a warning, with message "Explicit severity and custom message"
        qux(); // a note, with message "A note"
        corge(); // a mandatory warning, with message "o7"
        new WarnLevel(3); // an other, with message "\\o/"
    }

    @Warning(severity = Warning.Kind.NOTE, message = "\\o/")
    public WarnLevel(int x) {}
}
