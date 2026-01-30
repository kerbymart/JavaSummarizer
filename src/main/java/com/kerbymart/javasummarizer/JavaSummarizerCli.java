package com.kerbymart.javasummarizer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Command-line entry point for the Java summarizer.
 *
 * <p>The CLI is intentionally thin: it converts command-line arguments into a {@link Path}, delegates
 * to {@link JavaSummarizer}, and translates failures into human-readable output.
 */
public final class JavaSummarizerCli {

    /**
     * Summarizes a directory tree (or a single {@code .java} file).
     *
     * @param args optional first argument is a path; defaults to current working directory
     */
    public static void main(String[] args) {
        Path inputPath = Paths.get(args.length > 0 ? args[0] : ".");
        JavaSummarizer summarizer = JavaSummarizer.defaultSummarizer();

        try {
            summarizer.summarize(inputPath, System.out, System.err);
        } catch (IOException e) {
            System.err.println("I/O error while scanning " + inputPath + ": " + e.getMessage());
        }
    }
}
