package test.org.holo.warn.testdata;

public class Overrides {
    // This can't be code because it won't compile (It contains usage of methods classified as "severity = Diagnostic.Kind.ERROR")
    public static final String code =
            "package test.org.holo.warn.testdata;\n" +
            "\n" +
            "import org.holo.warn.compiletime.annotations.Warning;\n" +
            "\n" +
            "import javax.tools.Diagnostic;\n" +
            "\n" +
            "public abstract class Override {\n" +
            "    @Warning(severity = Diagnostic.Kind.NOTE, message = \"Override\")\n" +
            "    public void foo(){}\n" +
            "\n" +
            "    @Warning(severity = Diagnostic.Kind.NOTE, message = \"Override\")\n" +
            "    public void bar(){}\n" +
            "\n" +
            "    @Warning(severity = Diagnostic.Kind.NOTE, message = \"Override\")\n" +
            "    public void corge(){}\n" +
            "\n" +
            "    @Warning(severity = Diagnostic.Kind.NOTE, message = \"Override\")\n" +
            "    abstract void qux();\n" +
            "\n" +
            "    public static class Overriding extends Override {\n" +
            "        @java.lang.Override\n" +
            "        public void foo() {\n" +
            "            super.foo();\n" +
            "        }\n" +
            "\n" +
            "        @Warning(severity = Diagnostic.Kind.ERROR, message = \"Overriding\")\n" +
            "        @java.lang.Override\n" +
            "        public void bar() {\n" +
            "            super.bar();\n" +
            "        }\n" +
            "\n" +
            "        @Warning(severity = Diagnostic.Kind.ERROR, message = \"Overriding\")\n" +
            "        @java.lang.Override\n" +
            "        void qux() {}\n" +
            "    }\n" +
            "\n" +
            "    public static void main(String[] args) {\n" +
            "        var x = new Overriding();\n" +
            "        x.foo();\n" +
            "        x.bar();\n" +
            "        x.corge();\n" +
            "        x.qux();\n" +
            "    }\n" +
            "}\n";
}
