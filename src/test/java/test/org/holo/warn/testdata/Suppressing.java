package test.org.holo.warn.testdata;

import org.holo.warn.compiletime.annotations.Warning;
// On the methods definition caller site
public class Suppressing {
    @Warning
    public static int foo() {return 666;}

    @Warning
    public static void foo(int x) {}

    @Warning
    public static void foo(Suppressing x) {}

    @Warning
    public static void bar() {}

    public static void qux() {
        foo(); // warning
        foo(3); // warning
        foo(new Suppressing()); // warning
        bar(); // warning
    }

    @SuppressWarnings("Warning.foo()")
    public static void corge() {
        foo();
        foo(3); // warning
        foo(new Suppressing()); // warning
        bar(); // warning
    }

    @SuppressWarnings("Warning.foo(int)")
    public static void grault() {
        foo(); // warning
        foo(3);
        foo(new Suppressing()); // warning
        bar(); // warning
    }

    @SuppressWarnings("Warning.foo(test.org.holo.warn.testdata.Suppressing)")
    public static void garply() {
        foo(); // warning
        foo(3); // warning
        foo(new Suppressing());
        bar(); // warning
    }

    @SuppressWarnings("Warning.foo")
    public static void waldo() {
        foo();
        foo(3);
        foo(new Suppressing());
        bar(); // warning
    }

    @SuppressWarnings("Warning.*")
    public static void plugh() {
        foo();
        foo(3);
        foo(new Suppressing());
        bar();
    }
}

// On the variable declaration caller site
class hogera {
    public static void hoge() {
        var fuga = Suppressing.foo(); // warning
        @SuppressWarnings("Warning.foo()")
        var piyo = Suppressing.foo();
    }
}

// On the class definition caller site
@SuppressWarnings("Warning.foo()")
class toto {
    public static void tutu() {
        var tutu = Suppressing.foo();
    }
}