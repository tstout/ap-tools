package aptools.test;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ArgProcessor extends AbstractProcessor {
    public ArgProcessor() {}

    @Override public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        return false;
    }
}
