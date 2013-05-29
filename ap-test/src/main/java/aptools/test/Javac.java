package aptools.test;

import com.google.common.io.Files;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.Lists.*;

/**
 * A wrapper around the Java 6 javax.tools.JavaCompiler for quick on-the-fly compilation.
 * Give it some source, and you get back a class loader ready to create instances of the
 * freshly minted classes.
 */
public class Javac {
    //
    // TODO - consider spending the time to utilize a memory stream to avoid interaction with
    // the file system altogether.
    //
    private final File outputFolder = Files.createTempDir();
    private final JavaCompiler compiler= ToolProvider.getSystemJavaCompiler();
    private final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
    private final List<JavaFileObject> fileObjects = newArrayList();
    private final List<String> annotationProcessors = newArrayList();

    private final StandardJavaFileManager fileManager = compiler.getStandardFileManager(
            diagnostics,
            Locale.ENGLISH,
            Charset.defaultCharset());

    public Javac addSource(JavaFileObject obj) {
        fileObjects.add(obj);
        return this;
    }

    public Javac addAnnotationProcessor(Class<?> clazz) {
        annotationProcessors.add(clazz.getName());
        return this;
    }

    public ClassLoader compile(DiagnosticAction action) {
        JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                fileManager,
                diagnostics,
                Arrays.asList("-d", outputFolder.getPath()),
                annotationProcessors,
                fileObjects);

        task.call();
        action.run(diagnostics.getDiagnostics());
        return newClassLoader();
    }

    private ClassLoader newClassLoader() {
        try {
            return new URLClassLoader(new URL[] { outputFolder.toURI().toURL() });
        } catch (Exception e) {
            throw propagate(e);
        }
    }
}
