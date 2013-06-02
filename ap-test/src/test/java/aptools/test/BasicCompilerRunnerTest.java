package aptools.test;

import org.junit.Test;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static com.google.common.base.Throwables.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BasicCompilerRunnerTest {

    @Test
    public void invokeJavac() {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("package test;");
        out.println("public class Echo {");
        out.println("  public static String echo(String arg) {");
        out.println("    return arg;");
        out.println("  }");
        out.println("}");
        out.close();

        Javac compiler = new Javac().addSource(new JavaSource("test.Echo", writer.toString()));

        ClassLoader loader = compiler.run(new DiagnosticAction() {
            @Override public void run(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
                dumpAnyCompileErrors(diagnostics);
                assertThat(diagnostics.size(), is(0));
            }
        });

        try {
            Object echoText = loader
                    .loadClass("test.Echo")
                    .getDeclaredMethod("echo", new Class[]{String.class})
                    .invoke(null, "argone");

            assertThat(String.class.cast(echoText), is("argone"));
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    void dumpAnyCompileErrors(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        for (Diagnostic diagnostic : diagnostics) {
            System.out.println(diagnostic.getCode());
            System.out.println(diagnostic.getKind());
            System.out.println(diagnostic.getPosition());
            System.out.println(diagnostic.getStartPosition());
            System.out.println(diagnostic.getEndPosition());
            System.out.println(diagnostic.getSource());
            System.out.println(diagnostic.getMessage(null));
        }
    }
}
