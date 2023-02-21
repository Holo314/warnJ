package test.org.holo.warn;

import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.holo.warn.compiletime.processor.WarningProcessor;
import org.junit.jupiter.api.Test;
import test.org.holo.warn.testdata.Overrides;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import static javax.tools.Diagnostic.Kind.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class WarningTest {

    @Test
    public void warning()
            throws IOException {
        var testdataLocation = "src/test/java/test/org/holo/warn/testdata/WarnLevel.java";
        var testdata = Files.readString(Path.of(testdataLocation));
        var fullyQualifiedName = "test.org.holo.warn.testdata.WarnLevel";

        var processor = new WarningProcessor();
        var compiler = Compiler.javac()
                               .withProcessors(processor);
        var compiled = compiler.compile(JavaFileObjects.forSourceString(
                fullyQualifiedName,
                testdata
        ));

        compiled.diagnostics().forEach(diagnostic -> {
            if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 21) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 22) {
                assertEquals("Explicit severity and custom message", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == NOTE && diagnostic.getLineNumber() == 23) {
                assertEquals("A note", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == MANDATORY_WARNING && diagnostic.getLineNumber() == 24) {
                assertEquals("o7", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == NOTE && diagnostic.getLineNumber() == 25) {
                assertEquals("\\o/", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else {
                fail(getFailureMessage(diagnostic));
            }
        });
    }

    @Test
    public void override()
            throws IOException {

        var testdata = Overrides.code;
        var fullyQualifiedName = "test.org.holo.warn.testdata.Override";

        var processor = new WarningProcessor();
        var compiler = Compiler.javac()
                               .withProcessors(processor);
        var compiled = compiler.compile(JavaFileObjects.forSourceString(
                fullyQualifiedName,
                testdata
        ));

        compiled.diagnostics().forEach(diagnostic -> {
            if (diagnostic.getKind() == NOTE && diagnostic.getLineNumber() == 23) {
                assertEquals("Override", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == NOTE && diagnostic.getLineNumber() == 29) {
                assertEquals("Override", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == ERROR && diagnostic.getLineNumber() == 40) {
                assertEquals("Overriding", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == NOTE && diagnostic.getLineNumber() == 41) {
                assertEquals("Override", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == ERROR && diagnostic.getLineNumber() == 42) {
                assertEquals("Overriding", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else {
                fail(getFailureMessage(diagnostic));
            }
        });
    }

    @Test
    public void suppress()
            throws IOException {
        var testdataLocation = "src/test/java/test/org/holo/warn/testdata/Suppressing.java";
        var testdata = Files.readString(Path.of(testdataLocation));
        var fullyQualifiedName = "test.org.holo.warn.testdata.Suppressing";

        var processor = new WarningProcessor();
        var compiler = Compiler.javac()
                               .withProcessors(processor);
        var compiled = compiler.compile(JavaFileObjects.forSourceString(
                fullyQualifiedName,
                testdata
        ));

        compiled.diagnostics().forEach(diagnostic -> {
            if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 19) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 20) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 21) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 22) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 28) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 29) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 30) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 35) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 37) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 38) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 43) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 44) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 46) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 54) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else if (diagnostic.getKind() == WARNING && diagnostic.getLineNumber() == 69) {
                assertEquals("Component is marked as 'warning', users are advised to use with caution", diagnostic.getMessage(Locale.ENGLISH), getFailureMessage(diagnostic));
            } else {
                fail(getFailureMessage(diagnostic));
            }
        });
    }

    private static String getFailureMessage(Diagnostic<? extends JavaFileObject> it) {
        return it.getKind() + "(line=" + it.getLineNumber() + ", column=" + it.getColumnNumber() + "): " + it.getMessage(Locale.ENGLISH);
    }
}
