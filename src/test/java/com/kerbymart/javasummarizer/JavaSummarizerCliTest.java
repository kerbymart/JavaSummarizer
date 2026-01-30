package com.kerbymart.javasummarizer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class JavaSummarizerCliTest {

    @Test
    void main_printsASummaryToStdout(@TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("Hello.java"), "class Hello {}");

        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(outBuffer));
            System.setErr(new PrintStream(errBuffer));
            JavaSummarizerCli.main(new String[]{tempDir.toString()});
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        assertTrue(outBuffer.toString().contains("Class: Hello"));
        assertTrue(errBuffer.toString().isBlank());
    }
}

