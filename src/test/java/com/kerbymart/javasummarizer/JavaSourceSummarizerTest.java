package com.kerbymart.javasummarizer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class JavaSourceSummarizerTest {

    @Test
    void summarize_extractsPackageTypesAndMethods(@TempDir Path tempDir) throws IOException {
        String source = """
                package a.b;

                public class Foo extends Bar implements Baz {
                    void m() { new String("x"); helper(); }
                    void helper() {}
                }
                """;
        Path file = Files.writeString(tempDir.resolve("Foo.java"), source);

        JavaFileSummary summary = new JavaSourceSummarizer().summarize(file);

        assertEquals("a.b", summary.packageName());
        assertEquals(file, summary.file());
        assertEquals(1, summary.types().size());

        TypeSummary foo = summary.types().getFirst();
        assertEquals("a.b", foo.packageName());
        assertEquals("Foo", foo.typeName());
        assertEquals(1, foo.extendedTypes().size());
        assertEquals("Bar", foo.extendedTypes().getFirst());
        assertEquals(1, foo.implementedTypes().size());
        assertEquals("Baz", foo.implementedTypes().getFirst());
        assertEquals(2, foo.methods().size());

        MethodSummary m = foo.methods().getFirst();
        assertTrue(m.signature().contains("m("));
        assertTrue(m.methodCalls().contains("helper"));
        assertTrue(m.objectCreations().contains("String"));
    }

    @Test
    void summarize_usesDefaultPackageWhenNoneIsDeclared(@TempDir Path tempDir) throws IOException {
        Path file = Files.writeString(tempDir.resolve("DefaultPkg.java"), "class DefaultPkg {}");

        JavaFileSummary summary = new JavaSourceSummarizer().summarize(file);

        assertEquals(JavaFileSummary.DEFAULT_PACKAGE, summary.packageName());
        assertEquals(1, summary.types().size());
        assertEquals("DefaultPkg", summary.types().getFirst().typeName());
    }
}

