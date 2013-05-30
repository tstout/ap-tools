package aptools.test;

import org.junit.Test;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public class CaptureMethodArgNamesAPTest {

    @SupportedAnnotationTypes("*")
    @SupportedSourceVersion(SourceVersion.RELEASE_6)
    class ArgProcessor extends AbstractProcessor {

        @Override public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
            return false;
        }
    }


    @Test
    public void captureArgNameTest() {

    }

}



