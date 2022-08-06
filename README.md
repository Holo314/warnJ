# warnJ

Adds a warning annotation that gives compiletime hints and errors:

[Example](src/test/java/test/org/holo/warn/testdata/WarnLevel.java):

```java
@Warning // default values are 
// severity = Diagnostic.Kind.WARNING 
// message = "Component is marked as 'warning', users are advised to use with caution"
public static void foo(){}

@Warning(severity = Diagnostic.Kind.WARNING, message = "Explicit severity and custom message")
public static void bar(){}

@Warning(severity = Diagnostic.Kind.NOTE, message = "A note")
public static void qux(){}

@Warning(severity = Diagnostic.Kind.MANDATORY_WARNING, message = "o7")
public static void corge(){}

public static void main(String[]args){
    foo(); // a warning, with message "Component is marked as 'warning', users are advised to use with caution"
    bar(); // a warning, with message "Explicit severity and custom message"
    qux(); // a note, with message "A note"
    corge(); // a mandatory warning, with message "o7"
}
```

The possible severities are those
that [javax.tools.Diagnostic.Kind](https://docs.oracle.com/en/java/javase/11/docs/api/java.compiler/javax/tools/Diagnostic.Kind.html)
defines:

### `Diagnostic.Kind.ERROR`

Problem which prevents the tool's normal completion.

### `Diagnostic.Kind.WARNING`

Problem which does not usually prevent the tool from completing normally.

### `Diagnostic.Kind.MANDATORY_WARNING`

Problem similar to a warning, but is mandated by the tool's specification.

For example, the Java&trade; Language Specification mandates warnings on certain unchecked operations and the use of
deprecated methods.

### `Diagnostic.Kind.NOTE`

Informative message from the tool.

### `Diagnostic.Kind.OTHER`

Diagnostic which does not fit within the other kinds.

---

### Suppressing `@Warning`
Sometimes a method will be annotated with `@Warning`, but the call site decides to ignore the annotation (be it `WARNING`, `ERROR`, or any other).

It is possible to opt-out of any note that the annotation gives using the [`@SuppressWarnings` annotation](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/SuppressWarnings.html).

[Example](src/test/java/test/org/holo/warn/testdata/Suppressing.java):
```java
@SuppressWarnings("Warning.foo(int)")
public static void grault() {
    foo(); // warning
    foo(3);
    foo(new Suppressing()); // warning
    bar(); // warning
}
```
This suppression will work on Variable Declaration, Method Definition and Class Definition.

There are 3 levels of suppression:

#### Global
Any clause with `@SuppressWarnings("Warning.*")` will suppress any and all invocation of methods `@Warning` annotation.

### Named
Any clause with `@SuprressWarnings("Warning.<Name>")` will ignore any and all invocations of methods with the name `<Name>`.

For constructors `<Name>` is the class name.

### Specific
Any clause with `@SuprressWarnings("Warning.<Name>(<Type0>,<Type1>,...)")` will ignore any and all invocations of methods with the name `<Name>` and has parameters of type `<Type0>,<Type1>,...`.

`<Type_i>` is the canonical of the type

For constructors `<Name>` is the class name.