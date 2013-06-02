package aptools.test;

import org.junit.Test;

public class CaptureMethodArgNamesAPTest {
    @Test
    public void captureArgNameTest() {
        new Javac()
                .addClassForAP(Foo.class)
                .addAnnotationProcessor(ArgProcessor.class)
                .run(new SysOutDiagnostic());
    }
}



