package aptools.test;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.List;

public interface DiagnosticAction {
    void run(List<Diagnostic<? extends JavaFileObject>> diagnostics);
}
