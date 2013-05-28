package aptools.test;

import org.junit.Test;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class BasicCompilerRunnerTest {

    @Test
    public void invokeJavac() {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("public class HelloWorld {");
        out.println("  public static void main(String args[]) {");
        out.println("    System.out.println(\"Hello World\");");
        out.println("  }");
        out.println("}");
        out.close();

        Javac compiler = new Javac();
        compiler.addSource(new JavaSourceFromString("HelloWorld", writer.toString()));

        compiler.compile(new DiagnosticAction() {
            @Override public void run(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
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
        });

        try {
            Class.forName("HelloWorld").getDeclaredMethod("main", new Class[]{String[].class})
                    .invoke(null, new Object[]{null});
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e);
        } catch (NoSuchMethodException e) {
            System.err.println("No such method: " + e);
        } catch (IllegalAccessException e) {
            System.err.println("Illegal access: " + e);
        } catch (InvocationTargetException e) {
            System.err.println("Invocation target: " + e);
        }
    }
}
