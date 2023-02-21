package org.holo.warn.compiletime.processor;

import com.google.auto.service.AutoService;
import com.sun.source.tree.*;
import com.sun.source.util.*;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeInfo;
import org.holo.warn.compiletime.annotations.Warning;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SupportedAnnotationTypes({"org.holo.compiletime.annotations.Warning"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService({Processor.class})
public class WarningProcessor
        // It is easier to work with Annotation Processors rather than Plugin
        extends AbstractProcessor
        implements TaskListener {
    private Trees trees;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        var unwrapped = jbUnwrapProcessingEnvironment(processingEnv);
        trees = Trees.instance(unwrapped);
        JavacTask.instance(unwrapped).addTaskListener(this);
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

    @Override
    public void started(TaskEvent taskEvt) {}

    @Override
    public void finished(TaskEvent taskEvt) {
        if (taskEvt.getKind() != TaskEvent.Kind.ANALYZE) {
            return;
        }
        var root = taskEvt.getCompilationUnit();
        taskEvt.getCompilationUnit().accept(new TreeScanner<Void, Void>() {
            @Override
            public Void visitMethodInvocation(MethodInvocationTree methodInv, Void v) {
                var methodIdentity = (JCTree)methodInv.getMethodSelect();
                var method = (Symbol.MethodSymbol)TreeInfo.symbol(methodIdentity);
                if (method == null) return super.visitMethodInvocation(methodInv, v);
                generateNotes(trees, root, methodInv, methodIdentity, method);
                return super.visitMethodInvocation(methodInv, v);
            }

            @Override
            public Void visitNewClass(NewClassTree newClassTree, Void v) {
                var methodIdentity = ((JCTree.JCNewClass)newClassTree).getIdentifier();
                var method = (Symbol.MethodSymbol)((JCTree.JCNewClass)newClassTree).constructor;
                generateNotes(trees, root, newClassTree, methodIdentity, method);
                return super.visitNewClass(newClassTree, v);
            }
        }, null);
    }

    private static void generateNotes(
            Trees trees,
            CompilationUnitTree root,
            Tree tree,
            JCTree identity,
            Symbol.MethodSymbol invokedMethod
    ) {
        var warning = invokedMethod.getAnnotation(Warning.class);
        if (warning == null) return;
        var enclosingClass = getEnclosingOfTree(trees.getPath(root, tree), JCTree.JCClassDecl.class);
        var enclosingMethod = getEnclosingOfTree(trees.getPath(root, tree), JCTree.JCMethodDecl.class);
        var enclosingVariable = getEnclosingOfTree(trees.getPath(root, tree), JCTree.JCVariableDecl.class);

        if ((enclosingClass != null
                && isSuppressingCall(identity.toString(), invokedMethod.toString(), enclosingClass.getModifiers()))
                ||
                (enclosingMethod != null
                        && isSuppressingCall(identity.toString(), invokedMethod.toString(), enclosingMethod.getModifiers()))
                ||
                (enclosingVariable != null
                        && isSuppressingCall(identity.toString(), invokedMethod.toString(), enclosingVariable.getModifiers()))) {
            return;
        }

        trees.printMessage(warning.severity().unwrap, warning.message(), identity, root);
    }


    /**
     * Check if the enclosing method contains {@code @SuppressWarning("Warning.*")}
     * <p>
     * or {@code @SuppressWarning("Warning." + methodName)} (e.g. {@code @SuppressWarning("Warning.foo")})
     * <p>
     * or {@code @SuppressWarning("Warning." + invokedMethod)} (e.g.  {@code @SuppressWarning("Warning.foo(int)")})
     */
    private static boolean isSuppressingCall(String methodName, String invokedMethod, ModifiersTree modifiers) {
        var enclosingSuppress = modifiers.getAnnotations().stream()
                                         .filter(ann -> ann.getAnnotationType()
                                                           .toString()
                                                           .equals(SuppressWarnings.class.getSimpleName()))
                                         .findFirst()
                                         .orElse(null);

        if (enclosingSuppress == null) {
            return false;
        }

        var suppressing = enclosingSuppress.getArguments().stream()
                                           .filter(arg -> arg instanceof JCTree.JCAssign)
                                           .map(JCTree.JCAssign.class::cast)
                                           .filter(assign -> assign.lhs.toString()
                                                                       .equals("value"))
                                           .map(assign -> assign.rhs)
                                           .findFirst()
                                           .stream()
                                           .flatMap(suppressed -> {
                                               if (suppressed instanceof JCTree.JCNewArray) {
                                                   return Stream.of(suppressed)
                                                                .map(JCTree.JCNewArray.class::cast)
                                                                .map(JCTree.JCNewArray::getInitializers)
                                                                .flatMap(Collection::stream);
                                               } else {
                                                   return Stream.of(suppressed);
                                               }
                                           })
                                           .map(JCTree.JCLiteral.class::cast)
                                           .map(JCTree.JCLiteral::getValue)
                                           .map(Object::toString)
                                           .filter(suppressed -> suppressed.startsWith("Warning."))
                                           .map(suppressed -> suppressed.replaceFirst("Warning[.]", ""))
                                           .collect(Collectors.toSet());
        return suppressing.contains("*")
                || suppressing.contains(methodName)
                || suppressing.contains(invokedMethod);
    }

    /**
     * @return the current's context method. If non found return {@code null}
     */
    private static <T> T getEnclosingOfTree(TreePath path, Class<T> type) {
        Tree leaf;
        while (!((leaf = path.getLeaf()).getClass().equals(type))) {
            path = path.getParentPath();
            if (path == null) {
                return null;
            }
        }

        return type.cast(leaf);
    }

    /**
     * this method was provided by JetBrains 2021.3.3 to unwrap "processingEnv". Do not touch.
     */
    @SuppressWarnings({"DuplicatedCode"})
    private static ProcessingEnvironment jbUnwrapProcessingEnvironment(ProcessingEnvironment wrapper) {
        ProcessingEnvironment unwrapped = null;
        try {
            final Class<?> apiWrappers =
                    wrapper.getClass().getClassLoader().loadClass("org.jetbrains.jps.javac.APIWrappers");
            final Method unwrapMethod = apiWrappers.getDeclaredMethod("unwrap", Class.class, Object.class);
            unwrapped = (ProcessingEnvironment)unwrapMethod.invoke(null, ProcessingEnvironment.class, wrapper);
        } catch (Throwable ignored) {
        }
        return unwrapped != null ? unwrapped : wrapper;
    }
}
