//  Copyright 2013 Todd Stout
//
//  Licensed under the Apache License,Version2.0(the"License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing,software
//  distributed under the License is distributed on an"AS IS"BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.

package aptools.test;

import com.google.common.base.Joiner;
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

import static com.google.common.base.Throwables.*;
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
    private final List<String> classesToProcess = newArrayList();
    private final List<String> annotationProcessors = newArrayList();

    private final StandardJavaFileManager fileManager = compiler.getStandardFileManager(
            diagnostics,
            Locale.ENGLISH,
            Charset.defaultCharset());

    public Javac addSource(JavaFileObject obj) {
        fileObjects.add(obj);
        return this;
    }

    public Javac addClassForAP(Class<?> clazz) {
        classesToProcess.add(clazz.getName());
        return this;
    }

    public Javac addAnnotationProcessor(Class<?> clazz) {
        annotationProcessors.add(clazz.getName());
        return this;
    }

    public ClassLoader run(DiagnosticAction action) {
        List opts = classesToProcess.size() == 0 ?
                Arrays.asList("-d", outputFolder.getPath()) :
                Arrays.asList("-d", outputFolder.getPath(), "-processor", processorList());

        JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                fileManager,
                diagnostics,
                opts,
                classesToProcess,
                fileObjects);

        task.call();
        action.run(diagnostics.getDiagnostics());
        return newClassLoader();
    }

    private String processorList() {
        return Joiner.on(',').join(annotationProcessors);
    }

    private ClassLoader newClassLoader() {
        try {
            return new URLClassLoader(new URL[] { outputFolder.toURI().toURL() });
        } catch (Exception e) {
            throw propagate(e);
        }
    }
}
