package test.org.holo.warn.testdata;

public class Overrides {
    // This can't be code because it won't compile (It contains usage of methods classified as "severity = Diagnostic.Kind.ERROR")
    public static final String code =
            """
                    package test.org.holo.warn.testdata;

                    import org.holo.warn.compiletime.annotations.Warning;

                    import javax.tools.Diagnostic;

                    public abstract class Override {
                        @Warning(severity = Warning.Kind.NOTE, message = "Override")
                        public void foo(){}

                        @Warning(severity = Warning.Kind.NOTE, message = "Override")
                        public void bar(){}

                        @Warning(severity = Warning.Kind.NOTE, message = "Override")
                        public void corge(){}

                        @Warning(severity = Warning.Kind.NOTE, message = "Override")
                        abstract void qux();

                        public static class Overriding extends Override {
                            @java.lang.Override
                            public void foo() {
                                super.foo();
                            }

                            @Warning(severity = Warning.Kind.ERROR, message = "Overriding")
                            @java.lang.Override
                            public void bar() {
                                super.bar();
                            }

                            @Warning(severity = Warning.Kind.ERROR, message = "Overriding")
                            @java.lang.Override
                            void qux() {}
                        }

                        public static void main(String[] args) {
                            var x = new Overriding();
                            x.foo();
                            x.bar();
                            x.corge();
                            x.qux();
                        }
                    }
                    """;
}
