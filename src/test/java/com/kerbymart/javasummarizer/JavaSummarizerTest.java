package com.kerbymart.javasummarizer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class JavaSummarizerTest {

    @Test
    void defaultSummarizer_canSummarizeAProject(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("Ok.java"), "class Ok { void a() { b(); } void b() {} }");

        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        JavaSummarizer summarizer = JavaSummarizer.defaultSummarizer();

        summarizer.summarize(tempDir, new PrintStream(outBuffer), new PrintStream(errBuffer));

        String out = outBuffer.toString();
        assertTrue(out.contains("Class: Ok"));
        assertTrue(errBuffer.toString().isBlank());
    }

    @Test
    void summarize_continuesWhenAFileFailsToParse(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("Ok.java"), "class Ok {}");
        Files.writeString(tempDir.resolve("Broken.java"), "class Broken { this is not java }");

        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        JavaSummarizer summarizer = JavaSummarizer.defaultSummarizer();

        summarizer.summarize(tempDir, new PrintStream(outBuffer), new PrintStream(errBuffer));

        String out = outBuffer.toString();
        String err = errBuffer.toString();
        assertTrue(out.contains("Class: Ok"));
        assertTrue(err.contains("Error summarizing"));
        assertTrue(err.contains("Broken.java"));
    }

    @Test
    void summarize_reportsMissingPaths(@TempDir Path tempDir) throws IOException {
        Path missing = tempDir.resolve("does-not-exist");

        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        JavaSummarizer summarizer = JavaSummarizer.defaultSummarizer();

        summarizer.summarize(missing, new PrintStream(outBuffer), new PrintStream(errBuffer));

        assertTrue(outBuffer.toString().isBlank());
        assertTrue(errBuffer.toString().contains("Path does not exist"));
    }

    @Test
    void constructor_rejectsNullCollaborators() {
        JavaProjectScanner scanner = new JavaProjectScanner();
        JavaSourceSummarizer sourceSummarizer = new JavaSourceSummarizer();
        SummaryFormatter formatter = new SummaryFormatter();

        assertThrows(NullPointerException.class, () -> new JavaSummarizer(null, sourceSummarizer, formatter));
        assertThrows(NullPointerException.class, () -> new JavaSummarizer(scanner, null, formatter));
        assertThrows(NullPointerException.class, () -> new JavaSummarizer(scanner, sourceSummarizer, null));
    }
}

