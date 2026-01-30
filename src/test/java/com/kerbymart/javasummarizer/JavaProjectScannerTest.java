package com.kerbymart.javasummarizer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JavaProjectScannerTest {

    @Test
    void isJavaSourceFile_returnsTrueOnlyForRegularJavaFiles(@TempDir Path tempDir) throws IOException {
        Path javaFile = Files.writeString(tempDir.resolve("Example.java"), "class Example {}");
        Path textFile = Files.writeString(tempDir.resolve("notes.txt"), "hello");
        Path directory = Files.createDirectory(tempDir.resolve("src"));

        JavaProjectScanner scanner = new JavaProjectScanner();

        assertTrue(scanner.isJavaSourceFile(javaFile));
        assertFalse(scanner.isJavaSourceFile(textFile));
        assertFalse(scanner.isJavaSourceFile(directory));
    }

    @Test
    void findJavaFiles_discoversNestedJavaSources(@TempDir Path tempDir) throws IOException {
        Path nested = Files.createDirectories(tempDir.resolve("a/b/c"));
        Path first = Files.writeString(nested.resolve("One.java"), "class One {}");
        Path second = Files.writeString(tempDir.resolve("Two.java"), "class Two {}");
        Files.writeString(tempDir.resolve("skip.txt"), "ignore");

        JavaProjectScanner scanner = new JavaProjectScanner();
        List<Path> found;
        try (var stream = scanner.findJavaFiles(tempDir)) {
            found = stream.toList();
        }

        assertEquals(2, found.size());
        assertTrue(found.contains(first));
        assertTrue(found.contains(second));
    }
}

