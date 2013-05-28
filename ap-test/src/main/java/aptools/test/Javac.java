package aptools.test;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.List;

import static com.google.common.collect.Lists.*;

public class Javac {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();


    List<JavaFileObject> fileObjects = newArrayList();

    public Javac addSource(JavaFileObject obj) {
        fileObjects.add(obj);
        return this;
    }

    public void compile(DiagnosticAction action) {
        JavaCompiler.CompilationTask task = compiler.getTask(
                null,               // Writer
                null,               // FileManager
                diagnostics,        // DiagnosticListener
                null,               // Options
                null,               // Classes - Annotation processors
                fileObjects);       // CompilationUnits.

        task.call();
        action.run(diagnostics.getDiagnostics());
    }

}
