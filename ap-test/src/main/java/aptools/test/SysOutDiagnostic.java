package aptools.test;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.List;

public class SysOutDiagnostic implements DiagnosticAction {
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
}
